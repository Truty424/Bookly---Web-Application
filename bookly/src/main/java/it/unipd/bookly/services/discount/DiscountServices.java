package it.unipd.bookly.services.discount;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import it.unipd.bookly.Resource.Discount;
import it.unipd.bookly.utilities.ErrorCode;

public class DiscountServices {

    private static final Pattern DISCOUNT_CODE_PATTERN = Pattern.compile("^[A-Z0-9]{5,20}$");
    private static final int MIN_DISCOUNT_DAYS = 1;
    private static final int MAX_DISCOUNT_DAYS = 365;

    public static boolean validateDiscount(Discount discount, ErrorCode errorCode) {
        boolean flag = true;

        if (discount == null) {
            errorCode = ErrorCode.INVALID_DISCOUNT_OBJECT;
            flag = false;
        }

        if (!validateDiscountCode(discount.getCode(), errorCode)) {
            flag = false;
        }
        if (!validateDiscountPercentage(discount.getDiscountPercentage(), errorCode)) {
            flag = false;
        }
        if (!validateExpirationDate(discount.getExpiredDate(), errorCode)) {
            flag = false;
        }

        return flag;
    }

    public static boolean validateDiscountCode(String code, ErrorCode errorCode) {
        boolean flag = true;

        if (StringUtils.isBlank(code)) {
            errorCode = ErrorCode.INVALID_DISCOUNT_CODE;
            flag = false;
        } else if (!DISCOUNT_CODE_PATTERN.matcher(code).matches()) {
            errorCode = ErrorCode.INVALID_DISCOUNT_FORMAT;
            flag = false;
        }

        return flag;
    }

    public static boolean validateDiscountPercentage(double percentage, ErrorCode errorCode) {
        boolean flag = true;

        if (percentage <= 0) {
            errorCode = ErrorCode.DISCOUNT_TOO_LOW;
            flag = false;
        } else if (percentage > 100) {
            errorCode = ErrorCode.DISCOUNT_TOO_HIGH;
            flag = false;
        }

        return flag;
    }

    public static boolean validateExpirationDate(Timestamp expiration, ErrorCode errorCode) {
        boolean flag = true;

        if (expiration == null) {
            errorCode = ErrorCode.MISSING_EXPIRATION_DATE;
            flag = false;
        } else {
            Instant now = Instant.now();
            Instant minDate = now.plus(MIN_DISCOUNT_DAYS, ChronoUnit.DAYS);
            Instant maxDate = now.plus(MAX_DISCOUNT_DAYS, ChronoUnit.DAYS);

            if (expiration.before(Timestamp.from(now))) {
                errorCode = ErrorCode.EXPIRED_DISCOUNT;
                flag = false;
            } else if (expiration.before(Timestamp.from(minDate))) {
                errorCode = ErrorCode.DISCOUNT_PERIOD_TOO_SHORT;
                flag = false;
            } else if (expiration.after(Timestamp.from(maxDate))) {
                errorCode = ErrorCode.DISCOUNT_PERIOD_TOO_LONG;
                flag = false;
            }
        }

        return flag;
    }

    public static boolean validateForApplication(Discount discount, ErrorCode errorCode) {
        boolean flag = validateDiscount(discount, errorCode);

        if (flag && discount.getExpiredDate().before(Timestamp.from(Instant.now()))) {
            errorCode = ErrorCode.DISCOUNT_EXPIRED;
            flag = false;
        }

        return flag;
    }
}
