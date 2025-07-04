package it.unipd.bookly.servlet.order;

import it.unipd.bookly.LogContext;
import it.unipd.bookly.Resource.Cart;
import it.unipd.bookly.Resource.Order;
import it.unipd.bookly.Resource.User;
import it.unipd.bookly.Resource.Author;
import it.unipd.bookly.dao.cart.ClearCartDAO;
import it.unipd.bookly.dao.cart.CreateCartForUserDAO;
import it.unipd.bookly.dao.cart.GetCartByUserIdDAO;
import it.unipd.bookly.dao.cart.LinkOrderToCartDAO;
import it.unipd.bookly.dao.order.GetOrderWithBooksDAO;
import it.unipd.bookly.dao.order.GetOrdersByUserDAO;
import it.unipd.bookly.dao.order.InsertOrderDAO;
import it.unipd.bookly.dao.order.InsertOrderItemsDAO;
import it.unipd.bookly.dao.order.GetOrderByIdDAO;
import it.unipd.bookly.dao.author.GetAuthorsByBookListDAO;
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
import java.util.Map;
import java.util.HashMap;

@WebServlet(name = "OrderServlet", value = "/orders/*")
public class OrderServlet extends AbstractDatabaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction("OrderServlet");

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            res.sendRedirect(req.getContextPath() + "/user/login");
            return;
        }

        try (Connection con = getConnection()) {
            User user = (User) session.getAttribute("user");

            String pathInfo = req.getPathInfo(); // e.g. "/42"
            if (pathInfo != null && pathInfo.matches("/\\d+")) {
                // === Individual Order Details ===
                int orderId = Integer.parseInt(pathInfo.substring(1));

                System.out.println("Trying to load order with ID: " + orderId);

                Order order = new GetOrderWithBooksDAO(con, orderId).access().getOutputParam();

                if (order == null) {
                    LOGGER.warn("⚠️ No books found for order. Falling back to basic order info...");
                    try (Connection fallbackCon = getConnection()) {
                        order = new GetOrderByIdDAO(fallbackCon, orderId).access().getOutputParam();
                    }
                }
                Map<Integer, List<Author>> authorsMap = new HashMap<>();
                if (order != null && order.getBooks() != null && !order.getBooks().isEmpty()) {
                    List<Integer> bookIds = order.getBooks().stream().map(b -> b.getBookId()).toList();
                    try (Connection authorCon = getConnection()) {
                        authorsMap = new GetAuthorsByBookListDAO(authorCon, bookIds).access().getOutputParam();
                    } catch (Exception e) {
                        LOGGER.warn("Could not load authors for order view: {}", e.getMessage());
                    }
                }
                if (order == null) {
                    ServletUtils.redirectToErrorPage(req, res, "Order not found.");
                    return;
                }

                req.setAttribute("order", order);
                req.setAttribute("authors_map", authorsMap);
                req.getRequestDispatcher("/jsp/order/orderDetails.jsp").forward(req, res);
                return;
            }

            // === All Orders ===
            List<Order> orders = new GetOrdersByUserDAO(con, user.getUserId()).access().getOutputParam();
            req.setAttribute("orders", orders);
            req.getRequestDispatcher("/jsp/order/userOrders.jsp").forward(req, res);

        } catch (Exception e) {
            LOGGER.error("OrderServlet GET error", e);
            ServletUtils.redirectToErrorPage(req, res, "Failed to load your orders.");
        } finally {
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction("OrderServlet");

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            res.sendRedirect(req.getContextPath() + "/user/login");
            return;
        }

        try (Connection con = getConnection()) {
            User user = (User) session.getAttribute("user");

            Cart cart = new GetCartByUserIdDAO(con, user.getUserId()).access().getOutputParam();
            if (cart == null || cart.getCartId() == 0) {
                ServletUtils.redirectToErrorPage(req, res, "No cart available to place an order.");
                return;
            }

            double total = getFinalTotalFromSessionOrCart(req, cart);
            Order order = buildOrder(req, total);

            int orderId = new InsertOrderDAO(con, order).access().getOutputParam();
            if (orderId <= 0) {
                ServletUtils.redirectToErrorPage(req, res, "Failed to insert order.");
                return;
            }

            new LinkOrderToCartDAO(con, cart.getCartId(), orderId).access();
            new InsertOrderItemsDAO(con, cart.getCartId(), orderId).access();
            LOGGER.info("InsertOrderItemsDAO ran for orderId={}, cartId={}", orderId, cart.getCartId());
            new ClearCartDAO(con, cart.getCartId()).access();
            new CreateCartForUserDAO(con, user.getUserId(), order.getPaymentMethod()).access();

            clearDiscountSession(session);

            res.sendRedirect(req.getContextPath() + "/orders");

        } catch (Exception e) {
            LOGGER.error("OrderServlet POST error", e);
            ServletUtils.redirectToErrorPage(req, res, "Error placing the order.");
        } finally {
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }

    // 🔽 Helpers
    private double getFinalTotalFromSessionOrCart(HttpServletRequest req, Cart cart) {
        Object value = req.getSession().getAttribute("cart_final_price");
        if (value instanceof Double) return (Double) value;
        return cart.getTotalPrice();
    }

    private Order buildOrder(HttpServletRequest req, double total) {
        Order order = new Order();
        order.setTotalPrice(total);
        order.setPaymentMethod(req.getParameter("paymentMethod") != null ? req.getParameter("paymentMethod") : "credit_card");
        order.setStatus("placed");
        order.setAddress(req.getParameter("address") != null ? req.getParameter("address") : "Default Address");
        order.setShipmentCode("AUTO-GENERATED");
        return order;
    }

    private void clearDiscountSession(HttpSession session) {
        session.removeAttribute("appliedDiscount");
        session.removeAttribute("discountId");
        session.removeAttribute("cart_final_price");
    }
}
