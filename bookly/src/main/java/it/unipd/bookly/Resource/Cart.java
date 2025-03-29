package it.unipd.bookly.Resource;

import java.sql.Timestamp;
import java.util.Objects;

public class Cart {
    private int cartId;
    private int userId;
    private int quantity;
    private int totalPrice;
    private String shipmentMethod;
    private Integer discountId;
    private Integer orderId;
    private Timestamp createdDate;

    // Default constructor
    public Cart() {}

    // Full constructor
    public Cart(int cartId, int userId, int quantity, String shipmentMethod,
                Integer discountId, Integer orderId, Timestamp createdDate,int totalPrice) {
        this.cartId = cartId;
        this.userId = userId;
        this.quantity = quantity;
        this.shipmentMethod = shipmentMethod;
        this.discountId = discountId;
        this.orderId = orderId;
        this.createdDate = createdDate;
        this.totalPrice = totalPrice;
    }

    // Minimal constructor (e.g., for inserts)
    public Cart(int userId, String shipmentMethod ,int totalPrice) {
        this.userId = userId;
        this.shipmentMethod = shipmentMethod;
        this.quantity = 0;
        this.totalPrice = totalPrice;
    }

    // Getters
    public int getCartId() { return cartId; }
    public int getUserId() { return userId; }
    public int getQuantity() { return quantity; }
    public String getShipmentMethod() { return shipmentMethod; }
    public Integer getDiscountId() { return discountId; }
    public Integer getOrderId() { return orderId; }
    public Integer getTotalPrice() { return totalPrice; }
    public Timestamp getCreatedDate() { return createdDate; }

    // Setters
    public void setCartId(int cartId) { this.cartId = cartId; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setShipmentMethod(String shipmentMethod) { this.shipmentMethod = shipmentMethod; }
    public void setDiscountId(Integer discountId) { this.discountId = discountId; }
    public void setOrderId(Integer orderId) { this.orderId = orderId; }
    public void setCreatedDate(Timestamp createdDate) { this.createdDate = createdDate; }
    public void setTotalPrice(int totalPrice) { this.totalPrice = totalPrice; }


    @Override
    public int hashCode() {
        return Objects.hash(cartId);
    }
}
