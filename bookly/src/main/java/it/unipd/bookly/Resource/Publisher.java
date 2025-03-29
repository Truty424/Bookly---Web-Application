package it.unipd.bookly.resource;

import java.util.Objects;

/**
 * Represents a publisher entity in the Bookly system.
 */
public class Publisher {
    
    private int publisherId;
    private String publisherName;
    private String phone;
    private String address;

    // Default constructor (required for frameworks/serialization)
    public Publisher() {}

    // Full constructor with ID (retrieval)
    public Publisher(int publisherId, String publisherName, String phone, String address) {
        this.publisherId = publisherId;
        this.publisherName = publisherName;
        this.phone = phone;
        this.address = address;
    }

    // Constructor without ID (insertion)
    public Publisher(String publisherName, String phone, String address) {
        this.publisherName = publisherName;
        this.phone = phone;
        this.address = address;
    }

    // --- Getters ---
    public int getPublisherId() {
        return publisherId;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    // --- Setters ---
    public void setPublisherId(int publisherId) {
        this.publisherId = publisherId;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Publisher that)) return false;
        return publisherId == that.publisherId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(publisherId);
    }
}
