-- Authors
INSERT INTO booklySchema.authors (firstName, lastName, biography, nationality) VALUES
('Dan', 'Brown', 'American author best known for his thriller novels featuring Robert Langdon, including The Da Vinci Code.', 'American'),
('Stieg', 'Larsson', 'Swedish journalist and writer, renowned for his Millennium series, including The Girl with the Dragon Tattoo.', 'Swedish'),
('Khaled', 'Hosseini', 'Afghan-American novelist and physician, acclaimed for The Kite Runner and A Thousand Splendid Suns.', 'Afghan-American'),
('Suzanne', 'Collins', 'American author and screenwriter, widely recognized for The Hunger Games trilogy.', 'American'),
('Paulo', 'Coelho', 'Brazilian novelist known for his spiritual and symbolic storytelling, notably in The Alchemist.', 'Brazilian'),
('John', 'Green', 'American author and YouTube content creator, famous for The Fault in Our Stars.', 'American'),
('Markus', 'Zusak', 'Australian writer celebrated for The Book Thief, a novel set in Nazi Germany.', 'Australian'),
('Kathryn', 'Stockett', 'American novelist, best known for her debut novel The Help, exploring racial issues in the 1960s American South.', 'American'),
('Gillian', 'Flynn', 'American author and former journalist, recognized for her psychological thriller Gone Girl.', 'American'),
('Cormac', 'McCarthy', 'American novelist and playwright, awarded the Pulitzer Prize for The Road.', 'American'),
('Yann', 'Martel', 'Canadian author, acclaimed for Life of Pi, a novel about survival and faith.', 'Canadian'),
('Alice', 'Sebold', 'American author known for The Lovely Bones, a novel dealing with loss and the afterlife.', 'American'),
('Audrey', 'Niffenegger', 'American writer and artist, best known for her debut novel The Time Traveler''s Wife.', 'American'),
('William', 'Young', 'Canadian author, recognized for The Shack, a novel exploring themes of faith and redemption.', 'Canadian'),
('Rhonda', 'Byrne', 'Australian television writer and producer, known for The Secret, a book on the law of attraction.', 'Australian'),
('Elizabeth', 'Gilbert', 'American author and journalist, famous for her memoir Eat, Pray, Love.', 'American'),
('Paula', 'Hawkins', 'British author, best known for her psychological thriller The Girl on the Train.', 'British'),
('Erin', 'Morgenstern', 'American multimedia artist and author, celebrated for her debut novel The Night Circus.', 'American'),
('Andy', 'Weir', 'American novelist and software engineer, known for his science fiction novel The Martian.', 'American');


-- Users
INSERT INTO booklySchema.users (username, password, firstName, lastName, email, phone, address, role) VALUES
('esmith', MD5('vUU@3MdB7b'), 'Phillip', 'Akhtar', 'ypritchard@yahoo.co.uk', '+441514960872', 'Flat 09, Terry centers, New Lesleyhaven, G0J 2DZ', 'admin'),
('smithchristine', MD5('(6dR#aSJ$E'), 'Elizabeth', 'Stewart', 'cheryl80@hotmail.com', '+14124118244', '48740 Cynthia Village Suite 005, Lake Tina, WA 58413', 'user'),
('bensimpson', MD5('s)$KQ8Dvgm'), 'Dr', 'Morris', 'stewart33@gmail.com', '+441174960654', 'Flat 86R, Singh crescent, Antonyshire, BD1 5PS', 'user'),
('margot65', MD5('6dU6a!hs*J'), 'Henri', 'Guillou', 'marc69@dbmail.com', '+33473872148', 'chemin Jean, 20037 Lecoq-sur-Legrand', 'user'),
('jcontreras', MD5('ZO8NGbtf(8'), 'Barbara', 'Anderson', 'matthew81@yahoo.com', '+27823166587', '669 Melissa Wade, Woodsstad, SA, 2986', 'user');

-- Publishers
INSERT INTO booklySchema.publishers (publisher_name, phone, address) VALUES
('Gandhi, Virk and Bedi', '+918170262174', '15/865, Viswanathan Chowk, Tadipatri 343161'),
('Hebert S.A.', '+49300504556', '369, rue Chevalier, 93792 Pinto'),
('Ramos', '+556107482175', 'Vila Luiz Fernando Ramos, 73, Barro Preto, 13695-944 Novaes / GO'),
('Armstrong Ltd', '+443069990097', '533 Davies stravenue, Lake Lewis, RM0M 0YQ'),
('Clarke, Walker and Rose', '+4401414960232', 'Flat 47, Morris junction, South Keithberg, S85 6AL'),
('银嘉信息有限公司', '+8615649651370', '江西省佛山县朝阳长春街F座 813826'),
('Davies-Reid', '+441914960692', '96 Rhodes land, South Louise, N15 0RL'),
('Costa Ramos S/A', '+551190053293', 'Favela de da Rosa, 35, Vila Suzana Primeira Seção, 42284210 Alves / MS'),
('Martins Aragão S.A.', '+557102681177', 'Colônia de Porto, 139, Vila Oeste, 70076-617 Cardoso de Cardoso / MT'),
('Finke', '+4902499856984', 'Ljudmila-Trapp-Gasse 67, 65766 Heinsberg'),
('Penguin Random House', '+12127829000', 'New York, USA'),
('HarperCollins', '+18002427737', '195 Broadway, New York, USA'),
('Macmillan Publishers', '+442078414000', 'London, UK'),
('Simon & Schuster', '+18002232336', 'Rockefeller Center, New York, USA'),
('Hachette Livre', '+33143923000', 'Paris, France'),
('Bloomsbury Publishing', '+442076315600', 'London, UK'),
('Scholastic Corporation', '+12123436100', 'Broadway, New York, USA'),
('Oxford University Press', '+441865556767', 'Oxford, UK'),
('Cambridge University Press', '+441223358331', 'Cambridge, UK'),
('Springer Nature', '+4962214870', 'Heidelberg, Germany');

-- Categories
INSERT INTO booklySchema.categories (category_name, description) VALUES
('Fiction', 'Narrative literary works from all cultures.'),
('Science Fiction', 'Futuristic stories exploring science and technology.'),
('Fantasy', 'Imaginative tales involving magic and myth.'),
('Biography', 'Detailed descriptions of a person''s life.'),
('Classic', 'Timeless literature recognized worldwide.'),
('Drama', 'Emotionally driven narratives often performed.'),
('Mystery', 'Suspense-filled investigations and puzzles.'),
('Romance', 'Stories centered on love and relationships.'),
('Self-help', 'Guides for personal growth and improvement.'),
('History', 'Accounts of significant past events and cultures.');



-- Books
INSERT INTO booklySchema.books (
     title, language, isbn, price, edition, publication_year,
     number_of_pages, stock_quantity, average_rate, summary
 ) VALUES
 ('The Da Vinci Code', 'English', '978-0-385-50420-1', 19.95, 'Illustrated', 2003, 454, 50, 3.8, 'A symbologist uncovers a secret society while investigating a murder in the Louvre.'),
 ('The Girl with the Dragon Tattoo', 'English', '978-0-307-45454-6', 14.95, 'First', 2005, 465, 40, 4.1, 'A journalist and a hacker delve into a wealthy family''s dark secrets.'),
 ('The Kite Runner', 'English', '978-1-59463-193-1', 16.00, '10th Anniversary', 2003, 371, 35, 4.3, 'A tale of friendship and redemption set against Afghanistan''s turbulent history.'),
 ('The Hunger Games', 'English', '978-0-439-02352-8', 12.99, 'Collector''s', 2008, 374, 60, 4.5, 'In a dystopian future, teens fight to the death in a televised event.'),
 ('The Alchemist', 'Portuguese', '978-0-06-112241-5', 15.99, 'Deluxe', 1988, 208, 45, 3.9, 'A shepherd embarks on a journey to fulfill his personal legend.'),
 ('The Fault in Our Stars', 'English', '978-0-525-47881-2', 13.99, 'Collector''s', 2012, 313, 50, 4.2, 'Two teenagers with cancer fall in love and explore life''s meaning.'),
 ('The Book Thief', 'English', '978-0-375-84220-7', 12.95, 'Anniversary', 2005, 552, 40, 4.4, 'A young girl finds solace in books during Nazi Germany.'),
 ('The Help', 'English', '978-0-399-15534-5', 16.00, 'First', 2009, 524, 30, 4.3, 'African American maids share their stories in 1960s Mississippi.'),
 ('Gone Girl', 'English', '978-0-307-58836-4', 15.00, 'Reprint', 2012, 422, 35, 4.0, 'A man becomes the prime suspect when his wife disappears.'),
 ('The Road', 'English', '978-0-307-26543-3', 14.00, 'First', 2006, 287, 25, 4.1, 'A father and son journey through a post-apocalyptic landscape.'),
 ('Life of Pi', 'English', '978-0-15-602732-8', 14.95, 'Illustrated', 2001, 319, 30, 4.0, 'A boy survives a shipwreck and shares a lifeboat with a tiger.'),
 ('The Lovely Bones', 'English', '978-0-316-66634-3', 13.99, 'First', 2002, 328, 20, 3.8, 'A murdered girl watches over her family from the afterlife.'),
 ('The Time Traveler''s Wife', 'English', '978-0-15-602943-8', 14.95, 'First', 2003, 546, 25, 3.9, 'A love story between a man with a time-traveling condition and his wife.'),
 ('The Shack', 'English', '978-0-9647292-3-7', 14.99, 'First', 2007, 256, 30, 3.8, 'A man encounters God in a shack after a family tragedy.'),
 ('The Secret', 'English', '978-1-58270-170-7', 16.95, 'First', 2006, 198, 40, 3.7, 'A self-help book exploring the law of attraction.'),
 ('Eat, Pray, Love', 'English', '978-0-14-303841-2', 15.00, 'First', 2006, 334, 35, 3.6, 'A woman travels the world seeking self-discovery after a divorce.'),
 ('A Thousand Splendid Suns', 'English', '978-1-59448-385-1', 16.00, 'First', 2007, 384, 30, 4.4, 'Two women form a bond amidst Afghanistan''s turmoil.'),
 ('The Girl on the Train', 'English', '978-1-59463-402-4', 16.00, 'First', 2015, 336, 40, 3.9, 'A woman becomes entangled in a missing person investigation.'),
 ('The Night Circus', 'English', '978-0-385-53463-5', 15.95, 'First', 2011, 387, 25, 4.0, 'Two magicians compete in a magical, traveling circus.'),
 ('The Martian', 'English', '978-0-8041-3902-1', 15.00, 'First', 2014, 369, 35, 4.4, 'An astronaut is stranded on Mars and must survive alone.');


-- Writes
INSERT INTO booklySchema.writes (book_id, author_id) VALUES
(1, 1), (2, 2), (3, 3), (4, 4), (5, 5), (6, 6), (7, 7),
(8, 8), (9, 9), (10, 10), (11, 11), (12, 12), (13, 13),
(14, 14), (15, 15), (16, 16), (17, 3), (18, 17),
(19, 18), (20, 19);


-- Category Belongs
INSERT INTO booklySchema.category_belongs (book_id, category_id) VALUES
(1, 7), (2, 7), (3, 1), (4, 2), (5, 3),
(6, 1), (7, 1), (8, 1), (9, 7), (10, 1),
(11, 3), (12, 7), (13, 2), (14, 3), (15, 9),
(16, 9), (17, 1), (18, 7), (19, 3), (20, 2);



-- Published By
INSERT INTO booklySchema.published_by (book_id, publisher_id) VALUES
(1, 1), (2, 2), (3, 3), (4, 4), (5, 5),
(6, 6), (7, 7), (8, 8), (9, 9), (10, 10),
(11, 11), (12, 12), (13, 13), (14, 14), (15, 15),
(16, 16), (17, 3), (18, 17), (19, 18), (20, 19);



-- Discounts
INSERT INTO booklySchema.discounts (code, discount_percentage, expired_date) VALUES
('NEWYEAR10', 10.0, '2025-12-31'),
('WELCOME15', 15.0, '2025-11-30'),
('HOLIDAY20', 20.0, '2025-10-15'),
('SPRING5', 5.0, '2025-09-01'),
('SUMMER25', 25.0, '2025-08-01');




-- Reviews
INSERT INTO booklySchema.reviews (user_id, book_id, comment, rating, number_of_likes, number_of_dislikes) VALUES
(1, 1, 'Task factor particularly against seek consider anyone set article poor.', 4, 58, 0),
(2, 2, 'Place full else security begin thousand the lay partner street.', 5, 14, 7),
(3, 3, 'Hospital network sell act evidence ready start.', 3, 67, 1),
(4, 4, 'Special claim trade now financial author.', 5, 76, 3),
(5, 5, 'Meeting nearly size beyond operation trade explain popular voice court think.', 3, 49, 1);


-- Replies
INSERT INTO booklySchema.reviews (user_id, book_id, comment, rating, number_of_likes, number_of_dislikes, parent_review_id) VALUES
(2, 1, 'Absolutely agree with your take!', 5, 12, 0, 1),
(3, 2, 'Interesting perspective, thanks for sharing.', 4, 6, 1, 2),
(4, 3, 'I had a similar reaction to this book.', 3, 9, 2, 3),
(5, 4, 'Could not disagree more, but that is the fun of reading!', 2, 3, 5, 4),
(1, 5, 'Spot on. The writing was exceptional.', 5, 18, 0, 5);

-- Additional reviews
INSERT INTO booklySchema.reviews (user_id, book_id, comment, rating, number_of_likes, number_of_dislikes) VALUES
(2, 6, 'A beautiful yet heartbreaking story that captures the essence of youth and mortality.', 5, 72, 2),
(3, 7, 'Stunningly written. The narrative voice is unforgettable and powerful.', 5, 84, 1),
(4, 8, 'A moving portrait of courage and change, steeped in historical context.', 4, 63, 4),
(5, 9, 'Dark and suspenseful, with twists that kept me guessing until the end.', 4, 47, 3),
(1, 10, 'Bleak yet profound. A quiet masterpiece about survival and love.', 5, 91, 0),
(2, 11, 'Visually rich and philosophically deep. A unique survival tale.', 4, 58, 2),
(3, 12, 'A deeply emotional read that stayed with me long after I finished.', 4, 41, 1),
(4, 13, 'Imaginative and intense, though a bit hard to follow at times.', 3, 28, 6),
(5, 14, 'An emotional journey. Raises big questions about faith and suffering.', 4, 53, 2),
(1, 15, 'The message is powerful, but the style was not to my taste.', 3, 33, 8),
(2, 16, 'Authentic, raw, and inspiring. A journey of self-discovery.', 4, 45, 3),
(3, 17, 'A beautifully painful story that sheds light on resilience and womanhood.', 5, 69, 1),
(4, 18, 'Twisty, tense, and fast-paced. A real page-turner!', 4, 51, 2),
(5, 19, 'Magical realism at its best. Dreamlike and haunting.', 5, 60, 0),
(1, 20, 'Technically detailed but highly entertaining. A brilliant blend of science and humor.', 5, 88, 1);


INSERT INTO booklySchema.book_image (book_id, image, image_type) VALUES
(1, pg_read_binary_file('/mnt/book-images/1.jpg'), 'image/jpeg'),
(2, pg_read_binary_file('/mnt/book-images/2.jpg'), 'image/jpeg'),
(3, pg_read_binary_file('/mnt/book-images/3.jpg'), 'image/jpeg'),
(4, pg_read_binary_file('/mnt/book-images/4.jpg'), 'image/jpeg'),
(5, pg_read_binary_file('/mnt/book-images/5.jpg'), 'image/jpeg'),
(6, pg_read_binary_file('/mnt/book-images/6.jpg'), 'image/jpeg'),
(7, pg_read_binary_file('/mnt/book-images/7.jpg'), 'image/jpeg'),
(8, pg_read_binary_file('/mnt/book-images/8.jpg'), 'image/jpeg'),
(9, pg_read_binary_file('/mnt/book-images/9.jpg'), 'image/jpeg'),
(10, pg_read_binary_file('/mnt/book-images/10.jpg'), 'image/jpeg'),
(11, pg_read_binary_file('/mnt/book-images/11.jpg'), 'image/jpeg'),
(12, pg_read_binary_file('/mnt/book-images/12.jpg'), 'image/jpeg'),
(13, pg_read_binary_file('/mnt/book-images/13.jpg'), 'image/jpeg'),
(14, pg_read_binary_file('/mnt/book-images/14.jpg'), 'image/jpeg'),
(15, pg_read_binary_file('/mnt/book-images/15.jpg'), 'image/jpeg'),
(16, pg_read_binary_file('/mnt/book-images/16.jpg'), 'image/jpeg'),
(17, pg_read_binary_file('/mnt/book-images/17.jpg'), 'image/jpeg'),
(18, pg_read_binary_file('/mnt/book-images/18.jpg'), 'image/jpeg'),
(19, pg_read_binary_file('/mnt/book-images/19.jpg'), 'image/jpeg'),
(20, pg_read_binary_file('/mnt/book-images/20.jpg'), 'image/jpeg');