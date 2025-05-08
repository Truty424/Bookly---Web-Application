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
import jakarta.servlet.http.HttpSession;

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

            String path = req.getRequestURI();
            if (path.matches(".*/cart/?")) {
                handleViewCart(req, resp, userId);
            } else {
                ServletUtils.redirectToErrorPage(req, resp, "Invalid path in CartServlet: " + path);
            }

        } catch (Exception e) {
            LOGGER.error("CartServlet GET error: {}", e.getMessage(), e);
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

            String path = req.getPathInfo();
            if (path == null || path.isBlank()) {
                ServletUtils.redirectToErrorPage(req, resp, "Missing cart action path.");
                return;
            }

            String[] parts = path.split("/");
            if (parts.length < 2) {
                ServletUtils.redirectToErrorPage(req, resp, "Invalid cart path.");
                return;
            }

            String action = parts[1];
            int bookId = (parts.length >= 3 && parts[2].matches("\\d+")) ? Integer.parseInt(parts[2]) : -1;

            switch (action) {
                case "add" ->
                    handleAddBook(req, resp, userId, bookId);
                case "remove" ->
                    handleRemoveBook(req, resp, userId, bookId);
                case "clear" ->
                    handleClearCart(req, resp, userId);
                case "apply-discount" ->
                    handleApplyDiscount(req, resp, userId);
                case "continue-shopping" ->
                    resp.sendRedirect(req.getContextPath() + "/book");
                default ->
                    ServletUtils.redirectToErrorPage(req, resp, "Unknown cart action: " + action);
            }

        } catch (Exception e) {
            LOGGER.error("CartServlet POST error: {}", e.getMessage(), e);
            ServletUtils.redirectToErrorPage(req, resp, "CartServlet POST error: " + e.getMessage());
        } finally {
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }

    private void handleViewCart(HttpServletRequest req, HttpServletResponse resp, int userId) throws Exception {
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

        double totalPrice = cartBooks.stream().mapToDouble(Book::getPrice).sum();
        double discountedTotal = totalPrice;
        Discount appliedDiscount = (Discount) req.getSession().getAttribute("appliedDiscount");
        if (appliedDiscount != null) {
            var applyDiscountDAO = new ApplyDiscountToCartDAO(getConnection(), cartId, appliedDiscount.getDiscountId());
            applyDiscountDAO.access();
            discountedTotal = applyDiscountDAO.getOutputParam();
            req.setAttribute("applied_discount", appliedDiscount);
        } else if (req.getSession().getAttribute("discountError") != null) {
            req.setAttribute("discount_error", req.getSession().getAttribute("discountError"));
            req.getSession().removeAttribute("discountError");
        }

        req.setAttribute("cart_books", cartBooks);
        req.setAttribute("total_price", totalPrice);
        req.setAttribute("final_total", discountedTotal);

        req.getRequestDispatcher("/jsp/cart/viewCart.jsp").forward(req, resp);
    }

    private void handleAddBook(HttpServletRequest req, HttpServletResponse resp, int userId, int bookId) throws Exception {
        if (bookId == -1) {
            return;
        }
        try (Connection con = getConnection()) {
            int cartId = getOrCreateCartId(userId);
            new AddBookToCartDAO(con, bookId, cartId).access();
        }
        resp.sendRedirect(req.getContextPath() + "/cart");
    }

    private void handleRemoveBook(HttpServletRequest req, HttpServletResponse resp, int userId, int bookId) throws Exception {
        if (bookId == -1) {
            return;
        }
        try (Connection con = getConnection()) {
            int cartId = getOrCreateCartId(userId);
            new RemoveBookFromCartDAO(con, bookId, cartId).access();
        }
        resp.sendRedirect(req.getContextPath() + "/cart");
    }

    private void handleClearCart(HttpServletRequest req, HttpServletResponse resp, int userId) throws Exception {
        try (Connection con = getConnection()) {
            int cartId = getOrCreateCartId(userId);
            new ClearCartDAO(con, cartId).access();
        }
        req.getSession().removeAttribute("appliedDiscount");
        resp.sendRedirect(req.getContextPath() + "/cart");
    }

    private void handleApplyDiscount(HttpServletRequest req, HttpServletResponse resp, int userId) throws Exception {
        String code = req.getParameter("discount");
        HttpSession session = req.getSession();

        if (code == null || code.trim().isEmpty()) {
            session.setAttribute("discountError", "Please enter a discount code.");
            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        }

        try (Connection con = getConnection()) {
            int cartId = getOrCreateCartId(userId);
            Discount discount = new GetValidDiscountByCodeDAO(con, code).access().getOutputParam();

            if (discount != null) {
                session.setAttribute("appliedDiscount", discount);
                session.removeAttribute("discountError");
                LOGGER.info("Applied discount '{}' to cart ID {}", code, cartId);
            } else {
                session.setAttribute("discountError", "Invalid or expired discount code.");
                LOGGER.warn("Failed discount attempt for code '{}'.", code);
            }
        }
        resp.sendRedirect(req.getContextPath() + "/cart");
    }

    private int getOrCreateCartId(int userId) throws Exception {
        Cart cart = null;

        // Try to fetch the existing cart
        try (Connection con = getConnection()) {
            cart = new GetCartByUserIdDAO(con, userId).access().getOutputParam();
        } catch (Exception e) {
            LOGGER.error("Error retrieving cart for user ID {}: {}", userId, e.getMessage(), e);
            throw e;
        }

        // If cart exists, return the ID
        if (cart != null) {
            return cart.getCartId();
        }

        // Otherwise, create a new cart
        int newCartId;
        try (Connection con = getConnection()) {
            newCartId = new CreateCartForUserDAO(con, userId, "in_person").access().getOutputParam();
        } catch (Exception e) {
            LOGGER.error("Error creating cart for user ID {}: {}", userId, e.getMessage(), e);
            throw e;
        }

        if (newCartId <= 0) {
            throw new IllegalStateException("Failed to create a new cart for user: " + userId);
        }

        return newCartId;
    }

}
