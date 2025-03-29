package it.unipd.bookly.resource;

import java.sql.Timestamp;
import java.util.Objects;

public class Discount {

    private int discountId;
    private String code;
    private double discountPercentage;
    private Timestamp expiredDate;

    // No-arg constructor (required for frameworks like Jackson or JPA)
    public Discount() {}

    // Full constructor
    public Discount(int discountId, String code, double discountPercentage, Timestamp expiredDate) {
        this.discountId = discountId;
        this.code = code;
        this.discountPercentage = discountPercentage;
        this.expiredDate = expiredDate;
    }

    // Insert constructor (without ID)
    public Discount(String code, double discountPercentage, Timestamp expiredDate) {
        this.code = code;
        this.discountPercentage = discountPercentage;
        this.expiredDate = expiredDate;
    }

    // Getters
    public int getDiscountId() { return discountId; }
    public String getCode() { return code; }
    public double getDiscountPercentage() { return discountPercentage; }
    public Timestamp getExpiredDate() { return expiredDate; }

    // Setters
    public void setDiscountId(int discountId) { this.discountId = discountId; }
    public void setCode(String code) { this.code = code; }
    public void setDiscountPercentage(double discountPercentage) { this.discountPercentage = discountPercentage; }
    public void setExpiredDate(Timestamp expiredDate) { this.expiredDate = expiredDate; }

    // equals and hashCode
    @Override
    public int hashCode() {
        return Objects.hash(discountId);
    }
}
