package it.unipd.bookly.servlet.cart;

import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.dao.cart.*;
import it.unipd.bookly.servlet.AbstractDatabaseServlet;
import it.unipd.bookly.LogContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import it.unipd.bookly.dao.discount.GetValidDiscountByCodeDAO;

@WebServlet(name = "CartServlet", value = "/cart/*")
public class CartServlet extends AbstractDatabaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction("cartServlet");

        try {
            Integer userId = (Integer) req.getSession().getAttribute("userId");

            if (userId == null) {
                resp.sendRedirect("/html/login.html"); // redirect to log in if user not logged in
                return;
            }

            String path = req.getRequestURI();

            if (path.matches(".*/cart/?")) {
                showCart(req, resp, userId);
            } else {
                resp.sendRedirect("/html/error.html");
            }

        } catch (Exception e) {
            LOGGER.error("CartServlet GET error: {}", e.getMessage());
            resp.sendRedirect("/html/error.html");
        } finally {
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction("cartServlet");

        try {
            Integer userId = (Integer) req.getSession().getAttribute("userId");

            if (userId == null) {
                resp.sendRedirect("/html/login.html");
                return;
            }

            String path = req.getRequestURI();
            int cartId = getOrCreateCartId(userId);

            if (path.matches(".*/cart/add/\\d+/?")) {
                int bookId = extractBookIdFromPath(path);
                new AddBookToCartDAO(getConnection(), bookId, cartId).access();
                resp.sendRedirect("/cart");

            } else if (path.matches(".*/cart/remove/\\d+/?")) {
                int bookId = extractBookIdFromPath(path);
                new RemoveBookFromCartDAO(getConnection(), bookId, cartId).access();
                resp.sendRedirect("/cart");
            } else if (path.matches(".*/cart/clear/?")) {
                new ClearCartDAO(getConnection(), cartId).access();
                resp.sendRedirect("/cart");
            } else {
                resp.sendRedirect("/html/error.html");
            }

        } catch (Exception e) {
            LOGGER.error("CartServlet POST error: {}", e.getMessage());
            resp.sendRedirect("/html/error.html");
        } finally {
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }

    private void showCart(HttpServletRequest req, HttpServletResponse resp, int userId) throws Exception {
        var cart = new GetCartByUserIdDAO(getConnection(), userId).access().getOutputParam();

        if (cart == null) {
            int newCartId = new CreateCartForUserDAO(getConnection(), userId, "standard").access().getOutputParam();
            cart = new GetCartByUserIdDAO(getConnection(), userId).access().getOutputParam();
        }

        int cartId = cart.getCartId();
        List<Book> cartBooks = new GetBooksInCartDAO(getConnection(), cartId).access().getOutputParam();

        double totalPrice = cartBooks.stream().mapToDouble(Book::getPrice).sum();
        double discountAmount = 0;
        String discountCode = req.getParameter("discount"); // or use session attribute

        if (discountCode != null && !discountCode.isBlank()) {
            var discountDAO = new GetValidDiscountByCodeDAO(getConnection(), discountCode);
            discountDAO.access();
            var discount = discountDAO.getOutputParam();

            if (discount != null) {
                discountAmount = totalPrice * (discount.getDiscountPercentage() / 100); // assuming rate is 0.10 for 10%
                req.setAttribute("applied_discount", discount);
            } else {
                req.setAttribute("discount_error", "Invalid or expired discount code.");
            }
        }

        double finalTotal = totalPrice - discountAmount;

        req.setAttribute("cart_books", cartBooks);
        req.setAttribute("total_price", totalPrice);
        req.setAttribute("discount_amount", discountAmount);
        req.setAttribute("final_total", finalTotal);

        req.getRequestDispatcher("/jsp/cart/viewCart.jsp").forward(req, resp);
    }

    private int extractBookIdFromPath(String path) throws NumberFormatException {
        String[] segments = path.split("/");
        String last = segments[segments.length - 1];
        return Integer.parseInt(last.trim());
    }

    private int getOrCreateCartId(int userId) throws Exception {
        var cart = new GetCartByUserIdDAO(getConnection(), userId).access().getOutputParam();
        if (cart == null) {
            return new CreateCartForUserDAO(getConnection(), userId, "standard").access().getOutputParam();
        }
        return cart.getCartId();
    }

}
