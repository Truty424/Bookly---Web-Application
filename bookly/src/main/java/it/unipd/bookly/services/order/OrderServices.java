package it.unipd.bookly.services.order;

import it.unipd.bookly.resource.Order;
import it.unipd.bookly.utilities.ErrorCode;

import java.sql.Timestamp;
import java.time.Instant;

public class OrderServices {

    /**
     * Validates an Order before placing it.
     */
    public static boolean validateOrder(Order order, ErrorCode errorCode) {
        return userValidation(order.getUserId(), errorCode)
            && cartValidation(order.getCartId(), errorCode)
            && paymentValidation(order.getPaymentMethod(), errorCode)
            && timestampValidation(order.getOrderDate(), errorCode);
    }

    public static boolean userValidation(int userId, ErrorCode errorCode) {
        if (userId <= 0) {
            errorCode.setErrorCode(ErrorCode.INVALID_USER_ID);
            return false;
        }
        return true;
    }

    public static boolean cartValidation(int cartId, ErrorCode errorCode) {
        if (cartId <= 0) {
            errorCode.setErrorCode(ErrorCode.INVALID_CART_ID);
            return false;
        }
        return true;
    }

    public static boolean paymentValidation(String method, ErrorCode errorCode) {
        if (method == null || method.trim().isEmpty()) {
            errorCode.setErrorCode(ErrorCode.INVALID_PAYMENT_METHOD);
            return false;
        }
        return true;
    }
}
