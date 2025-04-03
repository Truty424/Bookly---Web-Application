package it.unipd.bookly.Resource;

import java.sql.Timestamp;

/**
 * Represents an order in the Bookly system.
 */
public class Order {

    private int orderId;
    private double totalPrice;
    private String paymentMethod;
    private Timestamp orderDate;
    private String address;
    private String shipmentCode;
    private String status;

    // Default constructor for frameworks and JSON mapping
    public Order() {
    }

    // Constructor with all fields (for retrieval)
    public Order(int orderId, double totalPrice, String paymentMethod, Timestamp orderDate, String address,
            String shipmentCode, String status) {
        this.orderId = orderId;
        this.totalPrice = totalPrice;
        this.paymentMethod = paymentMethod;
        this.orderDate = orderDate;
        this.address = address;
        this.shipmentCode = shipmentCode;
        this.status = status;
    }

    // Constructor without ID and orderDate (for creation)
    public Order(double totalPrice, String paymentMethod, String address, String shipmentCode, String status) {
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

    public Timestamp getOrderDate() {
        return orderDate;
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

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setorderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
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
