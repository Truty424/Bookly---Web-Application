package it.unipd.bookly.resource;

import java.sql.Timestamp;
import java.util.Objects;

/**
 * Represents an order in the Bookly system.
 */
public class Order {

    private int orderId;
    private BigDecimal totalPrice;
    private String paymentMethod;
    private Timestamp paymentDate;
    private String address;
    private String shipmentCode;
    private String status;

    // Default constructor for frameworks and JSON mapping
    public Order() {}

    // Constructor with all fields (for retrieval)
    public Order(int orderId, BigDecimal totalPrice, String paymentMethod, Timestamp paymentDate, String address,
                 String shipmentCode, String status) {
        this.orderId = orderId;
        this.totalPrice = totalPrice;
        this.paymentMethod = paymentMethod;
        this.paymentDate = paymentDate;
        this.address = address;
        this.shipmentCode = shipmentCode;
        this.status = status;
    }

    // Constructor without ID and paymentDate (for creation)
    public Order(BigDecimal totalPrice, String paymentMethod, String address, String shipmentCode, String status) {
        this.totalPrice = totalPrice;
        this.paymentMethod = paymentMethod;
        this.address = address;
        this.shipmentCode = shipmentCode;
        this.status = status;
    }

    // --- Getters ---
    public int getOrderId() {
        return orderId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public Timestamp getPaymentDate() {
        return paymentDate;
    }

    public String getAddress() {
        return address;
    }

    public String getShipmentCode() {
        return shipmentCode;
    }

    public String getStatus() {
        return status;
    }

    // --- Setters ---
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setPaymentDate(Timestamp paymentDate) {
        this.paymentDate = paymentDate;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setShipmentCode(String shipmentCode) {
        this.shipmentCode = shipmentCode;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
