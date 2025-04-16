package it.unipd.bookly.Resource;

/**
 * Represents an author with details such as firstname, lastname, biography, and nationality.
 */
public class Author {

    private int author_id;
    private String firstName;
    private String lastName;
    private String biography;
    private String nationality;

    /**
     * Constructs an Author with all attributes.
     *
     * @param author_id   The unique ID of the author.
     * @param firstName  The first name of the author.
     * @param lastName   The last name of the author.
     * @param biography   The biography of the author.
     * @param nationality The nationality of the author.
     */
    public Author(int author_id, String firstName, String lastName, String biography, String nationality) {
        this.author_id = author_id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.biography = biography;
        this.nationality = nationality;
    }

    /**
     * Constructs an Author without an ID.
     *
     * @param firstName  The first name of the author.
     * @param lastName   The last name of the author.
     * @param biography   The biography of the author.
     * @param nationality The nationality of the author.
     */
    public Author(String firstName, String lastName, String biography, String nationality) {
        this.firstName = firstName;
        this.lastName = lastName;
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
        return firstName;
    }

    /**
     * Sets the author's first name.
     *
     * @param firstName The author's first name.
     */
    public void setfirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the author's last name.
     *
     * @return The author's last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the author's last name.
     *
     * @param lastName The author's last name.
     */
    public void setlastName(String lastName) {
        this.lastName = lastName;
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
        return firstName + " " + lastName;
    }
}
