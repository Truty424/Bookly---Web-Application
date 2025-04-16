package it.unipd.bookly.Resource;

/**
 * Represents an author with details such as firstname, lastname, biography, and nationality.
 */
public class Author {

    private int author_id;
    private String first_name;
    private String last_name;
    private String biography;
    private String nationality;

    /**
     * Constructs an Author with all attributes.
     *
     * @param author_id   The unique ID of the author.
     * @param first_name  The first name of the author.
     * @param last_name   The last name of the author.
     * @param biography   The biography of the author.
     * @param nationality The nationality of the author.
     */
    public Author(int author_id, String first_name, String last_name, String biography, String nationality) {
        this.author_id = author_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.biography = biography;
        this.nationality = nationality;
    }

    /**
     * Constructs an Author without an ID.
     *
     * @param first_name  The first name of the author.
     * @param last_name   The last name of the author.
     * @param biography   The biography of the author.
     * @param nationality The nationality of the author.
     */
    public Author(String first_name, String last_name, String biography, String nationality) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.biography = biography;
        this.nationality = nationality;
    }

    /**
     * Gets the author's ID.
     *
     * @return The author's ID.
     */
    public int getAuthorId() {
        return author_id;
    }

    /**
     * Sets the author's ID.
     *
     * @param author_id The author's ID.
     */
    public void setAuthor_id(int author_id) {
        this.author_id = author_id;
    }

    /**
     * Gets the author's first name.
     *
     * @return The author's first name.
     */
    public String getFirstName() {
        return first_name;
    }

    /**
     * Sets the author's first name.
     *
     * @param first_name The author's first name.
     */
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    /**
     * Gets the author's last name.
     *
     * @return The author's last name.
     */
    public String getLastName() {
        return last_name;
    }

    /**
     * Sets the author's last name.
     *
     * @param last_name The author's last name.
     */
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    /**
     * Gets the author's biography.
     *
     * @return The author's biography.
     */
    public String get_biography() {
        return biography;
    }

    /**
     * Sets the author's biography.
     *
     * @param biography The author's biography.
     */
    public void set_biography(String biography) {
        this.biography = biography;
    }

    /**
     * Gets the author's nationality.
     *
     * @return The author's nationality.
     */
    public String get_nationality() {
        return nationality;
    }

    /**
     * Sets the author's nationality.
     *
     * @param nationality The author's nationality.
     */
    public void set_nationality(String nationality) {
        this.nationality = nationality;
    }

    /**
     * Gets the full name of the author.
     *
     * @return The full name of the author (first name + last name).
     */
    public String getName() {
        return first_name + " " + last_name;
    }
}
