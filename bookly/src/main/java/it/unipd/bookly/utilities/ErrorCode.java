package it.unipd.bookly.utilities;

import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

public enum ErrorCode {
    OK(200, HttpServletResponse.SC_OK, "OK."),
    BAD_REQUEST(400, HttpServletResponse.SC_BAD_REQUEST, "Required fields (e.g., username, email, password) are missing or invalid."),
    CONFLICT(409, HttpServletResponse.SC_CONFLICT, "A user with the same email or username already exists."),
    UNAUTHORIZED(401, HttpServletResponse.SC_UNAUTHORIZED, "Login credentials are incorrect."),
    LOGIN_PAYLOAD_INCOMPLETE(400, HttpServletResponse.SC_BAD_REQUEST, "The login payload is incomplete."),
    USER_NOT_FOUND(404, HttpServletResponse.SC_NOT_FOUND, "User with the specified user_id does not exist."),
    WISHLIST_NOT_FOUND(404, HttpServletResponse.SC_NOT_FOUND, "The specified user_id does not have a wishlist."),
    USER_NOT_AUTHENTICATED(401, HttpServletResponse.SC_UNAUTHORIZED, "The request is made by a user who is not authenticated."),
    INVALID_WISHLIST_DATA(400, HttpServletResponse.SC_BAD_REQUEST, "Wishlist data provided is invalid or incomplete."),
    USER_NOT_LOGGED_IN(401, HttpServletResponse.SC_UNAUTHORIZED, "The user is not logged in."),
    BOOK_NOT_FOUND(404, HttpServletResponse.SC_NOT_FOUND, "The requested book does not exist."),
    INVALID_REVIEW_DATA(400, HttpServletResponse.SC_BAD_REQUEST, "The review data is incomplete or invalid."),
    USER_NOT_LOGGED_IN_FOR_REVIEW(401, HttpServletResponse.SC_UNAUTHORIZED, "The user is not logged in to write a review."),
    CATEGORY_OR_PUBLISHER_NOT_FOUND(404, HttpServletResponse.SC_NOT_FOUND, "The specified category or publisher does not exist."),
    AUTHOR_NOT_FOUND(404, HttpServletResponse.SC_NOT_FOUND, "The author of the book does not exist."),
    CONFLICT_ADDING_BOOK_OR_WISHLIST_ITEM(409, HttpServletResponse.SC_CONFLICT, "A conflict occurred when trying to add a book or wishlist item."),
    INTERNAL_SERVER_ERROR(500, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred on the server."),
    FORBIDDEN(403, HttpServletResponse.SC_FORBIDDEN, "The user is not authorized to perform this action."),
    UNPROCESSABLE_ENTITY(422, HttpServletResponse.SC_UNPROCESSABLE_ENTITY, "The data provided is valid but cannot be processed (e.g., invalid book details or ratings)."),
    MALFORMED_REQUEST_BODY(400, HttpServletResponse.SC_BAD_REQUEST, "The request body is malformed or contains invalid data (e.g., invalid email format or missing required fields)."),
    SESSION_EXPIRED(401, HttpServletResponse.SC_UNAUTHORIZED, "User is not logged in or the session has expired."),
    PERMISSION_DENIED(403, HttpServletResponse.SC_FORBIDDEN, "The user does not have permission to access the requested resource."),
    RESOURCE_NOT_FOUND(404, HttpServletResponse.SC_NOT_FOUND, "The requested resource (e.g., book, user, wishlist) does not exist."),
    METHOD_NOT_ALLOWED(405, HttpServletResponse.SC_METHOD_NOT_ALLOWED, "The HTTP method used is not allowed for the requested resource."),
    NOT_ACCEPTABLE(406, HttpServletResponse.SC_NOT_ACCEPTABLE, "The requested resource is not available in a format acceptable by the client."),
    REQUEST_TIMEOUT(408, HttpServletResponse.SC_REQUEST_TIMEOUT, "The server timed out waiting for the request."),
    CONFLICT_IN_REQUEST(409, HttpServletResponse.SC_CONFLICT, "A conflict occurred during a request."),
    UNPROCESSABLE_ENTITY_ENTITY(422, HttpServletResponse.SC_UNPROCESSABLE_ENTITY, "The entity provided in the request is syntactically correct but semantically incorrect or incomplete."),
    TOO_MANY_REQUESTS(429, HttpServletResponse.SC_, "The user has made too many requests in a short amount of time."),
    SERVER_ERROR(500, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred on the server."),
    BAD_GATEWAY(502, HttpServletResponse.SC_BAD_GATEWAY, "The server received an invalid response from an upstream server."),
    SERVICE_UNAVAILABLE(503, HttpServletResponse.SC_SERVICE_UNAVAILABLE, "The server is currently unable to handle the request due to temporary overload or maintenance."),
    GATEWAY_TIMEOUT(504, HttpServletResponse.SC_GATEWAY_TIMEOUT, "The server did not receive a timely response from an upstream server.");

    private final int errorCode;
    private final int httpCode;
    private final String errorMessage;

    ErrorCode(int errorCode, int httpCode, String errorMessage) {
        this.errorCode = errorCode;
        this.httpCode = httpCode;
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public int getHTTPCode() {
        return httpCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public JSONObject toJSON() {
        JSONObject data = new JSONObject();
        data.put("code", errorCode);
        data.put("message", errorMessage);
        JSONObject info = new JSONObject();
        info.put("error", data);
        return info;
    }
}
