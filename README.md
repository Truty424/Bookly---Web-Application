# README #

Welcome to **Bookly**, a complete backend solution for managing an online bookstore. This project provides a RESTful API built with Java, following a layered architecture using DAO patterns, and is fully integrated with a PostgreSQL database.

---

### What is this repository for? ###

* **Quick summary:**
  Bookly is designed to handle the backend operations of an online bookstore. It covers user registration, book inventory, author/publisher relationships, orders, reviews, discounts, cart, and wishlist functionalities.

* **Version:** `1.0.0`

* **Main Technologies:**
  - Java 17+
  - Jakarta EE (Servlets)
  - PostgreSQL
  - Jackson (JSON)
  - JWT for authentication
  - JUnit 5 for testing

* **API Style:** RESTful, JSON-based

* [Learn Markdown](https://bitbucket.org/tutorials/markdowndemo)

---

### How do I get set up? ###

#### ✅ Summary of set up

1. Clone the repository:
   ```bash
   git clone https://your-repo-url/bookly.git
   cd bookly
   ```

2. Set up PostgreSQL with the `bookly` schema and tables. (SQL schema file should be provided or generated.)

3. Update your DB credentials inside DAO or through environment variables.

4. Compile and run tests:
   ```bash
   mvn clean test
   ```

5. Package and deploy to your preferred servlet container (Tomcat, Jetty, etc.).

---

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

#### 🛠️ Database configuration

Make sure PostgreSQL is running and a database named `bookly` exists:

```sql
CREATE DATABASE bookly;
```

Basic tables (example):
- `users`
- `book`
- `author`
- `publisher`
- `wishlist`
- `orders`
- `discounts`
- `reviews`
- `cart`

You can configure database credentials in DAO classes or inject via system properties.

---

#### 🧪 How to run tests

Tests are located under `src/test/java`.

```bash
# Run all tests
mvn clean test
```

Tests include:
- DAO layer integration tests
- Data creation and teardown using `@BeforeEach` and `@AfterEach`
- Assertions on insert/update/delete operations

Example:
```java
assertEquals("Book Title", book.getTitle());
assertTrue(bookList.size() > 0);
```

---

#### 🚀 Deployment instructions

1. Build the project with:
   ```bash
   mvn clean package
   ```

2. The WAR file will be generated under `target/bookly.war`.

3. Deploy this WAR file to your servlet container (Tomcat, Jetty, etc.).

4. Access via:
   ```
   http://localhost:8080/bookly/api/...
   ```

Example endpoints:
- `POST /auth/login`
- `POST /users`
- `GET /books`
- `POST /order`
- `GET /wishlist/user/{id}`

---

### Contribution guidelines ###

#### ✅ Writing tests

- Write JUnit 5-based tests in `src/test/java`.
- Use mock data or temporary inserts.
- Clean up test data after each run.

