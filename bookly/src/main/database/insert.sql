INSERT INTO booklySchema.authors (first_name, last_name, biography, nationality) VALUES
('Chimamanda', 'Ngozi Adichie', 'Nigerian writer known for novels blending themes of feminism, politics, and identity.', 'Nigerian'),
('Haruki', 'Murakami', 'A Japanese author famous for surreal, philosophical fiction exploring loneliness and time.', 'Japanese'),
('Isabel', 'Allende', 'Chilean-American writer whose works often focus on womens experiences and magical realism.', 'Chilean'),
('Ngũgĩ', 'wa Thiongo', 'Kenyan writer and academic known for his work on language, decolonization, and literature.', 'Kenyan'),
('Elif', 'Shafak', 'Turkish-British author whose novels explore culture, gender, and mysticism.', 'Turkish'),
('Orhan', 'Pamuk', 'Turkish novelist and Nobel laureate known for his reflections on Turkish identity and history.', 'Turkish'),
('Arundhati', 'Roy', 'Indian author and activist acclaimed for The God of Small Things and her political essays.', 'Indian'),
('Khaled', 'Hosseini', 'Afghan-American novelist best known for The Kite Runner and stories of exile and resilience.', 'Afghan'),
('Yaa', 'Gyasi', 'Ghanaian-American author recognized for her debut novel Homegoing.', 'Ghanaian'),
('Han', 'Kang', 'South Korean author awarded the Man Booker Prize for The Vegetarian.', 'South Korean');

INSERT INTO booklySchema.users (username, password, first_name, last_name, email, phone, address, role) VALUES
('esmith', 'vUU@3MdB7b', 'Phillip', 'Akhtar', 'ypritchard@yahoo.co.uk', '01514960872', 'Flat 09, Terry centers, New Lesleyhaven, G0J 2DZ', 'admin'),
('smithchristine', '(6dR#aSJ$E', 'Elizabeth', 'Stewart', 'cheryl80@hotmail.com', '+1-412-411-8244', '48740 Cynthia Village Suite 005, Lake Tina, WA 58413', 'user'),
('bensimpson', 's)$KQ8Dvgm', 'Dr', 'Morris', 'stewart33@gmail.com', '01174960654', 'Flat 86R, Singh crescent, Antonyshire, BD1 5PS', 'user'),
('margot65', '6dU6a!hs*J', 'Henri', 'Guillou', 'marc69@dbmail.com', '0473872148', 'chemin Jean, 20037 Lecoq-sur-Legrand', 'user'),
('jcontreras', 'ZO8NGbtf(8', 'Barbara', 'Anderson', 'matthew81@yahoo.com', '0823166587', '669 Melissa Wade, Woodsstad, SA, 2986', 'user'),
('brentgray', 'kT_Ra9ZdJO', 'Jessica', 'Camacho', 'howelljohn@yahoo.com.au', '93745299', '19 Sarah River, Sandersborough, NSW, 6314', 'admin'),
('laetitia87', 'SD9LELAp%B', 'Raymond', 'Gilles', 'jules73@live.com', '0154948083', '97, avenue Imbert, 70143 Jacquot-sur-Mer', 'admin'),
('oswingiess', 'Y+&1Y+IgM#', 'Berend', 'Gude-Hübel', 'hans-erich04@yahoo.de', '+4901671902294', 'Brita-Wilmsen-Straße 86, 74964 Schmölln', 'user'),
('neelofarborde', '#bHi#&Nw5q', 'Aarna', 'Bhandari', 'fkanda@hotmail.com', '05940139904', '87, Chana Road, Pallavaram 756551', 'admin'),
('wpires', '(yOGpIBk6G', 'Manuela', 'Moreira', 'ceciliagoncalves@bol.com.br', '+55816773-7826', 'Conjunto de Campos, 66, João Pinheiro, 04499-727 Monteiro de Melo / PI', 'admin');

INSERT INTO booklySchema.publishers (publisher_name, phone, address) VALUES
('Gandhi, Virk and Bedi', '01870262174', '15/865, Viswanathan Chowk, Tadipatri 343161'),
('Hebert S.A.', '0300504556', '369, rue Chevalier, 93792 Pinto'),
('Ramos', '+55610748-2175', 'Vila Luiz Fernando Ramos, 73, Barro Preto, 13695-944 Novaes / GO'),
('Armstrong Ltd', '+443069990097', '533 Davies stravenue, Lake Lewis, RM0M 0YQ'),
('Clarke, Walker and Rose', '+4401414960232', 'Flat 47, Morris junction, South Keithberg, S85 6AL'),
('银嘉信息有限公司', '15649651370', '江西省佛山县朝阳长春街F座 813826'),
('Davies-Reid', '+441914960692', '96 Rhodes land, South Louise, N15 0RL'),
('Costa Ramos S/A', '+55119005-3293', 'Favela de da Rosa, 35, Vila Suzana Primeira Seção, 42284210 Alves / MS'),
('Martins Aragão S.A.', '+55710268-1177', 'Colônia de Porto, 139, Vila Oeste, 70076-617 Cardoso de Cardoso / MT'),
('Finke', '+4902499856984', 'Ljudmila-Trapp-Gasse 67, 65766 Heinsberg');

INSERT INTO booklySchema.categories (category_name, description) VALUES
('Fiction', 'Narrative literary works from all cultures.'),
('Science Fiction', 'Futuristic stories exploring science and technology.'),
('Fantasy', 'Imaginative tales involving magic and myth.'),
('Biography', 'Detailed descriptions of a person"s life.'),
('Classic', 'Timeless literature recognized worldwide.'),
('Drama', 'Emotionally driven narratives often performed.'),
('Mystery', 'Suspense-filled investigations and puzzles.'),
('Romance', 'Stories centered on love and relationships.'),
('Self-help', 'Guides for personal growth and improvement.'),
('History', 'Accounts of significant past events and cultures.');

INSERT INTO booklySchema.books (
    title, language, isbn, price, edition, publication_year,
    number_of_pages, stock_quantity, average_rate, summary
) VALUES
('Least weight company', 'Spanish', '978-1-68181-174-1', 28.38, 'Second', 2000, 869, 27, 3.9, 'Friend finally should data condition.
Have important strong idea leave every. Civil possible buy accept how similar.'),
('I artist big feel', 'Chinese', '978-1-368-79420-6', 14.4, 'First', 1960, 874, 34, 3.5, 'Although knowledge any listen sense yet value. Behind cause political but. Reach run already international.'),
('Someone also ago', 'Arabic', '978-0-601-34019-4', 47.32, 'Second', 1992, 355, 24, 4.7, 'Purpose know rock contain with age. System suffer network city recent radio add floor. Company him land defense claim successful.'),
('Ready family', 'Spanish', '978-0-289-22596-7', 48.43, 'Second', 1974, 475, 28, 4.3, 'Successful break trip six trip example scene. Director trip different both become light.
Glass local manage western provide media eight.'),
('Daughter mind', 'Chinese', '978-0-87501-706-8', 18.43, 'Deluxe', 2021, 402, 29, 3.3, 'Create Congress entire design. Hundred travel hot free road. Task compare product behavior good.'),
('Move hair church', 'Arabic', '978-0-576-96371-8', 35.54, 'Second', 1950, 737, 30, 3.3, 'Computer early affect soldier evidence. Despite way friend reality other live ready. Meeting technology second hear.'),
('Everyone show need their amount', 'Chinese', '978-0-11-793815-1', 32.65, 'Second', 1971, 981, 6, 4.8, 'Write media how why. Southern its easy rather. Race last whole ability interest late responsibility explain. Some majority research suggest by.'),
('Father talk his', 'Spanish', '978-0-89208-008-3', 48.81, 'Deluxe', 1966, 758, 40, 4.5, 'Him after avoid prove environment mind. Thus local party network. Go everything instead world hit fact.'),
('Month staff', 'English', '978-1-05-935394-1', 34.36, 'Revised', 1993, 660, 48, 3.7, 'Use expert population value enter church financial kind. Play doctor front however life idea process. Per discover yourself expect trade.'),
('Yeah foreign everybody', 'Chinese', '978-1-192-21172-6', 47.73, 'Deluxe', 2001, 899, 8, 4.6, 'Article Republican nearly lawyer. Figure how friend popular learn official near notice.
Sister training attorney sea number scientist administration.');

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

INSERT INTO booklySchema.category_belongs (book_id, category_id) VALUES
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

INSERT INTO booklySchema.discounts (code, discount_percentage, expired_date) VALUES
('NEWYEAR10', 10.0, '2025-12-31'),
('WELCOME15', 15.0, '2025-11-30'),
('HOLIDAY20', 20.0, '2025-10-15'),
('SPRING5', 5.0, '2025-09-01'),
('SUMMER25', 25.0, '2025-08-01');

INSERT INTO booklySchema.orders (total_amount, payment_method) VALUES
(135.76, 'credit_card'),
(52.83, 'credit_card'),
(119.7, 'credit_card'),
(137.22, 'credit_card'),
(39.13, 'credit_card');

INSERT INTO booklySchema.shoppingcart (user_id, quantity, discount_id, order_id, shipment_method) VALUES
(1, 2, 1, 1, 'in_person'),
(2, 3, 2, 2, 'in_person'),
(3, 1, 3, 3, 'in_person'),
(4, 3, 4, 4, 'in_person'),
(5, 5, 5, 5, 'credit_card');

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

INSERT INTO booklySchema.reviews (user_id, book_id, review_text, rating, number_of_likes, number_of_dislikes) VALUES
(1, 1, 'Task factor particularly against seek consider anyone set article poor.', 4, 58, 0),
(2, 2, 'Place full else security begin thousand the lay partner street.', 5, 14, 7),
(3, 3, 'Hospital network sell act evidence ready start.', 3, 67, 1),
(4, 4, 'Special claim trade now financial author.', 5, 76, 3),
(5, 5, 'Meeting nearly size beyond operation trade explain popular voice court think.', 3, 49, 1);