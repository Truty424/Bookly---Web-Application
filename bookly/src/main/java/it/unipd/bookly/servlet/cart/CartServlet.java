package it.unipd.bookly.servlet.cart;

import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.Resource.Discount;
import it.unipd.bookly.dao.cart.*;
import it.unipd.bookly.dao.discount.GetValidDiscountByCodeDAO;
import it.unipd.bookly.servlet.AbstractDatabaseServlet;
import it.unipd.bookly.LogContext;
import it.unipd.bookly.utilities.ServletUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

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
                resp.sendRedirect(req.getContextPath() + "/jsp/user/login.jsp");
                return;
            }

            String path = req.getRequestURI();

            if (path.matches(".*/cart/?")) {
                showCart(req, resp, userId);
            } else {
                ServletUtils.redirectToErrorPage(req, resp, "Invalid cart path: " + path);
            }

        } catch (Exception e) {
            LOGGER.error("CartServlet GET error: {}", e.getMessage(), e);
            ServletUtils.redirectToErrorPage(req, resp, "CartServlet GET error: " + e.getMessage());
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
                resp.sendRedirect(req.getContextPath() + "/jsp/user/login.jsp");
                return;
            }

            String path = req.getRequestURI();
            int cartId = getOrCreateCartId(userId);

            if (path.matches(".*/cart/add/\\d+/?")) {
                int bookId = extractBookIdFromPath(path);
                new AddBookToCartDAO(getConnection(), bookId, cartId).access();
                resp.sendRedirect(req.getContextPath() + "/cart");

            } else if (path.matches(".*/cart/remove/\\d+/?")) {
                int bookId = extractBookIdFromPath(path);
                new RemoveBookFromCartDAO(getConnection(), bookId, cartId).access();
                resp.sendRedirect(req.getContextPath() + "/cart");

            } else if (path.matches(".*/cart/clear/?")) {
                new ClearCartDAO(getConnection(), cartId).access();
                resp.sendRedirect(req.getContextPath() + "/cart");

            } else if (path.matches(".*/cart/apply-discount/?")) {
                String discountCode = req.getParameter("discount");

                var discountDAO = new GetValidDiscountByCodeDAO(getConnection(), discountCode);
                discountDAO.access();
                Discount discount = discountDAO.getOutputParam();

                if (discount != null) {
                    req.getSession().setAttribute("discountId", discount.getDiscountId());
                    req.getSession().setAttribute("appliedDiscount", discount);
                    req.getSession().removeAttribute("discountError");
                } else {
                    req.getSession().setAttribute("discountError", "Invalid or expired discount.");
                }

                resp.sendRedirect(req.getContextPath() + "/cart");

            } else {
                ServletUtils.redirectToErrorPage(req, resp, "Invalid cart operation: " + path);
            }

        } catch (Exception e) {
            LOGGER.error("CartServlet POST error: {}", e.getMessage(), e);
            ServletUtils.redirectToErrorPage(req, resp, "CartServlet POST error: " + e.getMessage());
        } finally {
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }

    private void showCart(HttpServletRequest req, HttpServletResponse resp, int userId) throws Exception {
        var cart = new GetCartByUserIdDAO(getConnection(), userId).access().getOutputParam();

        if (cart == null) {
            int newCartId = new CreateCartForUserDAO(getConnection(), userId, "standard").access().getOutputParam();
            cart = new GetCartByUserIdDAO(getConnection(), newCartId).access().getOutputParam();
        }

        int cartId = cart.getCartId();
        List<Book> cartBooks = new GetBooksInCartDAO(getConnection(), cartId).access().getOutputParam();

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
        
        req.getSession().setAttribute("cart_final_price", totalPrice);

        req.getRequestDispatcher("/jsp/cart/viewCart.jsp").forward(req, resp);
    }

    private int extractBookIdFromPath(String path) {
        String[] segments = path.split("/");
        return Integer.parseInt(segments[segments.length - 1].trim());
    }

    private int getOrCreateCartId(int userId) throws Exception {
        var cart = new GetCartByUserIdDAO(getConnection(), userId).access().getOutputParam();
        if (cart == null) {
            return new CreateCartForUserDAO(getConnection(), userId, "standard").access().getOutputParam();
        }
        return cart.getCartId();
    }
}
