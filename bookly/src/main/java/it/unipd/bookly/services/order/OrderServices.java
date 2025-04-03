package it.unipd.bookly.services.order;

import it.unipd.bookly.Resource.Order;
import it.unipd.bookly.utilities.ErrorCode;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

public class OrderServices {

    // Valid order statuses from your business logic
    private static final List<String> VALID_STATUSES = Arrays.asList(
            "pending", "processing", "shipped", "delivered", "cancelled"
    );

    // Valid shipment code pattern (e.g., "SHIP-12345")
    private static final String SHIPMENT_CODE_PATTERN = "^[A-Z]{3,5}-\\d{4,6}$";

    /**
     * Validates all order fields including shipment code and status
     */
    public static boolean validateOrder(Order order, ErrorCode errorCode) {
        if (order == null) {
            errorCode.setCode(ErrorCode.INVALID_ORDER_OBJECT.getCode());
            return false;
        }

        // Required fields validation
        if (order.getOrderId() <= 0) {
            errorCode.setCode(ErrorCode.INVALID_USER_ID.getCode());
            return false;
        }

        if (order.getTotalPrice() <= 0) {
            errorCode.setCode(ErrorCode.INVALID_ORDER_AMOUNT.getCode());
            return false;
        }

        if (order.getAddress() == null || order.getAddress().trim().isEmpty()) {
            errorCode.setCode(ErrorCode.MISSING_SHIPPING_ADDRESS.getCode());
            return false;
        }

        // Shipment code validation (optional but must match pattern if present)
        if (order.getShipmentCode() != null
                && !order.getShipmentCode().matches(SHIPMENT_CODE_PATTERN)) {
            errorCode.setCode(ErrorCode.INVALID_SHIPMENT_CODE.getCode());
            return false;
        }

        // Status validation
        if (order.getStatus() == null
                || !VALID_STATUSES.contains(order.getStatus().toLowerCase())) {
            errorCode.setCode(ErrorCode.INVALID_ORDER_STATUS.getCode());
            return false;
        }

        // Date validation (cannot be in future)
        if (order.getOrderDate() != null
                && order.getOrderDate().after(Timestamp.from(Instant.now()))) {
            errorCode.setCode(ErrorCode.INVALID_ORDER_DATE.getCode());
            return false;
        }

        return true;
    }

    /**
     * Special validation for order cancellation
     */
    public static boolean validateCancellation(Order order, ErrorCode errorCode) {
        if (!validateOrder(order, errorCode)) {
            return false;
        }

        // Cannot cancel already shipped/delivered orders
        if (List.of("shipped", "delivered").contains(order.getStatus().toLowerCase())) {
            errorCode.setCode(ErrorCode.ORDER_ALREADY_SHIPPED.getCode());
            return false;
        }

        return true;
    }
}
