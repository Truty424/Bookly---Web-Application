package it.unipd.bookly.Resource;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.ArrayList;

public class Cart {
    private int cartId;
    private int userId;
    private int quantity;
    private double totalPrice;
    private String shipmentMethod;
    private Integer discountId;
    private Integer orderId;
    private Timestamp createdDate;
    private List<CartItem> items = new ArrayList<>(); // New field for cart items

    // Constructors
    public Cart() {}

    public Cart(int cartId, int userId, int quantity, String shipmentMethod,
               Integer discountId, Integer orderId, Timestamp createdDate, 
               double totalPrice, List<CartItem> items) {
        this.cartId = cartId;
        this.userId = userId;
        this.quantity = quantity;
        this.shipmentMethod = shipmentMethod;
        this.discountId = discountId;
        this.orderId = orderId;
        this.createdDate = createdDate;
        this.totalPrice = totalPrice;
        this.items = items != null ? items : new ArrayList<>();
    }

    public Cart(int userId, String shipmentMethod, double totalPrice) {
        this.userId = userId;
        this.shipmentMethod = shipmentMethod;
        this.totalPrice = totalPrice;
        this.quantity = 0;
    }

    // Getters
    public int getCartId() { return cartId; }
    public int getUserId() { return userId; }
    public int getQuantity() { return quantity; }
    public String getShipmentMethod() { return shipmentMethod; }
    public Integer getDiscountId() { return discountId; }
    public Integer getOrderId() { return orderId; }
    public double getTotalPrice() { return totalPrice; }
    public Timestamp getCreatedDate() { return createdDate; }
    public List<CartItem> getItems() { return new ArrayList<>(items); } // Defensive copy

    // Setters
    public void setCartId(int cartId) { this.cartId = cartId; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setQuantity(int quantity) { 
        this.quantity = Math.max(0, quantity); // Prevent negative quantities
    }
    public void setShipmentMethod(String shipmentMethod) { this.shipmentMethod = shipmentMethod; }
    public void setDiscountId(Integer discountId) { this.discountId = discountId; }
    public void setOrderId(Integer orderId) { this.orderId = orderId; }
    public void setCreatedDate(Timestamp createdDate) { this.createdDate = createdDate; }
    public void setTotalPrice(double totalPrice) { 
        this.totalPrice = Math.max(0, totalPrice); // Prevent negative totals
    }
    public void setItems(List<CartItem> items) {
        this.items = items != null ? new ArrayList<>(items) : new ArrayList<>();
        updateDerivedFields();
    }

    // Item management methods
    public void addItem(CartItem item) {
        if (item != null) {
            items.add(item);
            updateDerivedFields();
        }
    }

    public void removeItem(int bookId) {
        items.removeIf(item -> item.getBookId() == bookId);
        updateDerivedFields();
    }

    public void clearItems() {
        items.clear();
        updateDerivedFields();
    }

    private void updateDerivedFields() {
        this.quantity = items.stream()
                           .mapToInt(CartItem::getQuantity)
                           .sum();
        this.totalPrice = items.stream()
                             .mapToInt(item -> item.getQuantity() * item.getUnitPrice())
                             .sum();
    }

    @Override
    public int hashCode() {
        return Objects.hash(cartId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Cart other = (Cart) obj;
        return cartId == other.cartId;
    }
}