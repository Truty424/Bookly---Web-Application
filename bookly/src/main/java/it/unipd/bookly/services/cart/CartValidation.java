package it.unipd.bookly.services.discount;

import it.unipd.bookly.Resource.Discount;
import it.unipd.bookly.utilities.ErrorCode;
import org.apache.commons.lang3.StringUtils;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.regex.Pattern;

/**
 * Service for discount validation and business logic.
 */
public class DiscountServices {

    private static final Pattern DISCOUNT_CODE_PATTERN = 
        Pattern.compile("^[A-Z0-9]{5,20}$");
    private static final int MIN_DISCOUNT_DAYS = 1;
    private static final int MAX_DISCOUNT_DAYS = 365;

    /**
     * Validates all discount fields.
     * @return true if valid, false otherwise (sets errorCode)
     */
    public static boolean validateDiscount(Discount discount, ErrorCode errorCode) {
        if (discount == null) {
            errorCode.setCode(ErrorCode.INVALID_DISCOUNT_OBJECT.getCode());
            return false;
        }

        if (!validateDiscountCode(discount.getCode(), errorCode)) {
            return false;
        }

        if (!validateDiscountPercentage(discount.getDiscountPercentage(), errorCode)) {
            return false;
        }

        return validateExpirationDate(discount.getExpiredDate(), errorCode);
    }

    /**
     * Validates discount code format.
     * @return true if valid, false otherwise (sets errorCode)
     */
    public static boolean validateDiscountCode(String code, ErrorCode errorCode) {
        if (StringUtils.isBlank(code)) {
            errorCode.setCode(ErrorCode.INVALID_DISCOUNT_CODE.getCode());
            return false;
        }
        if (!DISCOUNT_CODE_PATTERN.matcher(code).matches()) {
            errorCode.setCode(ErrorCode.INVALID_DISCOUNT_FORMAT.getCode());
            return false;
        }
        return true;
    }

    /**
     * Validates discount percentage (0 < % <= 100).
     * @return true if valid, false otherwise (sets errorCode)
     */
    public static boolean validateDiscountPercentage(double percentage, ErrorCode errorCode) {
        if (percentage <= 0) {
            errorCode.setCode(ErrorCode.DISCOUNT_TOO_LOW.getCode());
            return false;
        }
        if (percentage > 100) {
            errorCode.setCode(ErrorCode.DISCOUNT_TOO_HIGH.getCode());
            return false;
        }
        return true;
    }

    /**
     * Validates expiration date is within 1-365 days from now.
     * @return true if valid, false otherwise (sets errorCode)
     */
    public static boolean validateExpirationDate(Timestamp expiration, ErrorCode errorCode) {
        if (expiration == null) {
            errorCode.setCode(ErrorCode.MISSING_EXPIRATION_DATE.getCode());
            return false;
        }

        Instant now = Instant.now();
        Instant minDate = now.plus(MIN_DISCOUNT_DAYS, ChronoUnit.DAYS);
        Instant maxDate = now.plus(MAX_DISCOUNT_DAYS, ChronoUnit.DAYS);

        if (expiration.before(Timestamp.from(now))) {
            errorCode.setCode(ErrorCode.EXPIRED_DISCOUNT.getCode());
            return false;
        }
        if (expiration.before(Timestamp.from(minDate))) {
            errorCode.setCode(ErrorCode.DISCOUNT_PERIOD_TOO_SHORT.getCode());
            return false;
        }
        if (expiration.after(Timestamp.from(maxDate))) {
            errorCode.setCode(ErrorCode.DISCOUNT_PERIOD_TOO_LONG.getCode());
            return false;
        }

        return true;
    }

    /**
     * Validates discount is applicable (valid and not expired).
     * @return true if applicable, false otherwise (sets errorCode)
     */
    public static boolean validateForApplication(Discount discount, ErrorCode errorCode) {
        if (!validateDiscount(discount, errorCode)) {
            return false;
        }

        if (discount.getExpiredDate().before(Timestamp.from(Instant.now()))) {
            errorCode.setCode(ErrorCode.DISCOUNT_EXPIRED.getCode());
            return false;
        }

        return true;
    }
}