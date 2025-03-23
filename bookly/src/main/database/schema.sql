-- Database Creation
CREATE DATABASE booklydb OWNER booklyTeam ENCODING 'UTF8';

-- Create new Schema
DROP SCHEMA IF EXISTS booklySchema CASCADE;
CREATE SCHEMA booklySchema;

-- Create new domains
CREATE DOMAIN booklySchema.username_domain AS VARCHAR(30)
  CHECK (VALUE ~ '^[a-zA-Z0-9_]{5,30}$');

CREATE DOMAIN booklySchema.password_domain AS VARCHAR(255)
  CHECK (char_length(VALUE) >= 8);

CREATE DOMAIN booklySchema.phone_domain AS VARCHAR(20)
  CHECK (VALUE ~ '^\\+?[0-9\\-]{7,20}$');

-- ENUM types
CREATE TYPE booklySchema.user_role AS ENUM ('user', 'admin');
CREATE TYPE booklySchema.payment_method AS ENUM ('in_person', 'credit_card');

-- Users
CREATE TABLE booklySchema.users (
    user_id SERIAL PRIMARY KEY,
    username booklySchema.username_domain UNIQUE NOT NULL,
    password booklySchema.password_domain NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone booklySchema.phone_domain,
    address TEXT,
    role booklySchema.user_role DEFAULT 'user'
);

-- Authors
CREATE TABLE booklySchema.authors (
    author_id SERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    author_books VARCHAR(100) NOT NULL,
    nationality VARCHAR(100) NOT NULL
);

-- Publishers
CREATE TABLE booklySchema.publishers (
    publisher_id SERIAL PRIMARY KEY,
    publisher_name VARCHAR(100) NOT NULL,
    phone booklySchema.phone_domain,
    address TEXT
);

-- Categories
CREATE TABLE booklySchema.categories (
    category_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT
);


CREATE TABLE bookly_db.books (
    book_id SERIAL PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    language VARCHAR(50),
    isbn VARCHAR(20) UNIQUE,
    price NUMERIC(10,2) NOT NULL,
    edition VARCHAR(50),
    publication_year INT CHECK (publication_year BETWEEN 1000 AND EXTRACT(YEAR FROM CURRENT_DATE)::INT),
    number_of_pages INT CHECK (number_of_pages > 0),
    stock_quantity INT DEFAULT 0 CHECK (stock_quantity >= 0),
    publisher_id INT,
    category_id INT,
    FOREIGN KEY (publisher_id) REFERENCES bookly_db.publishers(publisher_id),
    FOREIGN KEY (category_id) REFERENCES bookly_db.categories(category_id)
);


CREATE TABLE booklySchema.writes (
    book_id INTEGER NOT NULL,
    author_id INTEGER NOT NULL,
    PRIMARY KEY (book_id, author_id),
    FOREIGN KEY (book_id) REFERENCES booklySchema.books(book_id),
    FOREIGN KEY (author_id) REFERENCES booklySchema.authors(author_id)
);

CREATE TABLE booklySchema.cotains (
    book_id INTEGER NOT NULL,
    cart_id INTEGER NOT NULL,
    PRIMARY KEY (book_id, cart_id),
    FOREIGN KEY (book_id) REFERENCES booklySchema.books(book_id),
    FOREIGN KEY (cart_id) REFERENCES booklySchema.cart(cart_id)
);


CREATE TABLE booklySchema.belongs_to (
    book_id INTEGER NOT NULL,
    category_id INTEGER NOT NULL,
    PRIMARY KEY (book_id, category_id),
    FOREIGN KEY (book_id) REFERENCES booklySchema.books(book_id),
    FOREIGN KEY (category_id) REFERENCES booklySchema.categories(category_id)
);

CREATE TABLE booklySchema.published_by (
    book_id INTEGER NOT NULL,
    publisher_id INTEGER NOT NULL,
    PRIMARY KEY (book_id, publisher_id),
    FOREIGN KEY (book_id) REFERENCES booklySchema.books(book_id),
    FOREIGN KEY (publisher_id) REFERENCES booklySchema.publishers(publisher_id)
);

-- Carts
CREATE TABLE booklySchema.cart (
    cart_id SERIAL PRIMARY KEY,
    user_id INTEGER UNIQUE NOT NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    quantity INTEGER NOT NULL DEFAULT 0
    FOREIGN KEY (user_id) REFERENCES booklySchema.users(user_id)
);


-- Reviews
CREATE TABLE booklySchema.reviews (
    review_id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    book_id INTEGER NOT NULL,
    comment TEXT,
    rating INTEGER CHECK (rating >= 1 AND rating <= 5),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES booklySchema.users(user_id),
    FOREIGN KEY (book_id) REFERENCES booklySchema.books(book_id)
);


CREATE TABLE booklySchema.discounts (
    discount_id SERIAL PRIMARY KEY,
    code VARCHAR(50) UNIQUE NOT NULL,
    discount_percentage NUMERIC(5,2) CHECK (discount_percentage >= 0 AND discount_percentage <= 100),
    expired_date DATE
);


CREATE TABLE booklySchema.orders (
    order_id SERIAL PRIMARY KEY,
    cart_id INTEGER UNIQUE NOT NULL,
    discount_id INTEGER,
    total_amount NUMERIC(10,2) NOT NULL,
    payment_method booklySchema.payment_method NOT NULL,
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cart_id) REFERENCES booklySchema.carts(cart_id),
    FOREIGN KEY (discount_id) REFERENCES booklySchema.discounts(discount_id)
);
