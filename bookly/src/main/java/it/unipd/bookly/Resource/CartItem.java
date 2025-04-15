package it.unipd.bookly.Resource;

public class CartItem {

    private int book_id;
    private String title;
    private int quantity;
    private int unitPrice;

    public CartItem() {
    }

    public CartItem(int book_id, String title, int quantity, int unitPrice) {
        this.book_id = book_id;
        this.title = title;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    // Getters and setters
    public int getBookId() {
        return book_id;
    }

    public String getTitle() {
        return title;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public void setbook_id(int book_id) {
        this.book_id = book_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setQuantity(int quantity) {
        this.quantity = Math.max(0, quantity);
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = Math.max(0, unitPrice);
    }
}
