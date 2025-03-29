package it.unipd.bookly.resource;

import java.sql.Timestamp;
import java.util.Objects;

/**
 * Represents an order in the Bookly system.
 */
public class Order {

    private int orderId;
    private double totalAmount;
    private String paymentMethod;
    private Timestamp paymentDate;
    private String address;
    private String shipmentCode;
    private String status;

    // Default constructor for frameworks and JSON mapping
    public Order() {}

    // Constructor with all fields (for retrieval)
    public Order(int orderId, double totalAmount, String paymentMethod, Timestamp paymentDate, String address,
                 String shipmentCode, String status) {
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.paymentDate = paymentDate;
        this.address = address;
        this.shipmentCode = shipmentCode;
        this.status = status;
    }

    // Constructor without ID and paymentDate (for creation)
    public Order(double totalAmount, String paymentMethod, String address, String shipmentCode, String status) {
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.address = address;
        this.shipmentCode = shipmentCode;
        this.status = status;
    }

    // --- Getters ---
    public int getOrderId() {
        return orderId;
    }

    public double getTotalAmount() {
        return totalAmount;
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

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
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

    // --- Utility Methods ---
    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", totalAmount=" + totalAmount +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", paymentDate=" + paymentDate +
                ", address='" + address + '\'' +
                ", shipmentCode='" + shipmentCode + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order order)) return false;
        return orderId == order.orderId &&
                Double.compare(order.totalAmount, totalAmount) == 0 &&
                Objects.equals(paymentMethod, order.paymentMethod) &&
                Objects.equals(paymentDate, order.paymentDate) &&
                Objects.equals(address, order.address) &&
                Objects.equals(shipmentCode, order.shipmentCode) &&
                Objects.equals(status, order.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId);
    }
}
