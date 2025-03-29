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
public class DiscountServices {  // Changed to instance-based

    private static final Pattern DISCOUNT_CODE_PATTERN = 
        Pattern.compile("^[A-Z0-9]{5,20}$");
    private static final int MIN_DISCOUNT_DAYS = 1;
    private static final int MAX_DISCOUNT_DAYS = 365;

    /**
     * Validates a discount for creation.
     * @return ErrorCode indicating validation result
     */
    public ErrorCode validateDiscount(Discount discount) {
        if (discount == null) {
            return ErrorCode.INVALID_DISCOUNT_OBJECT;
        }

        ErrorCode codeValidation = validateDiscountCode(discount.getCode());
        if (codeValidation != ErrorCode.SUCCESS) {
            return codeValidation;
        }

        ErrorCode percentageValidation = validateDiscountPercentage(discount.getDiscountPercentage());
        if (percentageValidation != ErrorCode.SUCCESS) {
            return percentageValidation;
        }

        return validateExpirationDate(discount.getExpiredDate());
    }

    /**
     * Validates discount code format.
     */
    public ErrorCode validateDiscountCode(String code) {
        if (StringUtils.isBlank(code)) {
            return ErrorCode.INVALID_DISCOUNT_CODE;
        }
        if (!DISCOUNT_CODE_PATTERN.matcher(code).matches()) {
            return ErrorCode.INVALID_DISCOUNT_FORMAT;
        }
        return ErrorCode.SUCCESS;
    }

    /**
     * Validates discount percentage.
     */
    public ErrorCode validateDiscountPercentage(double percentage) {
        if (percentage <= 0) {
            return ErrorCode.DISCOUNT_TOO_LOW;
        }
        if (percentage > 100) {
            return ErrorCode.DISCOUNT_TOO_HIGH;
        }
        return ErrorCode.SUCCESS;
    }

    /**
     * Validates expiration date is within allowed range.
     */
    public ErrorCode validateExpirationDate(Timestamp expiration) {
        if (expiration == null) {
            return ErrorCode.MISSING_EXPIRATION_DATE;
        }

        Instant now = Instant.now();
        Instant minDate = now.plus(MIN_DISCOUNT_DAYS, ChronoUnit.DAYS);
        Instant maxDate = now.plus(MAX_DISCOUNT_DAYS, ChronoUnit.DAYS);

        if (expiration.before(Timestamp.from(now))) {
            return ErrorCode.EXPIRED_DISCOUNT;
        }
        if (expiration.before(Timestamp.from(minDate))) {
            return ErrorCode.DISCOUNT_PERIOD_TOO_SHORT;
        }
        if (expiration.after(Timestamp.from(maxDate))) {
            return ErrorCode.DISCOUNT_PERIOD_TOO_LONG;
        }

        return ErrorCode.SUCCESS;
    }

    /**
     * Validates discount for application to a cart.
     */
    public ErrorCode validateForApplication(Discount discount) {
        ErrorCode basicValidation = validateDiscount(discount);
        if (basicValidation != ErrorCode.SUCCESS) {
            return basicValidation;
        }

        if (discount.getExpiredDate().before(Timestamp.from(Instant.now()))) {
            return ErrorCode.DISCOUNT_EXPIRED;
        }

        return ErrorCode.SUCCESS;
    }
}