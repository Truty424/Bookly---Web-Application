package it.unipd.bookly.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Represents an order in the Bookly system.
 * An order includes details such as the order ID, total price, payment method,
 * order date, address, shipment code, and status.
 */
public class Order {

    private int orderId;
    private double totalPrice;
    private String paymentMethod;
    private Timestamp orderDate;
    private String address;
    private String shipmentCode;
    private String status;

    /**
     * Default constructor.
     */
    public Order() {
    }

    /**
     * Constructs an Order with all attributes.
     *
     * @param orderId       The ID of the order.
     * @param totalPrice    The total price of the order.
     * @param paymentMethod The payment method used for the order.
     * @param orderDate     The date and time when the order was placed.
     * @param address       The address associated with the order.
     * @param shipmentCode  The shipment code for tracking the order.
     * @param status        The status of the order (e.g., "Pending", "Shipped").
     */
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

    /**
     * Constructs an Order without ID and order date (for creation).
     *
     * @param totalPrice    The total price of the order.
     * @param paymentMethod The payment method used for the order.
     * @param address       The address associated with the order.
     * @param shipmentCode  The shipment code for tracking the order.
     * @param status        The status of the order (e.g., "Pending", "Shipped").
     */
    public Order(double totalPrice, String paymentMethod, String address, String shipmentCode, String status) {
        this.totalPrice = totalPrice;
        this.paymentMethod = paymentMethod;
        this.address = address;
        this.shipmentCode = shipmentCode;
        this.status = status;
    }

    /**
     * Gets the ID of the order.
     *
     * @return The order ID.
     */
    public int getOrderId() {
        return orderId;
    }

    /**
     * Gets the total price of the order.
     *
     * @return The total price.
     */
    public double getTotalPrice() {
        return totalPrice;
    }

    /**
     * Gets the payment method used for the order.
     *
     * @return The payment method.
     */
    public String getPaymentMethod() {
        return paymentMethod;
    }

    /**
     * Gets the date and time when the order was placed.
     *
     * @return The order date.
     */
    public Timestamp getOrderDate() {
        return orderDate;
    }

    /**
     * Gets the address associated with the order.
     *
     * @return The address.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Gets the shipment code for tracking the order.
     *
     * @return The shipment code.
     */
    public String getShipmentCode() {
        return shipmentCode;
    }

    /**
     * Gets the status of the order.
     *
     * @return The order status.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the ID of the order.
     *
     * @param orderId The order ID.
     */
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    /**
     * Sets the total price of the order.
     *
     * @param totalPrice The total price.
     */
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     * Sets the payment method used for the order.
     *
     * @param paymentMethod The payment method.
     */
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    /**
     * Sets the date and time when the order was placed.
     *
     * @param orderDate The order date.
     */
    public void setorderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    /**
     * Sets the address associated with the order.
     *
     * @param address The address.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Sets the shipment code for tracking the order.
     *
     * @param shipmentCode The shipment code.
     */
    public void setShipmentCode(String shipmentCode) {
        this.shipmentCode = shipmentCode;
    }

    /**
     * Sets the status of the order.
     *
     * @param status The order status.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Serializes the order to JSON and writes it to the provided output stream.
     *
     * @param out The output stream to which the JSON representation of the order is written.
     */
    @SuppressWarnings("CallToPrintStackTrace")
    public void toJSON(OutputStream out) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(out, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
