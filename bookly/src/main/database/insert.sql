-- Authors
INSERT INTO booklySchema.authors (firstName, lastName, biography, nationality) VALUES
('Chimamanda', 'Ngozi Adichie', 'Nigerian writer known for novels blending themes of feminism, politics, and identity.', 'Nigerian'),
('Haruki', 'Murakami', 'A Japanese author famous for surreal, philosophical fiction exploring loneliness and time.', 'Japanese'),
('Isabel', 'Allende', 'Chilean-American writer whose works often focus on womens experiences and magical realism.', 'Chilean'),
('Ngũgĩ', 'wa Thiongo', 'Kenyan writer and academic known for his work on language, decolonization, and literature.', 'Kenyan'),
('Elif', 'Shafak', 'Turkish-British author whose novels explore culture, gender, and mysticism.', 'Turkish'),
('Orhan', 'Pamuk', 'Turkish novelist and Nobel laureate known for his reflections on Turkish identity and history.', 'Turkish'),
('Arundhati', 'Roy', 'Indian author and activist acclaimed for The God of Small Things and her political essays.', 'Indian'),
('Khaled', 'Hosseini', 'Afghan-American novelist best known for The Kite Runner and stories of exile and resilience.', 'Afghan'),
('Yaa', 'Gyasi', 'Ghanaian-American author recognized for her debut novel Homegoing.', 'Ghanaian'),
('Han', 'Kang', 'South Korean author awarded the Man Booker Prize for The Vegetarian.', 'South Korean'),
('Margaret', 'Atwood', 'Canadian author known for dystopian and speculative fiction.', 'Canadian'),
('Gabriel', 'García Márquez', 'Colombian novelist famous for magical realism.', 'Colombian'),
('Salman', 'Rushdie', 'British-Indian writer known for postcolonial and magical realist fiction.', 'British-Indian'),
('Toni', 'Morrison', 'American novelist renowned for her profound exploration of race and identity.', 'American'),
('Kazuo', 'Ishiguro', 'British author known for his deeply introspective and melancholic novels.', 'British'),
('J.K.', 'Rowling', 'British writer famous for the Harry Potter series.', 'British'),
('Chinua', 'Achebe', 'Nigerian novelist credited with pioneering modern African literature.', 'Nigerian'),
('George', 'Orwell', 'British author famous for dystopian novels like 1984 and Animal Farm.', 'British'),
('Leo', 'Tolstoy', 'Russian writer known for his epic novels War and Peace and Anna Karenina.', 'Russian'),
('Virginia', 'Woolf', 'English modernist author known for her stream-of-consciousness style.', 'English');

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
('Least weight company', 'Spanish', '978-1-68181-174-1', 28.38, 'Second', 2000, 869, 27, 3.9, 'Friend finally should data condition. Have important strong idea leave every. Civil possible buy accept how similar.'),
('I artist big feel', 'Chinese', '978-1-368-79420-6', 14.4, 'First', 1960, 874, 34, 3.5, 'Although knowledge any listen sense yet value. Behind cause political but. Reach run already international.'),
('Someone also ago', 'Arabic', '978-0-601-34019-4', 47.32, 'Second', 1992, 355, 24, 4.7, 'Purpose know rock contain with age. System suffer network city recent radio add floor. Company him land defense claim successful.'),
('Ready family', 'Spanish', '978-0-289-22596-7', 48.43, 'Second', 1974, 475, 28, 4.3, 'Successful break trip six trip example scene. Director trip different both become light. Glass local manage western provide media eight.'),
('Daughter mind', 'Chinese', '978-0-87501-706-8', 18.43, 'Deluxe', 2021, 402, 29, 3.3, 'Create Congress entire design. Hundred travel hot free road. Task compare product behavior good.'),
('Move hair church', 'Arabic', '978-0-576-96371-8', 35.54, 'Second', 1950, 737, 30, 3.3, 'Computer early affect soldier evidence. Despite way friend reality other live ready. Meeting technology second hear.'),
('Everyone show need their amount', 'Chinese', '978-0-11-793815-1', 32.65, 'Second', 1971, 981, 6, 4.8, 'Write media how why. Southern its easy rather. Race last whole ability interest late responsibility explain. Some majority research suggest by.'),
('Father talk his', 'Spanish', '978-0-89208-008-3', 48.81, 'Deluxe', 1966, 758, 40, 4.5, 'Him after avoid prove environment mind. Thus local party network. Go everything instead world hit fact.'),
('Month staff', 'English', '978-1-05-935394-1', 34.36, 'Revised', 1993, 660, 48, 3.7, 'Use expert population value enter church financial kind. Play doctor front however life idea process. Per discover yourself expect trade.'),
('Yeah foreign everybody', 'Chinese', '978-1-192-21172-6', 47.73, 'Deluxe', 2001, 899, 8, 4.6, 'Article Republican nearly lawyer. Figure how friend popular learn official near notice. Sister training attorney sea number scientist administration.'),
('The Handmaid''s Tale', 'English', '978-0-385-49081-8', 24.99, 'First', 1985, 311, 50, 4.6, 'A dystopian novel about a totalitarian society.'),
('One Hundred Years of Solitude', 'Spanish', '978-0-06-088328-7', 22.50, 'Second', 1967, 417, 40, 4.8, 'A generational saga of the Buendía family.'),
('Midnight''s Children', 'English', '978-0-394-53668-6', 19.99, 'Revised', 1981, 446, 30, 4.7, 'A novel blending magical realism with India''s history.'),
('Beloved', 'English', '978-0-394-53597-9', 18.50, 'First', 1987, 324, 25, 4.9, 'A haunting novel about slavery and memory.'),
('Never Let Me Go', 'English', '978-1-4000-7871-0', 16.75, 'Deluxe', 2005, 288, 35, 4.5, 'A speculative fiction novel exploring cloning.'),
('Harry Potter and the Sorcerer''s Stone', 'English', '978-0-590-35340-3', 39.99, 'First', 1997, 309, 100, 4.9, 'A young wizard embarks on a magical journey.'),
('Things Fall Apart', 'English', '978-0-385-47454-2', 14.99, 'Second', 1958, 209, 40, 4.8, 'A novel about Igbo society and colonialism.'),
('1984', 'English', '978-0-452-28423-4', 21.50, 'Revised', 1949, 328, 50, 4.9, 'A classic dystopian novel about surveillance.'),
('Anna Karenina', 'Russian', '978-0-679-42306-4', 25.75, 'Deluxe', 1877, 864, 30, 4.7, 'A tragic love story in imperial Russia.'),
('Mrs Dalloway', 'English', '978-0-15-662870-9', 14.25, 'Second', 1925, 194, 20, 4.5, 'A novel about one day in the life of Clarissa Dalloway.');

-- Writes
INSERT INTO booklySchema.writes (book_id, author_id) VALUES
(1, 1), (2, 2), (3, 3), (4, 4), (5, 5),
(6, 6), (7, 7), (8, 8), (9, 9), (10, 10),
(11, 11), (12, 12), (13, 13), (14, 14), (15, 15),
(16, 16), (17, 17), (18, 18), (19, 19), (20, 20);


-- Category Belongs
INSERT INTO booklySchema.category_belongs (book_id, category_id) VALUES
(1, 1), (2, 2), (3, 3), (4, 4), (5, 5),
(6, 6), (7, 7), (8, 8), (9, 9), (10, 10),
(11, 1), (12, 2), (13, 3), (14, 4), (15, 5),
(16, 6), (17, 7), (18, 8), (19, 9), (20, 10);


-- Published By
INSERT INTO booklySchema.published_by (book_id, publisher_id) VALUES
(1, 1), (2, 2), (3, 3), (4, 4), (5, 5),
(6, 6), (7, 7), (8, 8), (9, 9), (10, 10),
(11, 1), (12, 2), (13, 3), (14, 4), (15, 5),
(16, 6), (17, 7), (18, 8), (19, 9), (20, 10);


-- Discounts
INSERT INTO booklySchema.discounts (code, discount_percentage, expired_date) VALUES
('NEWYEAR10', 10.0, '2025-12-31'),
('WELCOME15', 15.0, '2025-11-30'),
('HOLIDAY20', 20.0, '2025-10-15'),
('SPRING5', 5.0, '2025-09-01'),
('SUMMER25', 25.0, '2025-08-01');


-- Orders
INSERT INTO booklySchema.orders (total_price, payment_method, address, shipment_code, status) VALUES
(135.76, 'credit_card', '123 Ocean Drive, Lisbon, Portugal', 'SHIP123', 'paid'),
(52.83, 'credit_card', '77 Sakura St, Kyoto, Japan', 'SHIP124', 'shipped'),
(119.70, 'credit_card', '456 Maple Avenue, Toronto, Canada', 'SHIP125', 'placed'),
(137.22, 'credit_card', '10 Rue de Rivoli, Paris, France', 'SHIP126', 'delivered'),
(39.13, 'credit_card', '891 Green Lane, Cape Town, South Africa', 'SHIP127', 'cancelled');


-- Shopping Cart
INSERT INTO booklySchema.shoppingcart (user_id, quantity, discount_id, order_id, total_price, shipment_method) VALUES
(1, 2, 1, 1, 135.76, 'in_person'),
(2, 3, 2, 2, 52.83, 'in_person'),
(3, 1, 3, 3, 119.70, 'in_person'),
(4, 3, 4, 4, 137.22, 'in_person'),
(5, 5, 5, 5, 39.13, 'credit_card');


-- Cart Contents
INSERT INTO booklySchema.contains (book_id, cart_id) VALUES
(1, 1), (2, 2), (3, 3), (4, 4), (5, 5),
(6, 1), (7, 2), (8, 3), (9, 4), (10, 5);


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