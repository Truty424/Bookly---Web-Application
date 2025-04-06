package it.unipd.bookly.utilities;

import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

public enum ErrorCode {
    // --- General ---
    OK(200, HttpServletResponse.SC_OK, "Success."),
    CREATED(201, HttpServletResponse.SC_CREATED, "Resource created."),
    ACCEPTED(202, HttpServletResponse.SC_ACCEPTED, "Request accepted for processing."),
    NO_CONTENT(204, HttpServletResponse.SC_NO_CONTENT, "No content to return."),
    BAD_REQUEST(400, HttpServletResponse.SC_BAD_REQUEST, "Bad request."),
    UNAUTHORIZED(401, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized access."),
    FORBIDDEN(403, HttpServletResponse.SC_FORBIDDEN, "Forbidden."),
    NOT_FOUND(404, HttpServletResponse.SC_NOT_FOUND, "Resource not found."),
    METHOD_NOT_ALLOWED(405, HttpServletResponse.SC_METHOD_NOT_ALLOWED, "HTTP method not allowed."),
    CONFLICT(409, HttpServletResponse.SC_CONFLICT, "Conflict occurred."),
    UNSUPPORTED_MEDIA_TYPE(415, HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "Unsupported media type."),
    SERVER_ERROR(500, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error."),

    // --- User & Auth ---
    USERNAME_MISSING(40001, 400, "Username is required."),
    FIRST_NAME_MISSING(40002, 400, "First name is required."),
    LAST_NAME_MISSING(40003, 400, "Last name is required."),
    EMAIL_MISSING(40004, 400, "Email is required."),
    INVALID_EMAIL_FORMAT(40005, 400, "Invalid email format."),
    PASSWORD_MISSING(40006, 400, "Password is required."),
    WEAK_PASSWORD(40007, 400, "Password is too weak."),
    DIFFERENT_PASSWORDS(40008, 400, "Passwords do not match."),
    USERNAME_OR_EMAIL_MISSING(40009, 400, "Username or email is required."),
    INVALID_USERNAME_FORMAT(40010, 400, "Invalid username format."),
    USER_ALREADY_EXISTS(40901, 409, "User already exists."),
    USER_NOT_FOUND(40401, 404, "User not found."),
    USER_REGISTRATION_INVALID(40011, 400, "Invalid registration input."),
    SESSION_EXPIRED(40101, 401, "Session expired or not authenticated."),

    // --- Wishlist ---
    WISHLIST_NOT_FOUND(40402, 404, "Wishlist not found."),
    INVALID_WISHLIST_DATA(40012, 400, "Invalid wishlist data."),
    BOOK_ALREADY_IN_WISHLIST(40902, 409, "Book already in wishlist."),
    BOOK_NOT_FOUND_IN_WISHLIST(40403, 404, "Book not found in wishlist."),

    // --- Book ---
    BOOK_NOT_FOUND(40404, 404, "Book not found."),
    CATEGORY_NOT_FOUND(40405, 404, "Category not found."),
    AUTHOR_NOT_FOUND(40406, 404, "Author not found."),
    PUBLISHER_NOT_FOUND(40407, 404, "Publisher not found."),

    // --- Review ---
    REVIEW_NOT_FOUND(40408, 404, "Review not found."),
    INVALID_REVIEW_DATA(40013, 400, "Invalid review content."),
    USER_NOT_AUTHORIZED_FOR_REVIEW(40301, 403, "Not authorized to review this item."),
    DUPLICATE_REVIEW(40903, 409, "User has already reviewed this book."),

    // --- Discount ---
    DISCOUNT_NOT_FOUND(40409, 404, "Discount not found."),
    DISCOUNT_EXPIRED(40014, 400, "Discount code is expired."),
    DISCOUNT_INVALID(40015, 400, "Invalid discount code."),

    // --- Cart ---
    CART_NOT_FOUND(40410, 404, "Cart not found."),
    INVALID_CART_OPERATION(40016, 400, "Cart update failed."),
    CART_EMPTY(40017, 400, "Cart is empty."),
    ITEM_ALREADY_IN_CART(40904, 409, "Item already in cart."),

    // --- Order ---
    ORDER_NOT_FOUND(40411, 404, "Order not found."),
    PAYMENT_FAILED(50001, 500, "Payment could not be processed."),
    ORDER_CREATION_FAILED(50002, 500, "Failed to create order."),
    ORDER_ALREADY_PAID(40905, 409, "Order has already been paid."),
    INVALID_ORDER_UPDATE(40018, 400, "Invalid order update attempt."),

    // --- Images ---
    INVALID_IMAGE_FORMAT(41501, 415, "Image format not supported."),
    IMAGE_TOO_LARGE(41301, 413, "Image size exceeds allowed limit."),

    // --- File Upload ---
    FILE_UPLOAD_FAILED(50003, 500, "File upload failed."),
    FILE_NOT_FOUND(40412, 404, "File not found."),
    INVALID_FILE_TYPE(40019, 400, "Invalid file type."),
    MISSING_FILE(40020, 400, "Missing file in request.");

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
