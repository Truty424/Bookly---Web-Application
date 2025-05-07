package it.unipd.bookly.servlet.cart;

import it.unipd.bookly.LogContext;
import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.Resource.Cart;
import it.unipd.bookly.Resource.Discount;
import it.unipd.bookly.dao.cart.*;
import it.unipd.bookly.dao.discount.GetValidDiscountByCodeDAO;
import it.unipd.bookly.servlet.AbstractDatabaseServlet;
import it.unipd.bookly.utilities.ServletUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@WebServlet(name = "CartServlet", value = "/cart/*")
public class CartServlet extends AbstractDatabaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction("CartServlet");

        try {
            Integer userId = (Integer) req.getSession().getAttribute("userId");
            if (userId == null) {
                resp.sendRedirect(req.getContextPath() + "/user/login");
                return;
            }

            showCart(req, resp, userId);

        } catch (Exception e) {
            LOGGER.error("CartServlet GET error", e);
            ServletUtils.redirectToErrorPage(req, resp, "Unable to load your cart.");
        } finally {
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction("CartServlet");

        try {
            Integer userId = (Integer) req.getSession().getAttribute("userId");
            if (userId == null) {
                resp.sendRedirect(req.getContextPath() + "/user/login");
                return;
            }

            String path = req.getPathInfo(); // e.g., "/add/42"
            if (path == null || path.isBlank()) {
                ServletUtils.redirectToErrorPage(req, resp, "Missing cart action path.");
                return;
            }

            String[] parts = path.split("/");
            if (parts.length < 2) {
                ServletUtils.redirectToErrorPage(req, resp, "Invalid cart path.");
                return;
            }

            String action = parts[1]; // "add"
            int bookId = (parts.length >= 3 && parts[2].matches("\\d+")) ? Integer.parseInt(parts[2]) : -1;
            
            int cartId;
            try (Connection con = getConnection()) {
                cartId = getOrCreateCartId(con, userId);
            }

            switch (action) {
                case "add" -> {
                    if (bookId != -1) {
                        try (Connection con = getConnection()) {
                            new AddBookToCartDAO(con, bookId, cartId).access();
                        }
                    }
                    resp.sendRedirect(req.getContextPath() + "/cart");
                }
                case "remove" -> {
                    if (bookId != -1) {
                        try (Connection con = getConnection()) {
                            new RemoveBookFromCartDAO(con, bookId, cartId).access();
                        }
                    }
                    resp.sendRedirect(req.getContextPath() + "/cart");
                }
                case "clear" -> {
                    try (Connection con = getConnection()) {
                        new ClearCartDAO(con, cartId).access();
                    }
                    req.getSession().removeAttribute("appliedDiscount");
                    resp.sendRedirect(req.getContextPath() + "/cart");
                }
                case "apply-discount" -> {
                    try (Connection con = getConnection()) {
                        handleApplyDiscount(req, resp, con, cartId);
                    }
                }
                case "continue-shopping" ->
                    resp.sendRedirect(req.getContextPath() + "/book");
                default ->
                    ServletUtils.redirectToErrorPage(req, resp, "Unknown cart action: " + action);
            }

        } catch (Exception e) {
            LOGGER.error("CartServlet POST error", e);
            ServletUtils.redirectToErrorPage(req, resp, "CartServlet POST error: " + e.getMessage());
        } finally {
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }

    private void showCart(HttpServletRequest req, HttpServletResponse resp, int userId) throws Exception {
        Cart cart;

        try (Connection con = getConnection()) {
            cart = new GetCartByUserIdDAO(con, userId).access().getOutputParam();
        }

        if (cart == null) {
            try (Connection con = getConnection()) {
                new CreateCartForUserDAO(con, userId, "in_person").access();
            }

            try (Connection con = getConnection()) {
                cart = new GetCartByUserIdDAO(con, userId).access().getOutputParam();
            }
        }

        int cartId = cart.getCartId();

        List<Book> cartBooks;
        try (Connection con = getConnection()) {
            cartBooks = new GetBooksInCartDAO(con, cartId).access().getOutputParam();
        }

        double total = cartBooks.stream().mapToDouble(Book::getPrice).sum();
        double finalTotal = total;

        Discount discount = (Discount) req.getSession().getAttribute("appliedDiscount");
        if (discount != null) {
            try (Connection con = getConnection()) {
                finalTotal = new ApplyDiscountToCartDAO(con, cartId, discount.getDiscountId()).access().getOutputParam();
            }
            req.setAttribute("applied_discount", discount);
        } else if (req.getSession().getAttribute("discountError") != null) {
            req.setAttribute("discount_error", req.getSession().getAttribute("discountError"));
            req.getSession().removeAttribute("discountError");
        }

        req.setAttribute("cart_books", cartBooks);
        req.setAttribute("total_price", total);
        req.setAttribute("final_total", finalTotal);
        req.getSession().setAttribute("cart_final_price", finalTotal);

        req.getRequestDispatcher("/jsp/cart/viewCart.jsp").forward(req, resp);
    }

    private void handleApplyDiscount(HttpServletRequest req, HttpServletResponse resp, Connection con, int cartId) throws Exception {
        String discountCode = req.getParameter("discount");

        if (discountCode == null || discountCode.isBlank()) {
            req.getSession().setAttribute("discountError", "Discount code is required.");
            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        }

        Discount discount = new GetValidDiscountByCodeDAO(con, discountCode).access().getOutputParam();

        if (discount != null) {
            req.getSession().setAttribute("appliedDiscount", discount);
            req.getSession().removeAttribute("discountError");
            LOGGER.info("Discount '{}' applied to cart {}", discountCode, cartId);
        } else {
            req.getSession().setAttribute("discountError", "Invalid or expired discount code.");
        }

        resp.sendRedirect(req.getContextPath() + "/cart");
    }

    private int getOrCreateCartId(Connection con, int userId) throws Exception {
        Cart cart = new GetCartByUserIdDAO(con, userId).access().getOutputParam();
        if (cart == null) {
            return new CreateCartForUserDAO(con, userId, "in_person").access().getOutputParam();
        }
        return cart.getCartId();
    }
}
