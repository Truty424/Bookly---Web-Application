package it.unipd.bookly.Resource;

/**
 * Represents an item in a shopping cart, including details about the book,
 * quantity, and price.
 */
public class CartItem {

    private int book_id;
    private String title;
    private int quantity;
    private int unitPrice;

    /**
     * Default constructor for CartItem.
     */
    public CartItem() {
    }

    /**
     * Constructs a CartItem with the specified details.
     *
     * @param book_id   The ID of the book.
     * @param title     The title of the book.
     * @param quantity  The quantity of the book in the cart.
     * @param unitPrice The unit price of the book.
     */
    public CartItem(int book_id, String title, int quantity, int unitPrice) {
        this.book_id = book_id;
        this.title = title;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    /**
     * Gets the ID of the book.
     *
     * @return The book ID.
     */
    public int getBookId() {
        return book_id;
    }

    /**
     * Gets the title of the book.
     *
     * @return The book title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the quantity of the book in the cart.
     *
     * @return The quantity of the book.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Gets the unit price of the book.
     *
     * @return The unit price of the book.
     */
    public int getUnitPrice() {
        return unitPrice;
    }

    /**
     * Sets the ID of the book.
     *
     * @param book_id The book ID.
     */
    public void setbookId(int book_id) {
        this.book_id = book_id;
    }

    /**
     * Sets the title of the book.
     *
     * @param title The book title.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Sets the quantity of the book in the cart.
     *
     * @param quantity The quantity of the book. Must be non-negative.
     */
    public void setQuantity(int quantity) {
        this.quantity = Math.max(0, quantity);
    }

    /**
     * Sets the unit price of the book.
     *
     * @param unitPrice The unit price of the book. Must be non-negative.
     */
    public void setUnitPrice(int unitPrice) {
        this.unitPrice = Math.max(0, unitPrice);
    }
}
