package it.unipd.bookly.Resource;

public class CartItem {
    private int bookId;
    private String title;
    private int quantity;
    private int unitPrice;

    public CartItem() {}

    public CartItem(int bookId, String title, int quantity, int unitPrice) {
        this.bookId = bookId;
        this.title = title;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    // Getters and setters
    public int getBookId() { return bookId; }
    public String getTitle() { return title; }
    public int getQuantity() { return quantity; }
    public int getUnitPrice() { return unitPrice; }

    public void setBookId(int bookId) { this.bookId = bookId; }
    public void setTitle(String title) { this.title = title; }
    public void setQuantity(int quantity) { 
        this.quantity = Math.max(0, quantity); 
    }
    public void setUnitPrice(int unitPrice) { 
        this.unitPrice = Math.max(0, unitPrice); 
    }
}