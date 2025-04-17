package it.unipd.bookly.Resource;

/**
 * Represents a book resource, including details such as title, language, ISBN, price, 
 * edition, publication year, number of pages, stock quantity, average rating, summary, 
 * and an associated image.
 */
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
    private Image image;

    /**
     * Default constructor for Book.
     */
    public Book() {
    }

    /**
     * Constructs a Book with all attributes except the image.
     *
     * @param book_id          The ID of the book.
     * @param title            The title of the book.
     * @param language         The language of the book.
     * @param isbn             The ISBN of the book.
     * @param price            The price of the book.
     * @param edition          The edition of the book.
     * @param publication_year The publication year of the book.
     * @param number_of_pages  The number of pages in the book.
     * @param stock_quantity   The stock quantity of the book.
     * @param average_rate     The average rating of the book.
     * @param summary          The summary of the book.
     */
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

    /**
     * Constructs a Book with all attributes including the image.
     *
     * @param book_id          The ID of the book.
     * @param title            The title of the book.
     * @param language         The language of the book.
     * @param isbn             The ISBN of the book.
     * @param price            The price of the book.
     * @param edition          The edition of the book.
     * @param publication_year The publication year of the book.
     * @param number_of_pages  The number of pages in the book.
     * @param stock_quantity   The stock quantity of the book.
     * @param average_rate     The average rating of the book.
     * @param summary          The summary of the book.
     * @param image         The image associated with the book.
     */
    public Book(int book_id, String title, String language, String isbn, double price, String edition,
            int publication_year, int number_of_pages, int stock_quantity, double average_rate, String summary,Image image) {
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
        this.image = image;

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
     * Gets the language of the book.
     *
     * @return The book language.
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Gets the ISBN of the book.
     *
     * @return The book ISBN.
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Gets the price of the book.
     *
     * @return The book price.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Gets the edition of the book.
     *
     * @return The book edition.
     */
    public String getEdition() {
        return edition;
    }

    /**
     * Gets the publication year of the book.
     *
     * @return The publication year.
     */
    public int getPublication_year() {
        return publication_year;
    }

    /**
     * Gets the number of pages in the book.
     *
     * @return The number of pages.
     */
    public int getNumber_of_pages() {
        return number_of_pages;
    }

    /**
     * Gets the stock quantity of the book.
     *
     * @return The stock quantity.
     */
    public int getStockQuantity() {
        return stock_quantity;
    }

    /**
     * Gets the average rating of the book.
     *
     * @return The average rating.
     */
    public double getAverage_rate() {
        return average_rate;
    }

    /**
     * Gets the summary of the book.
     *
     * @return The book summary.
     */
    public String getSummary() {
        return summary;
    }

    /**
     * Gets the image associated with the book.
     *
     * @return The book image.
     */
    public Image getImage() {
        return image;
    }

    /**
     * Sets the ID of the book.
     *
     * @param book_id The book ID.
     */
    public void setBookId(int book_id) {
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
     * Sets the language of the book.
     *
     * @param language The book language.
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * Sets the ISBN of the book.
     *
     * @param isbn The book ISBN.
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * Sets the price of the book.
     *
     * @param price The book price.
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Sets the edition of the book.
     *
     * @param edition The book edition.
     */
    public void setEdition(String edition) {
        this.edition = edition;
    }

    /**
     * Sets the publication year of the book.
     *
     * @param publication_year The publication year.
     */
    public void setPublication_year(int publication_year) {
        this.publication_year = publication_year;
    }

    /**
     * Sets the number of pages in the book.
     *
     * @param number_of_pages The number of pages.
     */
    public void setNumber_of_pages(int number_of_pages) {
        this.number_of_pages = number_of_pages;
    }

    /**
     * Sets the stock quantity of the book.
     *
     * @param stock_quantity The stock quantity.
     */
    public void setStock_quantity(int stock_quantity) {
        this.stock_quantity = stock_quantity;
    }

    /**
     * Sets the average rating of the book.
     *
     * @param average_rate The average rating.
     */
    public void setAverage_rate(double average_rate) {
        this.average_rate = average_rate;
    }

    /**
     * Sets the summary of the book.
     *
     * @param summary The book summary.
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * Sets the image associated with the book.
     *
     * @param image The book image.
     */
    public void setImage(Image image) {
        this.image = image;
    }
}
