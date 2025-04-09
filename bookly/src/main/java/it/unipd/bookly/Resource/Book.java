package it.unipd.bookly.Resource;

import it.unipd.bookly.Resource.Image;

public class Book {

    private int book_id;
    private String title;
    private String language;
    private String isbn;
    private double price;
    private String edition;
    private int publication_year;
    private int number_of_pages;
    private int stock_quantity;
    private double average_rate;
    private String summary;
    private Image book_pic;


    public Book() {
    }
    
    public Book(int book_id, String title, String language, String isbn, double price, String edition,
            int publication_year, int number_of_pages, int stock_quantity, double average_rate, String summary) {
        this.book_id = book_id;
        this.title = title;
        this.language = language;
        this.isbn = isbn;
        this.price = price;
        this.edition = edition;
        this.publication_year = publication_year;
        this.number_of_pages = number_of_pages;
        this.stock_quantity = stock_quantity;
        this.average_rate = average_rate;
        this.summary = summary;
    }

    public Book(String title, String language, String isbn, double price, String edition,
            int publication_year, int number_of_pages, int stock_quantity, double average_rate, String summary, Image book_pic) {
        this.title = title;
        this.language = language;
        this.isbn = isbn;
        this.price = price;
        this.edition = edition;
        this.publication_year = publication_year;
        this.number_of_pages = number_of_pages;
        this.stock_quantity = stock_quantity;
        this.average_rate = average_rate;
        this.summary = summary;
        this.book_pic = book_pic;
    }

    public int getBook_id() {
        return book_id;
    }

    public String getTitle() {
        return title;
    }

    public String getLanguage() {
        return language;
    }

    public String getIsbn() {
        return isbn;
    }

    public double getPrice() {
        return price;
    }

    public String getEdition() {
        return edition;
    }

    public int getPublication_year() {
        return publication_year;
    }

    public int getNumber_of_pages() {
        return number_of_pages;
    }

    public int getStock_quantity() {
        return stock_quantity;
    }

    public double getAverage_rate() {
        return average_rate;
    }

    public String getSummary() {
        return summary;
    }

    public Image getBook_pic() {
        return book_pic;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public void setPublication_year(int publication_year) {
        this.publication_year = publication_year;
    }

    public void setNumber_of_pages(int number_of_pages) {
        this.number_of_pages = number_of_pages;
    }

    public void setStock_quantity(int stock_quantity) {
        this.stock_quantity = stock_quantity;
    }

    public void setAverage_rate(double average_rate) {
        this.average_rate = average_rate;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setProfile_pic(Image book_pic) {
        this.book_pic = book_pic;
    }
}
