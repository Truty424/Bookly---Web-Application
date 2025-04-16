package it.unipd.bookly.Resource;

import java.sql.Timestamp;

/**
 * Represents a discount that can be applied to a purchase, including its ID, code,
 * percentage, and expiration date.
 */
public class Discount {

    private int discountId;
    private String code;
    private double discountPercentage;
    private Timestamp expiredDate;

    /**
     * Default constructor for Discount.
     */
    public Discount() {}

    /**
     * Constructs a Discount with all attributes.
     *
     * @param discountId         The ID of the discount.
     * @param code               The code of the discount.
     * @param discountPercentage The percentage of the discount.
     * @param expiredDate        The expiration date of the discount.
     */
    public Discount(int discountId, String code, double discountPercentage, Timestamp expiredDate) {
        this.discountId = discountId;
        this.code = code;
        this.discountPercentage = discountPercentage;
        this.expiredDate = expiredDate;
    }

    /**
     * Constructs a Discount without an ID.
     *
     * @param code               The code of the discount.
     * @param discountPercentage The percentage of the discount.
     * @param expiredDate        The expiration date of the discount.
     */
    public Discount(String code, double discountPercentage, Timestamp expiredDate) {
        this.code = code;
        this.discountPercentage = discountPercentage;
        this.expiredDate = expiredDate;
    }

    /**
     * Gets the ID of the discount.
     *
     * @return The discount ID.
     */
    public int getDiscountId() { return discountId; }

    /**
     * Gets the code of the discount.
     *
     * @return The discount code.
     */
    public String getCode() { return code; }

    /**
     * Gets the percentage of the discount.
     *
     * @return The discount percentage.
     */
    public double getDiscountPercentage() { return discountPercentage; }

    /**
     * Gets the expiration date of the discount.
     *
     * @return The expiration date.
     */
    public Timestamp getExpiredDate() { return expiredDate; }

    /**
     * Sets the ID of the discount.
     *
     * @param discountId The discount ID.
     */
    public void setDiscountId(int discountId) { this.discountId = discountId; }

    /**
     * Sets the code of the discount.
     *
     * @param code The discount code.
     */
    public void setCode(String code) { this.code = code; }

    /**
     * Sets the percentage of the discount.
     *
     * @param discountPercentage The discount percentage.
     */
    public void setDiscountPercentage(double discountPercentage) { this.discountPercentage = discountPercentage; }

    /**
     * Sets the expiration date of the discount.
     *
     * @param expiredDate The expiration date.
     */
    public void setExpiredDate(Timestamp expiredDate) { this.expiredDate = expiredDate; }

}
