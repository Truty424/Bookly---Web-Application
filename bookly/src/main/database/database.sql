-- Database Creation
CREATE DATABASE BooklyDB OWNER postgres ENCODING 'UTF8';

-- Create new Schema
DROP SCHEMA IF EXISTS booklySchema CASCADE;
CREATE SCHEMA booklySchema;

-- Create new domains
CREATE DOMAIN booklySchema.username_domain AS VARCHAR(255)
  CHECK (VALUE ~ '^[a-zA-Z0-9_]{2,50}$');

CREATE DOMAIN booklySchema.password_domain AS VARCHAR(255)
  CHECK (char_length(VALUE) >= 8);

CREATE DOMAIN booklySchema.phone_domain AS VARCHAR(20)
  CHECK (VALUE ~ '^\\+?[0-9\\-]{7,20}$');

ALTER DOMAIN booklySchema.phone_domain
DROP CONSTRAINT phone_domain_check;

-- ENUM types
CREATE TYPE booklySchema.user_role AS ENUM ('user', 'admin');
CREATE TYPE booklySchema.payment_method AS ENUM ('in_person', 'credit_card');
CREATE TYPE booklySchema.payment_status AS ENUM ('placed', 'paid','cancelled','shipped','delivered');

-- Users
CREATE TABLE IF NOT EXISTS booklySchema.users (
    user_id SERIAL PRIMARY KEY,
    username booklySchema.username_domain UNIQUE NOT NULL,
    password booklySchema.password_domain NOT NULL,
    firstName VARCHAR(50) NOT NULL,
    lastName VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone booklySchema.phone_domain,
    address TEXT,
    role booklySchema.user_role DEFAULT 'user'
);

CREATE TABLE IF NOT EXISTS booklySchema.user_image (
    user_id INTEGER PRIMARY KEY NOT NULL,
    image BYTEA NOT NULL,
    image_type VARCHAR(255) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES booklySchema.users(user_id)
);


CREATE TABLE IF NOT EXISTS booklySchema.authors (
    author_id SERIAL PRIMARY KEY,
    firstName VARCHAR(100) NOT NULL,
    lastName VARCHAR(100) NOT NULL,
    biography TEXT,
    nationality VARCHAR(100) NOT NULL
);


CREATE TABLE IF NOT EXISTS booklySchema.publishers (
    publisher_id SERIAL PRIMARY KEY,
    publisher_name VARCHAR(100) NOT NULL,
    phone booklySchema.phone_domain,
    address TEXT
);

CREATE TABLE IF NOT EXISTS booklySchema.categories (
    category_id SERIAL PRIMARY KEY,
    category_name VARCHAR(50) NOT NULL,
    description TEXT
);


CREATE TABLE IF NOT EXISTS booklySchema.books (
    book_id SERIAL PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    language VARCHAR(50),
    isbn VARCHAR(20) UNIQUE,
    price REAL NOT NULL,
    edition VARCHAR(50),
    publication_year INTEGER CHECK (publication_year BETWEEN 1000 AND EXTRACT(YEAR FROM CURRENT_DATE)::INTEGER),
    number_of_pages INTEGER CHECK (number_of_pages > 0),
    stock_quantity INTEGER DEFAULT 0 CHECK (stock_quantity >= 0),
    average_rate REAL,
    summary TEXT
);

CREATE TABLE IF NOT EXISTS booklySchema.book_image (
    book_id INTEGER PRIMARY KEY NOT NULL,
    image BYTEA NOT NULL,
    image_type VARCHAR(255) NOT NULL,
    FOREIGN KEY (book_id) REFERENCES booklySchema.books(book_id) 
);



CREATE TABLE IF NOT EXISTS booklySchema.wishlists (
    wishlist_id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES booklySchema.users(user_id)
);


CREATE TABLE IF NOT EXISTS booklySchema.orders (
    order_id SERIAL PRIMARY KEY,
    total_price NUMERIC(10,2) NOT NULL,
    payment_method booklySchema.payment_method NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    address TEXT NOT NULL,
    shipment_code VARCHAR(50),
    status booklySchema.payment_status NOT NULL
);


CREATE TABLE IF NOT EXISTS booklySchema.discounts (
    discount_id SERIAL PRIMARY KEY,
    code VARCHAR(50) UNIQUE NOT NULL,
    discount_percentage NUMERIC(5,2) CHECK (discount_percentage >= 0 AND discount_percentage <= 100),
    expired_date TIMESTAMP
);

CREATE TABLE IF NOT EXISTS booklySchema.reviews (
    review_id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    book_id INTEGER NOT NULL,
    comment TEXT,
    rating INTEGER CHECK (rating >= 1 AND rating <= 5),
    review_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    number_of_likes INTEGER,
    number_of_dislikes INTEGER,
    FOREIGN KEY (user_id) REFERENCES booklySchema.users(user_id),
    FOREIGN KEY (book_id) REFERENCES booklySchema.books(book_id)
);

CREATE TABLE IF NOT EXISTS booklySchema.shoppingcart (
    cart_id SERIAL PRIMARY KEY,
    shipment_method booklySchema.payment_method,
    user_id INTEGER UNIQUE NOT NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    quantity INTEGER NOT NULL DEFAULT 0,
    total_price NUMERIC(10,2) NOT NULL,
    discount_id INTEGER,
    order_id INTEGER,
    FOREIGN KEY (user_id) REFERENCES booklySchema.users(user_id),
    FOREIGN KEY (discount_id) REFERENCES booklySchema.discounts(discount_id),
    FOREIGN KEY (order_id) REFERENCES booklySchema.orders(order_id)
);



CREATE TABLE IF NOT EXISTS booklySchema.published_by (
    book_id INTEGER NOT NULL,
    publisher_id INTEGER NOT NULL,
    PRIMARY KEY (book_id, publisher_id),
    FOREIGN KEY (book_id) REFERENCES booklySchema.books(book_id),
    FOREIGN KEY (publisher_id) REFERENCES booklySchema.publishers(publisher_id)
);


CREATE TABLE IF NOT EXISTS booklySchema.writes (
    book_id INTEGER NOT NULL,
    author_id INTEGER NOT NULL,
    PRIMARY KEY (book_id, author_id),
    FOREIGN KEY (book_id) REFERENCES booklySchema.books(book_id),
    FOREIGN KEY (author_id) REFERENCES booklySchema.authors(author_id)
);

CREATE TABLE IF NOT EXISTS booklySchema.contains (
    book_id INTEGER NOT NULL,
    cart_id INTEGER NOT NULL,
    PRIMARY KEY (book_id, cart_id),
    FOREIGN KEY (book_id) REFERENCES booklySchema.books(book_id),
    FOREIGN KEY (cart_id) REFERENCES booklySchema.shoppingcart(cart_id)
);


CREATE TABLE IF NOT EXISTS booklySchema.category_belongs (
    book_id INTEGER NOT NULL,
    category_id INTEGER NOT NULL,
    PRIMARY KEY (book_id, category_id),
    FOREIGN KEY (book_id) REFERENCES booklySchema.books(book_id),
    FOREIGN KEY (category_id) REFERENCES booklySchema.categories(category_id)
);

CREATE TABLE IF NOT EXISTS booklySchema.contains_wishlist (
    wishlist_id INTEGER NOT NULL,
    book_id INTEGER UNIQUE, 
    PRIMARY KEY (wishlist_id, book_id),
    FOREIGN KEY (wishlist_id) REFERENCES booklySchema.wishlists(wishlist_id),
    FOREIGN KEY (book_id) REFERENCES booklySchema.books(book_id)
);
