package it.unipd.bookly.resource;

import java.sql.Timestamp;
import java.util.Objects;

public class Cart {
    private int cartId;
    private int userId;
    private int quantity;
    private String shipmentMethod;
    private Integer discountId;
    private Integer orderId;
    private Timestamp createdDate;

    // Default constructor
    public Cart() {}

    // Full constructor
    public Cart(int cartId, int userId, int quantity, String shipmentMethod,
                Integer discountId, Integer orderId, Timestamp createdDate) {
        this.cartId = cartId;
        this.userId = userId;
        this.quantity = quantity;
        this.shipmentMethod = shipmentMethod;
        this.discountId = discountId;
        this.orderId = orderId;
        this.createdDate = createdDate;
    }

    // Minimal constructor (e.g., for inserts)
    public Cart(int userId, String shipmentMethod) {
        this.userId = userId;
        this.shipmentMethod = shipmentMethod;
        this.quantity = 0;
    }

    // Getters
    public int getCartId() { return cartId; }
    public int getUserId() { return userId; }
    public int getQuantity() { return quantity; }
    public String getShipmentMethod() { return shipmentMethod; }
    public Integer getDiscountId() { return discountId; }
    public Integer getOrderId() { return orderId; }
    public Timestamp getCreatedDate() { return createdDate; }

    // Setters
    public void setCartId(int cartId) { this.cartId = cartId; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setShipmentMethod(String shipmentMethod) { this.shipmentMethod = shipmentMethod; }
    public void setDiscountId(Integer discountId) { this.discountId = discountId; }
    public void setOrderId(Integer orderId) { this.orderId = orderId; }
    public void setCreatedDate(Timestamp createdDate) { this.createdDate = createdDate; }

    @Override
    public String toString() {
        return "Cart{" +
               "cartId=" + cartId +
               ", userId=" + userId +
               ", quantity=" + quantity +
               ", shipmentMethod='" + shipmentMethod + '\'' +
               ", discountId=" + discountId +
               ", orderId=" + orderId +
               ", createdDate=" + createdDate +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cart)) return false;
        Cart that = (Cart) o;
        return cartId == that.cartId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cartId);
    }
}
