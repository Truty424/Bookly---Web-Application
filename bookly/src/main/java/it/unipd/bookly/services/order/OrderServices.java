package it.unipd.bookly.services.order;

import it.unipd.bookly.Resource.Order;
import it.unipd.bookly.utilities.ErrorCode;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

public class OrderServices {

    private static final List<String> VALID_STATUSES = Arrays.asList(
            "pending", "processing", "shipped", "delivered", "cancelled"
    );

    private static final String SHIPMENT_CODE_PATTERN = "^[A-Z]{3,5}-\\d{4,6}$";

    public static boolean validateOrder(Order order, ErrorCode errorCode) {
        boolean flag = true;

        if (order == null) {
            errorCode = ErrorCode.INVALID_ORDER_OBJECT;
            flag = false;
        } else {
            if (order.getOrderId() <= 0) {
                errorCode = ErrorCode.INVALID_USER_ID;
                flag = false;
            }

            if (order.getTotalPrice() <= 0) {
                errorCode = ErrorCode.INVALID_ORDER_AMOUNT;
                flag = false;
            }

            if (order.getAddress() == null || order.getAddress().trim().isEmpty()) {
                errorCode = ErrorCode.MISSING_SHIPPING_ADDRESS;
                flag = false;
            }

            if (order.getShipmentCode() != null &&
                    !order.getShipmentCode().matches(SHIPMENT_CODE_PATTERN)) {
                errorCode = ErrorCode.INVALID_SHIPMENT_CODE;
                flag = false;
            }

            if (order.getStatus() == null ||
                    !VALID_STATUSES.contains(order.getStatus().toLowerCase())) {
                errorCode = ErrorCode.INVALID_ORDER_STATUS;
                flag = false;
            }

            if (order.getOrderDate() != null &&
                    order.getOrderDate().after(Timestamp.from(Instant.now()))) {
                errorCode = ErrorCode.INVALID_ORDER_DATE;
                flag = false;
            }
        }

        return flag;
    }

    public static boolean validateCancellation(Order order, ErrorCode errorCode) {
        boolean flag = validateOrder(order, errorCode);

        if (flag && List.of("shipped", "delivered").contains(order.getStatus().toLowerCase())) {
            errorCode = ErrorCode.ORDER_ALREADY_SHIPPED;
            flag = false;
        }

        return flag;
    }
}
