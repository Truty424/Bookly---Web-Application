package it.unipd.bookly.services.cart;

import it.unipd.bookly.Resource.Cart;
import it.unipd.bookly.Resource.CartItem;
import it.unipd.bookly.utilities.ErrorCode;
import java.util.List;

public class CartServices {
    private static final int MAX_ITEMS_PER_CART = 20;
    private static final double MAX_CART_TOTAL = 1000.00;

    /**
     * Validates all cart fields.
     * @return true if valid, false otherwise (sets errorCode)
     */
    public static boolean validateCart(Cart cart, ErrorCode errorCode) {
        if (cart == null) {
            errorCode.setCode(ErrorCode.INVALID_CART_OBJECT.getCode());
            return false;
        }

        if (!validateUserId(cart.getUserId(), errorCode)) {
            return false;
        }

        if (!validateItems(cart.getItems(), errorCode)) {
            return false;
        }

        return validateTotalAmount(cart.getTotalPrice(), errorCode);
    }

    /**
     * Validates user ID is positive.
     * @return true if valid, false otherwise (sets errorCode)
     */
    public static boolean validateUserId(int userId, ErrorCode errorCode) {
        if (userId <= 0) {
            errorCode.setCode(ErrorCode.INVALID_USER_ID.getCode());
            return false;
        }
        return true;
    }

    /**
     * Validates cart items.
     * @return true if valid, false otherwise (sets errorCode)
     */
    public static boolean validateItems(List<CartItem> items, ErrorCode errorCode) {
        if (items == null || items.isEmpty()) {
            errorCode.setCode(ErrorCode.EMPTY_CART.getCode());
            return false;
        }

        if (items.size() > MAX_ITEMS_PER_CART) {
            errorCode.setCode(ErrorCode.CART_LIMIT_EXCEEDED.getCode());
            return false;
        }

        for (CartItem item : items) {
            if (item.getQuantity() <= 0) {
                errorCode.setCode(ErrorCode.INVALID_ITEM_QUANTITY.getCode());
                return false;
            }
        }

        return true;
    }

    /**
     * Validates total amount is reasonable.
     * @return true if valid, false otherwise (sets errorCode)
     */
    public static boolean validateTotalAmount(double total, ErrorCode errorCode) {
        if (total <= 0) {
            errorCode.setCode(ErrorCode.INVALID_CART_TOTAL.getCode());
            return false;
        }

        if (total > MAX_CART_TOTAL) {
            errorCode.setCode(ErrorCode.CART_TOTAL_TOO_HIGH.getCode());
            return false;
        }

        return true;
    }

    /**
     * Validates cart is ready for checkout.
     * @return true if ready, false otherwise (sets errorCode)
     */
    public static boolean validateForCheckout(Cart cart, ErrorCode errorCode) {
        if (!validateCart(cart, errorCode)) {
            return false;
        }

        if (cart.getShipmentMethod() == null || cart.getShipmentMethod().isEmpty()) {
            errorCode.setCode(ErrorCode.MISSING_SHIPPING_METHOD.getCode());
            return false;
        }

        return true;
    }
}