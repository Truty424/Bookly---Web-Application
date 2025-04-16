package it.unipd.bookly.Resource;

import java.util.List;

/**
 * Represents a user in the Bookly system. A user includes details such as
 * username, password, personal information, and relationships with orders,
 * wishlists, and carts.
 */
public class User {

    private int userId;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private String role;

    private Image profileImage;

    // Relations
    private List<Order> orders;
    private List<Wishlist> wishlists;
    private List<Cart> carts;

    /**
     * Default constructor for User.
     */
    public User() {
        // Default
    }

    /**
     * Constructs a User with basic attributes.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @param firstName The first name of the user.
     * @param lastName The last name of the user.
     * @param email The email address of the user.
     * @param phone The phone number of the user.
     * @param address The address of the user.
     * @param role The role of the user (either "Admin", "Customer").
     */
    public User(String username, String password, String firstName, String lastName,
            String email, String phone, String address, String role, Image profileImage) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.role = role;
        this.profileImage = profileImage;
    }

    /**
     * Constructs a User with basic attributes.
     *
     * @param userId the id of the user
     * @param username The username of the user.
     * @param password The password of the user.
     * @param firstName The first name of the user.
     * @param lastName The last name of the user.
     * @param email The email address of the user.
     * @param phone The phone number of the user.
     * @param address The address of the user.
     * @param role The role of the user (either "Admin", "Customer").
     */
    public User(int userId, String username, String password, String firstName, String lastName,
            String email, String phone, String address, String role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.role = role;
    }

    /**
     * Constructs a User with all attributes, including the profile image.
     *
     * @param userId the id of the user
     * @param username The username of the user.
     * @param password The password of the user.
     * @param firstName The first name of the user.
     * @param lastName The last name of the user.
     * @param email The email address of the user.
     * @param phone The phone number of the user.
     * @param address The address of the user.
     * @param role The role of the user (either "Admin", "Customer").
     * @param profileImage The profile image of the user.
     */
    public User(int userId, String username, String password, String firstName, String lastName,
            String email, String phone, String address, String role, Image profileImage) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.role = role;
        this.profileImage = profileImage;
    }

    /**
     * Gets the ID of the user.
     *
     * @return The user ID.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Gets the username of the user.
     *
     * @return The username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the password of the user.
     *
     * @return The password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets the first name of the user.
     *
     * @return The first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Gets the last name of the user.
     *
     * @return The last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Gets the email address of the user.
     *
     * @return The email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets the phone number of the user.
     *
     * @return The phone number.
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Gets the address of the user.
     *
     * @return The address.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Gets the role of the user.
     *
     * @return The role.
     */
    public String getRole() {
        return role;
    }

    /**
     * Gets the profile image of the user.
     *
     * @return The profile image.
     */
    public Image getProfileImage() {
        return profileImage;
    }

    /**
     * Gets the list of orders associated with the user.
     *
     * @return The list of orders.
     */
    public List<Order> getOrders() {
        return orders;
    }

    /**
     * Gets the list of wishlists associated with the user.
     *
     * @return The list of wishlists.
     */
    public List<Wishlist> getWishlists() {
        return wishlists;
    }

    /**
     * Gets the list of carts associated with the user.
     *
     * @return The list of carts.
     */
    public List<Cart> getCarts() {
        return carts;
    }

    /**
     * Sets the ID of the user.
     *
     * @param userId The user ID.
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Sets the username of the user.
     *
     * @param username The username.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets the password of the user.
     *
     * @param password The password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Sets the first name of the user.
     *
     * @param firstName The first name.
     */
    public void setFirst_name(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Sets the last name of the user.
     *
     * @param lastName The last name.
     */
    public void setLast_name(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Sets the email address of the user.
     *
     * @param email The email address.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Sets the phone number of the user.
     *
     * @param phone The phone number.
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Sets the address of the user.
     *
     * @param address The address.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Sets the role of the user.
     *
     * @param role The role.
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Sets the profile image of the user.
     *
     * @param profileImage The profile image.
     */
    public void setProfileImage(Image profileImage) {
        this.profileImage = profileImage;
    }

    /**
     * Sets the list of orders associated with the user.
     *
     * @param orders The list of orders.
     */
    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    /**
     * Sets the list of wishlists associated with the user.
     *
     * @param wishlists The list of wishlists.
     */
    public void setWishlists(List<Wishlist> wishlists) {
        this.wishlists = wishlists;
    }

    /**
     * Sets the list of carts associated with the user.
     *
     * @param carts The list of carts.
     */
    public void setCarts(List<Cart> carts) {
        this.carts = carts;
    }

    @Override
    public String toString() {
        return "User{"
                + "userId=" + userId
                + ", username='" + username + '\''
                + ", email='" + email + '\''
                + ", role='" + role + '\''
                + '}';
    }
}
