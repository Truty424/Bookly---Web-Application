package it.unipd.bookly.utilities;

import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

/**
 * Enum for application error codes and their corresponding HTTP status.
 */
public enum ErrorCode {

    // --- General ---
    OK(0, HttpServletResponse.SC_OK, "Request successful."),
    CREATED(1, HttpServletResponse.SC_CREATED, "Resource created successfully."),
    ACCEPTED(2, HttpServletResponse.SC_ACCEPTED, "Request accepted but not yet processed."),
    NO_CONTENT(3, HttpServletResponse.SC_NO_CONTENT, "No content available."),
    INTERNAL_ERROR(-1, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error."),
    METHOD_NOT_ALLOWED(-2, HttpServletResponse.SC_METHOD_NOT_ALLOWED, "HTTP method not allowed."),
    BAD_REQUEST(-3, HttpServletResponse.SC_BAD_REQUEST, "Bad request."),
    UNAUTHORIZED(-4, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized access."),
    FORBIDDEN(-5, HttpServletResponse.SC_FORBIDDEN, "Forbidden action."),
    NOT_FOUND(-6, HttpServletResponse.SC_NOT_FOUND, "Resource not found."),
    CONFLICT(-7, HttpServletResponse.SC_CONFLICT, "Request conflict."),
    UNSUPPORTED_MEDIA_TYPE(-8, HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "Unsupported media type."),

    // --- User & Auth ---
    USER_NOT_FOUND(-100, HttpServletResponse.SC_NOT_FOUND, "User not found."),
    USERNAME_MISSING(-101, HttpServletResponse.SC_BAD_REQUEST, "Username is required."),
    FIRST_NAME_MISSING(-102, HttpServletResponse.SC_BAD_REQUEST, "First name is required."),
    LAST_NAME_MISSING(-103, HttpServletResponse.SC_BAD_REQUEST, "Last name is required."),
    EMAIL_MISSING(-104, HttpServletResponse.SC_BAD_REQUEST, "Email is required."),
    INVALID_EMAIL_FORMAT(-105, HttpServletResponse.SC_BAD_REQUEST, "Invalid email format."),
    PASSWORD_MISSING(-106, HttpServletResponse.SC_BAD_REQUEST, "Password is required."),
    WEAK_PASSWORD(-107, HttpServletResponse.SC_BAD_REQUEST, "Password is too weak."),
    DIFFERENT_PASSWORDS(-108, HttpServletResponse.SC_CONFLICT, "Passwords do not match."),
    USERNAME_OR_EMAIL_MISSING(-109, HttpServletResponse.SC_BAD_REQUEST, "Username or email is required."),
    INVALID_USERNAME_FORMAT(-110, HttpServletResponse.SC_BAD_REQUEST, "Invalid username format."),
    USER_ALREADY_EXISTS(-111, HttpServletResponse.SC_CONFLICT, "User already exists."),
    USER_REGISTRATION_INVALID(-112, HttpServletResponse.SC_BAD_REQUEST, "User registration input is invalid."),
    SESSION_EXPIRED(-113, HttpServletResponse.SC_UNAUTHORIZED, "Session expired or user not logged in."),
    INVALID_CREDENTIALS(-114, HttpServletResponse.SC_UNAUTHORIZED, "Invalid login credentials."),

    // --- Discount ---
    INVALID_DISCOUNT_OBJECT(-100, HttpServletResponse.SC_BAD_REQUEST, "Invalid discount object."),
    INVALID_DISCOUNT_CODE(-101, HttpServletResponse.SC_BAD_REQUEST, "Discount code is required."),
    INVALID_DISCOUNT_FORMAT(-102, HttpServletResponse.SC_BAD_REQUEST, "Discount code format is invalid."),
    DISCOUNT_TOO_LOW(-103, HttpServletResponse.SC_BAD_REQUEST, "Discount must be greater than 0%."),
    DISCOUNT_TOO_HIGH(-104, HttpServletResponse.SC_BAD_REQUEST, "Discount cannot exceed 100%."),
    MISSING_EXPIRATION_DATE(-105, HttpServletResponse.SC_BAD_REQUEST, "Discount expiration date is required."),
    EXPIRED_DISCOUNT(-106, HttpServletResponse.SC_BAD_REQUEST, "Discount has already expired."),
    DISCOUNT_PERIOD_TOO_SHORT(-107, HttpServletResponse.SC_BAD_REQUEST, "Discount must last at least 1 day."),
    DISCOUNT_PERIOD_TOO_LONG(-108, HttpServletResponse.SC_BAD_REQUEST, "Discount must not exceed 365 days."),
    DISCOUNT_EXPIRED(-109, HttpServletResponse.SC_BAD_REQUEST, "This discount is expired and cannot be used."),

    // --- Cart ---
    CART_NOT_FOUND(-200, HttpServletResponse.SC_NOT_FOUND, "Cart not found."),
    INVALID_CART_OPERATION(-201, HttpServletResponse.SC_BAD_REQUEST, "Cart operation failed."),
    CART_EMPTY(-202, HttpServletResponse.SC_BAD_REQUEST, "Cart is empty."),
    ITEM_ALREADY_IN_CART(-203, HttpServletResponse.SC_CONFLICT, "Item already in cart."),
    INVALID_CART_OBJECT(-204, HttpServletResponse.SC_BAD_REQUEST, "Cart object is null or invalid."),
    INVALID_USER_ID(-205, HttpServletResponse.SC_BAD_REQUEST, "User ID is invalid."),
    CART_LIMIT_EXCEEDED(-206, HttpServletResponse.SC_BAD_REQUEST, "Cart has too many items."),
    INVALID_ITEM_QUANTITY(-207, HttpServletResponse.SC_BAD_REQUEST, "Item quantity must be greater than 0."),
    INVALID_CART_TOTAL(-208, HttpServletResponse.SC_BAD_REQUEST, "Cart total must be greater than 0."),
    CART_TOTAL_TOO_HIGH(-209, HttpServletResponse.SC_BAD_REQUEST, "Cart total exceeds maximum allowed value."),
    MISSING_SHIPPING_METHOD(-210, HttpServletResponse.SC_BAD_REQUEST, "Shipping method is missing or empty."),

    // --- Order ---
    INVALID_ORDER_OBJECT(-305, HttpServletResponse.SC_BAD_REQUEST, "Invalid or null order object."),
    INVALID_ORDER_AMOUNT(-306, HttpServletResponse.SC_BAD_REQUEST, "Order total must be greater than 0."),
    MISSING_SHIPPING_ADDRESS(-307, HttpServletResponse.SC_BAD_REQUEST, "Shipping address is required."),
    INVALID_SHIPMENT_CODE(-308, HttpServletResponse.SC_BAD_REQUEST, "Shipment code format is invalid."),
    INVALID_ORDER_STATUS(-309, HttpServletResponse.SC_BAD_REQUEST, "Order status is invalid."),
    INVALID_ORDER_DATE(-310, HttpServletResponse.SC_BAD_REQUEST, "Order date cannot be in the future."),
    ORDER_ALREADY_SHIPPED(-311, HttpServletResponse.SC_CONFLICT, "Order has already been shipped or delivered and cannot be cancelled.");



    private final int code;
    private final int httpCode;
    private final String message;

    ErrorCode(int code, int httpCode, String message) {
        this.code = code;
        this.httpCode = httpCode;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public String getMessage() {
        return message;
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("message", message);
        return json;
    }
}
