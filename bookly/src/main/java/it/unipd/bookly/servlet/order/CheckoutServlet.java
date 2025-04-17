package it.unipd.bookly.servlet.order;

import java.io.IOException;
import java.sql.Connection;

import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.Resource.Order;
import it.unipd.bookly.dao.order.InsertOrderDAO;
import it.unipd.bookly.servlet.AbstractDatabaseServlet;
import it.unipd.bookly.utilities.ServletUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "CheckoutServlet", value = "/checkout")
public class CheckoutServlet extends AbstractDatabaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        Book book = (Book) (session != null ? session.getAttribute("checkout_book") : null);

        if (book == null) {
            ServletUtils.redirectToErrorPage(req, res, "No book selected for checkout.");
            return;
        }

        req.setAttribute("book", book);
        req.getRequestDispatcher("/jsp/order/checkout.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        Book book = (Book) (session != null ? session.getAttribute("checkout_book") : null);
        String address = req.getParameter("address");
        String paymentMethod = req.getParameter("paymentMethod");

        if (book == null || address == null || paymentMethod == null || address.isBlank() || paymentMethod.isBlank()) {
            LOGGER.warn("Order submission failed: missing book or form data.");
            ServletUtils.redirectToErrorPage(req, res, "Please complete all fields to confirm your order.");
            return;
        }

        try (Connection con = getConnection()) {
            Order order = new Order(
                book.getPrice(),
                paymentMethod,
                address,
                null,           // shipment code
                "placed"        // default status
            );

            boolean success = new InsertOrderDAO(con, order).access().getOutputParam();

            if (success) {
                session.removeAttribute("checkout_book");
                LOGGER.info("Order placed successfully for book: " + book.getTitle());
                res.sendRedirect(req.getContextPath() + "/orders");
            } else {
                LOGGER.error("Database insertion for order failed.");
                ServletUtils.redirectToErrorPage(req, res, "Could not complete the order.");
            }
        } catch (Exception e) {
            LOGGER.error("Checkout failed", e);
            ServletUtils.redirectToErrorPage(req, res, "Unexpected error during checkout.");
        }
    }
}
