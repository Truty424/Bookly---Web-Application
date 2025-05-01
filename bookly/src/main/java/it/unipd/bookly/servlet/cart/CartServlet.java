package it.unipd.bookly.servlet.cart;

import it.unipd.bookly.LogContext;
import it.unipd.bookly.Resource.Book;
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

            String path = req.getPathInfo() != null ? req.getPathInfo() : "";

            switch (path) {
                case "/", "" -> showCart(req, resp, userId);
                default -> ServletUtils.redirectToErrorPage(req, resp, "Invalid cart path: " + path);
            }

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

            String path = req.getPathInfo() != null ? req.getPathInfo() : "";
            Connection con = getConnection();
            int cartId = getOrCreateCartId(userId);

            switch (normalizePath(path)) {
                case "add" -> {
                    int bookId = extractBookIdFromPath(path);
                    new AddBookToCartDAO(con, bookId, cartId).access();
                    resp.sendRedirect(req.getContextPath() + "/cart");
                }
                case "remove" -> {
                    int bookId = extractBookIdFromPath(path);
                    new RemoveBookFromCartDAO(con, bookId, cartId).access();
                    resp.sendRedirect(req.getContextPath() + "/cart");
                }
                case "clear" -> {
                    new ClearCartDAO(con, cartId).access();
                    req.getSession().removeAttribute("appliedDiscount");
                    req.getSession().removeAttribute("discountId");
                    resp.sendRedirect(req.getContextPath() + "/cart");
                }
                case "apply-discount" -> {
                    handleApplyDiscount(req, resp, con, cartId);
                }
                default -> {
                    ServletUtils.redirectToErrorPage(req, resp, "Unknown cart action: " + path);
                }
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
        Connection con = getConnection();
        var cart = new GetCartByUserIdDAO(con, userId).access().getOutputParam();

        if (cart == null) {
            int newCartId = new CreateCartForUserDAO(con, userId, "standard").access().getOutputParam();
            cart = new GetCartByUserIdDAO(con, newCartId).access().getOutputParam();
        }

        int cartId = cart.getCartId();
        List<Book> cartBooks = new GetBooksInCartDAO(con, cartId).access().getOutputParam();

        double total = cartBooks.stream().mapToDouble(Book::getPrice).sum();
        double finalTotal = total;

        Discount discount = (Discount) req.getSession().getAttribute("appliedDiscount");

        if (discount != null) {
            finalTotal = new ApplyDiscountToCartDAO(con, cartId, discount.getDiscountId()).access().getOutputParam();
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

    private int extractBookIdFromPath(String path) {
        String[] parts = path.split("/");
        return Integer.parseInt(parts[parts.length - 1].trim());
    }

    private int getOrCreateCartId(int userId) throws Exception {
        var cart = new GetCartByUserIdDAO(getConnection(), userId).access().getOutputParam();
        if (cart == null) {
            return new CreateCartForUserDAO(getConnection(), userId, "standard").access().getOutputParam();
        }
        return cart.getCartId();
    }

    private String normalizePath(String rawPath) {
        if (rawPath == null || rawPath.isBlank()) return "";
        if (rawPath.matches("/add/\\d+/?")) return "add";
        if (rawPath.matches("/remove/\\d+/?")) return "remove";
        if (rawPath.matches("/clear/?")) return "clear";
        if (rawPath.matches("/apply-discount/?")) return "apply-discount";
        return "unknown";
    }
}
