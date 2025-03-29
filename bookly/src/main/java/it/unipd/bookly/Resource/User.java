package it.unipd.bookly.Resource;

import java.util.List;

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

    // Constructors
    public User() {
        // Default
    }

    public User(String username, String password, String firstName, String lastName,
                String email, String phone, String address, String role) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.role = role;
    }

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

    // Getters
    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getRole() { return role; }
    public Image getProfileImage() { return profileImage; }
    public List<Order> getOrders() { return orders; }
    public List<Wishlist> getWishlists() { return wishlists; }
    public List<Cart> getCarts() { return carts; }

    // Setters
    public void setUserId(int userId) { this.userId = userId; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setAddress(String address) { this.address = address; }
    public void setRole(String role) { this.role = role; }
    public void setProfileImage(Image profileImage) { this.profileImage = profileImage; }
    public void setOrders(List<Order> orders) { this.orders = orders; }
    public void setWishlists(List<Wishlist> wishlists) { this.wishlists = wishlists; }
    public void setCarts(List<Cart> carts) { this.carts = carts; }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
