# Bookly 📚

Bookly is a complete backend and JSP-powered frontend system for managing an online bookstore. It provides:

- RESTful APIs for integration
- JSP & servlet-based frontend views
- Full CRUD for books, users, publishers, authors
- Order + wishlist + discount management
- JWT-based user authentication

---

## 📁 Project Structure

| Folder             | Description                                      |
|--------------------|--------------------------------------------------|
| `/rest`            | Jakarta REST endpoints (e.g., `/books`, `/wishlist`) |
| `/servlet`         | JSP-based MVC controller servlets (e.g., `UserServlet`) |
| `/dao`             | DAO layer with PostgreSQL queries                |
| `/resources`       | Java models for User, Book, Author, etc.         |
| `/jsp`             | JSP pages used for views                         |
| `/html`            | Static error/success HTML pages                  |
| `/sql`             | Contains `database.sql` and `insert.sql` files   |
| `/static/img/book` | Book cover images stored in the host file system |

---

## 🚀 Quick Setup

```bash
git clone https://github.com/your-org/bookly.git
cd bookly
```
1. Add Book Cover Images
Place your .jpg book cover images inside
```bash 
/src/main/webapp/static/img/book/
```
Files should be named 1.jpg, 2.jpg, ..., 20.jpg to match book IDs.

2. after that you have to run the docker
```bash
   mvn package
   docker compose up --build
```

2.1. For login to profile page use this username and password
```bash
   username: 'smithchristine'
   password: '(6dR#aSJ$E'
```

2.2. For login to admin page use this username and password
```bash
   username: 'esmith'
   password: 'vUU@3MdB7b'
```

3. The WAR file will be generated under `target/bookly.war`.

4. Deploy this WAR file to your servlet container (Tomcat, Jetty, etc.).

5. Access via:
   ```bash
   http://localhost:8080/bookly/api/...
   ```

Example endpoints:
- `POST /auth/login`
- `POST /users`
- `GET /books`
- `POST /order`
- `GET /wishlist`

5. Package and deploy to your preferred servlet container (Tomcat, Jetty, etc.).

---

####  🧠 Notes on Image Handling
- Images are stored as binary (BYTEA) in the book_image table.
- Docker volume mapping

```bash
 ./src/main/webapp/static/img/book:/mnt/book-images
```
- SQL uses
```bash
pg_read_binary_file('/mnt/book-images/1.jpg')
```

Ensure the .jpg files exist before launching docker compose up.

#### 🔧 Configuration

- `Java 17+`
- `Maven 3.6+`
- `PostgreSQL 14+`
- `Tomcat 9+` or any Jakarta-compatible servlet container
- JDBC and ORM dependencies included via Maven

---

#### 📦 Dependencies

Main dependencies include:
- `postgresql` (JDBC driver)
- `jakarta.servlet-api`
- `com.fasterxml.jackson.core:jackson-databind`
- `io.jsonwebtoken:jjwt`
- `org.junit.jupiter:junit-jupiter`
- `org.slf4j:slf4j-api`

All dependencies are declared in `pom.xml`.

---


#### 🚀 Deployment instructions

1. Clean and build the project with:
   ```bash
   mvn clean package
   ```
2. Run the Docker with:
   ```bash
   docker compose up --build
   ```


#### 💻 Member of Team
- Ayoub Ben Yamoune
- Amirreza Jolani Mameghani
- Md Abu Raihan Mia
- Parisa Saeedidana
- Adam Truty


