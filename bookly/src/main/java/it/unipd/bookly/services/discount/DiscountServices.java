package it.unipd.bookly.services.discount;

import it.unipd.bookly.resource.Discount;
import it.unipd.bookly.utilities.ErrorCode;

import java.sql.Timestamp;
import java.time.Instant;

public class DiscountServices {

    /**
     * Validates a discount object before inserting it.
     */
    public static boolean validateNewDiscount(Discount discount, ErrorCode errorCode) {
        return codeValidation(discount.getCode(), errorCode) &&
               percentageValidation(discount.getDiscountPercentage(), errorCode) &&
               expirationValidation(discount.getExpiredDate(), errorCode);
    }

    /**
     * Validates discount code.
     */
    public static boolean codeValidation(String code, ErrorCode errorCode) {
        boolean flag = true;
        if (code == null || code.trim().isEmpty()) {
            errorCode.setErrorCode(ErrorCode.INVALID_DISCOUNT_CODE);
            flag = false;
        }
        return flag;
    }

    /**
     * Validates discount percentage.
     */
    public static boolean percentageValidation(double percentage, ErrorCode errorCode) {
        boolean flag = true;
        if (percentage <= 0 || percentage > 100) {
            errorCode.setErrorCode(ErrorCode.INVALID_DISCOUNT_PERCENTAGE);
            flag = false;
        }
        return flag;
    }

    /**
     * Validates that the expiration date is in the future.
     */
    public static boolean expirationValidation(Timestamp expirationDate, ErrorCode errorCode) {
        boolean flag = true;
        if (expirationDate == null || expirationDate.before(Timestamp.from(Instant.now()))) {
            errorCode.setErrorCode(ErrorCode.EXPIRED_DISCOUNT);
            flag = false;
        }
        return flag;
    }

    /**
     * Validates a code before applying to a cart.
     */
    public static boolean validateDiscountCodeUsage(String code, ErrorCode errorCode) {
        return codeValidation(code, errorCode);
    }
}
