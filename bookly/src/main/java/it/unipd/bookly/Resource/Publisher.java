package it.unipd.bookly.Resource;

import java.util.Objects;


/**
 * Represents a publisher entity in the Bookly system.
 * A publisher includes details such as the publisher ID, name, phone number, and address.
 */
public class Publisher {

    private int publisherId;
    private String publisherName;
    private String phone;
    private String address;

    /**
     * Default constructor.
     */
    public Publisher() {}

    /**
     * Constructs a Publisher with all attributes, including the ID.
     *
     * @param publisherId   The ID of the publisher.
     * @param publisherName The name of the publisher.
     * @param phone         The phone number of the publisher.
     * @param address       The address of the publisher.
     */
    public Publisher(int publisherId, String publisherName, String phone, String address) {
        this.publisherId = publisherId;
        this.publisherName = publisherName;
        this.phone = phone;
        this.address = address;
    }

    /**
     * Constructs a Publisher without an ID.
     *
     * @param publisherName The name of the publisher.
     * @param phone         The phone number of the publisher.
     * @param address       The address of the publisher.
     */
    public Publisher(String publisherName, String phone, String address) {
        this.publisherName = publisherName;
        this.phone = phone;
        this.address = address;
    }

    /**
     * Gets the ID of the publisher.
     *
     * @return The publisher ID.
     */
    public int getPublisherId() {
        return publisherId;
    }

    /**
     * Gets the name of the publisher.
     *
     * @return The publisher name.
     */
    public String getPublisherName() {
        return publisherName;
    }

    /**
     * Gets the phone number of the publisher.
     *
     * @return The publisher phone number.
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Gets the address of the publisher.
     *
     * @return The publisher address.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the ID of the publisher.
     *
     * @param publisherId The publisher ID.
     */
    public void setPublisherId(int publisherId) {
        this.publisherId = publisherId;
    }

    /**
     * Sets the name of the publisher.
     *
     * @param publisherName The publisher name.
     */
    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    /**
     * Sets the phone number of the publisher.
     *
     * @param phone The publisher phone number.
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Sets the address of the publisher.
     *
     * @param address The publisher address.
     */
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
