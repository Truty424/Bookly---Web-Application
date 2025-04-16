package it.unipd.bookly.Resource;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.ArrayList;

/**
 * Represents a shopping cart containing items, user details, and other metadata.
 */
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

    /**
     * Default constructor for Cart.
     */
    public Cart() {}

    /**
     * Constructs a Cart with all attributes.
     *
     * @param cartId         The ID of the cart.
     * @param userId         The ID of the user who owns the cart.
     * @param quantity       The total quantity of items in the cart.
     * @param shipmentMethod The shipment method for the cart.
     * @param discountId     The ID of the discount applied to the cart.
     * @param orderId        The ID of the order associated with the cart.
     * @param createdDate    The timestamp when the cart was created.
     * @param totalPrice     The total price of the cart.
     * @param items          The list of items in the cart.
     */
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

    /**
     * Constructs a Cart with minimal attributes.
     *
     * @param userId         The ID of the user who owns the cart.
     * @param shipmentMethod The shipment method for the cart.
     * @param totalPrice     The total price of the cart.
     */
    public Cart(int userId, String shipmentMethod, double totalPrice) {
        this.userId = userId;
        this.shipmentMethod = shipmentMethod;
        this.totalPrice = totalPrice;
        this.quantity = 0;
    }

    /**
     * Gets the cart ID.
     *
     * @return The cart ID.
     */
    public int getCartId() { return cartId; }

    /**
     * Gets the user ID.
     *
     * @return The user ID.
     */
    public int getUserId() { return userId; }

    /**
     * Gets the total quantity of items in the cart.
     *
     * @return The total quantity of items.
     */
    public int getQuantity() { return quantity; }

    /**
     * Gets the shipment method.
     *
     * @return The shipment method.
     */
    public String getShipmentMethod() { return shipmentMethod; }

    /**
     * Gets the discount ID.
     *
     * @return The discount ID, or null if no discount is applied.
     */
    public Integer getDiscountId() { return discountId; }

    /**
     * Gets the order ID.
     *
     * @return The order ID, or null if no order is associated.
     */
    public Integer getOrderId() { return orderId; }

    /**
     * Gets the total price of the cart.
     *
     * @return The total price.
     */
    public double getTotalPrice() { return totalPrice; }

    /**
     * Gets the creation timestamp of the cart.
     *
     * @return The creation timestamp.
     */
    public Timestamp getCreatedDate() { return createdDate; }

    /**
     * Gets the list of items in the cart.
     *
     * @return A defensive copy of the list of items.
     */
    public List<CartItem> getItems() { return new ArrayList<>(items); }

    /**
     * Sets the cart ID.
     *
     * @param cartId The cart ID.
     */
    public void setCartId(int cartId) { this.cartId = cartId; }

    /**
     * Sets the user ID.
     *
     * @param userId The user ID.
     */
    public void setUserId(int userId) { this.userId = userId; }

    /**
     * Sets the total quantity of items in the cart.
     *
     * @param quantity The total quantity. Must be non-negative.
     */
    public void setQuantity(int quantity) { 
        this.quantity = Math.max(0, quantity); // Prevent negative quantities
    }

    /**
     * Sets the shipment method.
     *
     * @param shipmentMethod The shipment method.
     */
    public void setShipmentMethod(String shipmentMethod) { this.shipmentMethod = shipmentMethod; }

    /**
     * Sets the discount ID.
     *
     * @param discountId The discount ID, or null if no discount is applied.
     */
    public void setDiscountId(Integer discountId) { this.discountId = discountId; }

    /**
     * Sets the order ID.
     *
     * @param orderId The order ID, or null if no order is associated.
     */
    public void setOrderId(Integer orderId) { this.orderId = orderId; }

    /**
     * Sets the creation timestamp of the cart.
     *
     * @param createdDate The creation timestamp.
     */
    public void setCreatedDate(Timestamp createdDate) { this.createdDate = createdDate; }

    /**
     * Sets the total price of the cart.
     *
     * @param totalPrice The total price. Must be non-negative.
     */
    public void setTotalPrice(double totalPrice) { 
        this.totalPrice = Math.max(0, totalPrice);
    }

    /**
     * Sets the list of items in the cart.
     *
     * @param items The list of items. If null, an empty list is used.
     */
    public void setItems(List<CartItem> items) {
        this.items = items != null ? new ArrayList<>(items) : new ArrayList<>();
        updateDerivedFields();
    }

    /**
     * Adds an item to the cart.
     *
     * @param item The item to add.
     */
    public void addItem(CartItem item) {
        if (item != null) {
            items.add(item);
            updateDerivedFields();
        }
    }

    /**
     * Removes an item from the cart by its book ID.
     *
     * @param bookId The ID of the book to remove.
     */
    public void removeItem(int bookId) {
        items.removeIf(item -> item.getBookId() == bookId);
        updateDerivedFields();
    }

    /**
     * Clears all items from the cart.
     */
    public void clearItems() {
        items.clear();
        updateDerivedFields();
    }

    /**
     * Updates derived fields such as quantity and total price based on the items in the cart.
     */
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