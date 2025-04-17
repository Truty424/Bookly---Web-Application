package it.unipd.bookly.Resource;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.List;

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

    // ✅ New field: list of books associated with the order
    private List<Book> books;

    public Order() {
        // Default constructor
    }


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


    public Order(int orderId, double totalPrice, String paymentMethod, Timestamp orderDate, String address,
                 String shipmentCode, String status, List<Book> books) {
        this.orderId = orderId;
        this.totalPrice = totalPrice;
        this.paymentMethod = paymentMethod;
        this.orderDate = orderDate;
        this.address = address;
        this.shipmentCode = shipmentCode;
        this.status = status;
        this.books = books;
    }

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

    public List<Book> getBooks() {
        return books;
    }

    // --- Setters ---
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setOrderDate(Timestamp orderDate) {
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

    public void setBooks(List<Book> books) {
        this.books = books;
    }


}
