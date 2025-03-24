INSERT INTO booklySchema.users (username, password, first_name, last_name, email, phone, address, role) VALUES
('johnsmith', 'password123', 'John', 'Smith', 'john@example.com', '+1234567890', '123 Elm St', 'user'),
('annawhite', 'securePass9', 'Anna', 'White', 'anna@example.com', '+1987654321', '456 Oak Ave', 'user'),
('mikebrown', 'Mike!pass22', 'Mike', 'Brown', 'mike@example.com', '+1122334455', '789 Pine Rd', 'admin'),
('sarahlee', 's@rahpass11', 'Sarah', 'Lee', 'sarah@example.com', '+1444555666', '321 Maple Blvd', 'user'),
('danielgray', 'dani3lG!8', 'Daniel', 'Gray', 'daniel@example.com', '+1555666777', '654 Birch Ln', 'user'),
('aliceblue', 'alicePass1', 'Alice', 'Blue', 'alice@example.com', '+1666777888', '12 Blue St', 'user'),
('bobgray', 'bobSecure2', 'Bob', 'Gray', 'bob@example.com', '+1777888999', '78 Gray Rd', 'user'),
('carolgreen', 'carolGreen3', 'Carol', 'Green', 'carol@example.com', '+1888999000', '56 Green Ln', 'user'),
('davidwhite', 'davidWhite4', 'David', 'White', 'david@example.com', '+1999000111', '44 White Blvd', 'user'),
('eveblack', 'eveBlack5', 'Eve', 'Black', 'eve@example.com', '+1222333444', '88 Black Ave', 'admin');


INSERT INTO booklySchema.authors (first_name, last_name, author_books, nationality) VALUES
('George', 'Orwell', '1984', 'British'),
('Harper', 'Lee', 'To Kill a Mockingbird', 'American'),
('J.K.', 'Rowling', 'Harry Potter', 'British'),
('Gabriel', 'Garcia Marquez', 'One Hundred Years of Solitude', 'Colombian'),
('Jane', 'Austen', 'Pride and Prejudice', 'British'),
('Leo', 'Tolstoy', 'War and Peace', 'Russian'),
('Mark', 'Twain', 'Adventures of Huckleberry Finn', 'American'),
('Fyodor', 'Dostoevsky', 'Crime and Punishment', 'Russian'),
('Mary', 'Shelley', 'Frankenstein', 'British'),
('Ernest', 'Hemingway', 'The Old Man and the Sea', 'American');


INSERT INTO booklySchema.publishers (publisher_name, phone, address) VALUES
('Penguin Books', '+1230001111', '80 Strand, London'),
('HarperCollins', '+1988002222', '195 Broadway, NY'),
('Scholastic', '+1122003333', '557 Broadway, NY'),
('Vintage', '+1999004444', 'New York'),
('Macmillan', '+1444005555', '120 Broadway, NY'),
('Oxford Press', '+1333006666', 'OUP, UK'),
('Random House', '+1444111222', '1745 Broadway, NY'),
('Simon & Schuster', '+1555222333', '1230 Avenue, NY'),
('Bloomsbury', '+1666333444', 'London, UK'),
('Anchor Books', '+1777444555', 'New York, NY');


INSERT INTO booklySchema.categories (name, description) VALUES
('Fiction', 'Narrative literary works'),
('Science Fiction', 'Fictional stories based on science'),
('Fantasy', 'Fiction with magical elements'),
('Biography', 'Life stories of people'),
('Classic', 'Timeless works of literature'),
('Drama', 'Plays and dramatic writing'),
('Mystery', 'Whodunit and suspense'),
('Romance', 'Love stories'),
('Self-help', 'Personal development'),
('History', 'Historical accounts and nonfiction');


INSERT INTO booklySchema.books (
    title, language, isbn, price, edition, publication_year,
    number_of_pages, stock_quantity, publisher_id, category_id
) VALUES
('1984', 'English', '9780451524935', 15.99, 'First', 1949, 328, 20, 1, 1),
('To Kill a Mockingbird', 'English', '9780061120084', 14.99, 'First', 1960, 281, 15, 2, 5),
('Harry Potter and the Sorcerer''s Stone', 'English', '9780439554930', 25.00, 'First', 1997, 309, 50, 3, 3),
('One Hundred Years of Solitude', 'English', '9780060883287', 18.50, 'First', 1967, 417, 10, 4, 1),
('Pride and Prejudice', 'English', '9781503290563', 10.00, 'First', 1813, 279, 12, 5, 5),
('War and Peace', 'English', '9780199232765', 22.99, 'First', 1869, 1225, 8, 6, 10),
('Crime and Punishment', 'English', '9780140449136', 18.99, 'First', 1866, 671, 5, 7, 10),
('Frankenstein', 'English', '9780486282114', 9.99, 'First', 1818, 280, 11, 9, 9),
('Huckleberry Finn', 'English', '9780486280615', 12.99, 'First', 1884, 366, 7, 8, 6),
('The Old Man and the Sea', 'English', '9780684801223', 14.99, 'First', 1952, 127, 7, 6);

INSERT INTO booklySchema.cart (user_id, quantity) VALUES
(1, 2),
(2, 1),
(3, 3),
(4, 1),
(5, 4),



INSERT INTO booklySchema.reviews (user_id, book_id, comment, rating) VALUES
(1, 1, 'A timeless dystopian novel.', 5),
(2, 2, 'Very touching story.', 4),
(3, 3, 'Absolutely magical!', 5),
(4, 4, 'Beautiful and complex.', 4),
(5, 5, 'A lovely classic.', 5);


INSERT INTO booklySchema.discounts (code, discount_percentage, expired_date) VALUES
('SAVE10', 10.00, '2025-12-31'),
('WELCOME15', 15.00, '2025-11-30'),
('FALL20', 20.00, '2025-10-15'),
('SPRING5', 5.00, '2025-09-01'),
('EXTRA25', 25.00, '2025-08-01');


INSERT INTO booklySchema.orders (cart_id, discount_id, total_amount, payment_method) VALUES
(1, 1, 28.78, 'credit_card'),
(2, 2, 12.74, 'in_person'),
(3, 3, 60.00, 'credit_card'),
(4, 4, 17.58, 'in_person'),
(5, 5, 22.50, 'credit_card');


INSERT INTO booklySchema.writes (book_id, author_id) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 5),
(6, 6),
(7, 7),
(8, 8),
(9, 9),
(10, 10);


INSERT INTO booklySchema.belongs_to (book_id, category_id) VALUES
(1, 1),
(2, 5),
(3, 3),
(4, 1),
(5, 5),
(6, 10), 
(7, 10), 
(8, 9),
(9, 6), 
(10, 6); 


INSERT INTO booklySchema.published_by (book_id, publisher_id) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 5),
(6, 6),
(7, 7),
(8, 8),
(9, 9),
(10, 10);


INSERT INTO booklySchema.contains (book_id, cart_id) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 5),
(6, 1),
(7, 2),
(8, 3),
(9, 4),
(10, 5);
