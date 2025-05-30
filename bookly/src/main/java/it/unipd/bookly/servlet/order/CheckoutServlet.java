package it.unipd.bookly.servlet.order;

import it.unipd.bookly.LogContext;
import it.unipd.bookly.Resource.*;
import it.unipd.bookly.dao.cart.*;
import it.unipd.bookly.dao.order.InsertOrderDAO;
import it.unipd.bookly.dao.order.InsertOrderItemsDAO;
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

@WebServlet(name = "CheckoutServlet", value = "/checkout")
public class CheckoutServlet extends AbstractDatabaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction("CheckoutServlet");

        HttpSession session = req.getSession(false);
        if (!isUserLoggedIn(session)) {
            res.sendRedirect(req.getContextPath() + "/user/login");
            return;
        }

        try {
            User user = (User) session.getAttribute("user");
            if (user != null && user.getAddress() != null) {
                req.setAttribute("savedAddress", user.getAddress());
            }

            Cart cart = getCartByUserId(getConnection(), user.getUserId());

            if (cart == null) {
                ServletUtils.redirectToErrorPage(req, res, "Your cart is empty.");
                return;
            }

            List<Book> books = getBooksInCart(getConnection(), cart.getCartId());
            double total = calculateCartTotal(books);
            double finalTotal = applyDiscountIfExists(getConnection(), cart.getCartId(), session);

            // Format the final total to 2 decimal places
            String formattedTotal = String.format("%.2f", total);
            String formattedFinalTotal = String.format("%.2f", finalTotal);

            // Pass the formatted values to the JSP
            req.setAttribute("cart_books", books);
            req.setAttribute("total_price", total);
            req.setAttribute("final_total", finalTotal);

            req.getRequestDispatcher("/jsp/order/checkout.jsp").forward(req, res);
        } catch (Exception e) {
            LOGGER.error("Checkout GET error: {}", e.getMessage(), e);
            ServletUtils.redirectToErrorPage(req, res, "Failed to load checkout page.");
        } finally {
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction("CheckoutServlet");

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            res.sendRedirect(req.getContextPath() + "/user/login");
            return;
        }

        String address = req.getParameter("address");
        String paymentMethod = req.getParameter("paymentMethod");
        String cardNumber = req.getParameter("cardNumber");
        String cvv = req.getParameter("cvv");
        String expiry = req.getParameter("expiry");

        if (paymentMethod == null || paymentMethod.isBlank()) {
            ServletUtils.redirectToErrorPage(req, res, "Payment method is required.");
        } else if ("credit_card".equals(paymentMethod)) {
            if (cardNumber == null || !cardNumber.replaceAll("\\s+", "").matches("\\d{13,19}")) {
                ServletUtils.redirectToErrorPage(req, res, "Invalid card number.");
            }
            if (cvv == null || !cvv.matches("\\d{3,4}")) {
                ServletUtils.redirectToErrorPage(req, res, "Invalid CVV.");
            }
            if (expiry == null || !expiry.matches("\\d{4}-\\d{2}")) {
                ServletUtils.redirectToErrorPage(req, res, "Invalid expiry date format.");
            } else {
                // Validate if expiry date is future
                String[] parts = expiry.split("-");
                int year = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]);
                java.time.YearMonth now = java.time.YearMonth.now();
                java.time.YearMonth exp = java.time.YearMonth.of(year, month);
                if (exp.isBefore(now)) {
                    ServletUtils.redirectToErrorPage(req, res, "Card expiry must be in the future.");
                }
            }
        } else if ("in_person".equals(paymentMethod)) {
            if (address == null || address.isBlank()) {
                ServletUtils.redirectToErrorPage(req, res, "Delivery address is required.");
            }
        }

        try {
            User user = (User) session.getAttribute("user");
            Cart cart;
            List<Book> books;

            try (Connection con = getConnection()) {
                cart = new GetCartByUserIdDAO(con, user.getUserId()).access().getOutputParam();
                if (cart == null) {
                    ServletUtils.redirectToErrorPage(req, res, "No active cart to place an order.");
                    return;
                }
            }

            try (Connection con = getConnection()) {
                books = new GetBooksInCartDAO(con, cart.getCartId()).access().getOutputParam();
            }

            double total = books.stream().mapToDouble(Book::getPrice).sum();
            double finalTotal = total;

            Discount discount = (Discount) session.getAttribute("appliedDiscount");
            if (discount != null) {
                try (Connection con = getConnection()) {
                    finalTotal = new ApplyDiscountToCartDAO(con, cart.getCartId(), discount.getDiscountId()).access().getOutputParam();
                }
            }

            int orderId;
            Order order = new Order(finalTotal, paymentMethod, address, null, "placed");
            try (Connection con = getConnection()) {
                orderId = new InsertOrderDAO(con, order).access().getOutputParam();
            }

            if (orderId <= 0) {
                throw new IllegalStateException("Order insertion failed");
            }

            try (Connection con = getConnection()) {
                new LinkOrderToCartDAO(con, cart.getCartId(), orderId).access();
            }

            try (Connection con = getConnection()) {
                new InsertOrderItemsDAO(con, cart.getCartId(), orderId).access();
            }

            try (Connection con = getConnection()) {
                new ClearCartDAO(con, cart.getCartId()).access();
            }

            try (Connection con = getConnection()) {
                new CreateCartForUserDAO(con, user.getUserId(), paymentMethod).access();
            }

            clearDiscountSession(session);

            res.sendRedirect(req.getContextPath() + "/orders");

        } catch (Exception e) {
            LOGGER.error("Checkout POST error: {}", e.getMessage(), e);
            ServletUtils.redirectToErrorPage(req, res, "An error occurred while placing your order.");
        } finally {
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }

    // ======== Private Helper Methods ========
    private boolean isUserLoggedIn(HttpSession session) {
        return session != null && session.getAttribute("user") != null;
    }

    private Cart getCartByUserId(Connection con, int userId) throws Exception {
        return new GetCartByUserIdDAO(con, userId).access().getOutputParam();
    }

    private List<Book> getBooksInCart(Connection con, int cartId) throws Exception {
        return new GetBooksInCartDAO(con, cartId).access().getOutputParam();
    }

    private double calculateCartTotal(List<Book> books) {
        return books.stream().mapToDouble(Book::getPrice).sum();
    }

    private double applyDiscountIfExists(Connection con, int cartId, HttpSession session) throws Exception {
        Discount discount = (Discount) session.getAttribute("appliedDiscount");
        if (discount != null) {
            return new ApplyDiscountToCartDAO(con, cartId, discount.getDiscountId()).access().getOutputParam();
        }
        List<Book> books = getBooksInCart(con, cartId);
        return calculateCartTotal(books);
    }

    private Cart getCartById(Connection con, int cartId) throws Exception {
        return new GetCartByUserIdDAO(con, cartId).access().getOutputParam();
    }

    private void clearDiscountSession(HttpSession session) {
        session.removeAttribute("appliedDiscount");
        session.removeAttribute("discountId");
    }
}
