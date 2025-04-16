package it.unipd.bookly.Resource;

/**
 * Represents a category of books, including its ID, name, and description.
 */
public class Category {
    private int category_id;
    private String category_name;
    private String description;

    /**
     * Default constructor for Category.
     */
    public Category() {}

    /**
     * Constructs a Category with all attributes.
     *
     * @param category_id   The ID of the category.
     * @param category_name The name of the category.
     * @param description   The description of the category.
     */
    public Category(int category_id, String category_name, String description) {
        this.category_id = category_id;
        this.category_name = category_name;
        this.description = description;
    }

    /**
     * Constructs a Category without an ID.
     *
     * @param category_name The name of the category.
     * @param description   The description of the category.
     */
    public Category(String category_name, String description) {
        this.category_name = category_name;
        this.description = description;
    }

    /**
     * Gets the ID of the category.
     *
     * @return The category ID.
     */
    public int getCategory_id() { return category_id; }

    /**
     * Gets the name of the category.
     *
     * @return The category name.
     */
    public String getCategory_name() { return category_name; }

    /**
     * Gets the description of the category.
     *
     * @return The category description.
     */
    public String getDescription() { return description; }

    /**
     * Sets the ID of the category.
     *
     * @param category_id The category ID.
     */
    public void setCategory_id(int category_id) { this.category_id = category_id; }

    /**
     * Sets the name of the category.
     *
     * @param category_name The category name.
     */
    public void setCategory_name(String category_name) { this.category_name = category_name; }

    /**
     * Sets the description of the category.
     *
     * @param description The category description.
     */
    public void setDescription(String description) { this.description = description; }
}
