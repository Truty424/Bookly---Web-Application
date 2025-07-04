package it.unipd.bookly.servlet.admin;

import it.unipd.bookly.Resource.*;
import it.unipd.bookly.dao.author.*;
import it.unipd.bookly.dao.book.*;
import it.unipd.bookly.dao.publisher.*;
import it.unipd.bookly.dao.discount.*;
import it.unipd.bookly.servlet.AbstractDatabaseServlet;
import it.unipd.bookly.utilities.ServletUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Timestamp;

import it.unipd.bookly.LogContext;
import it.unipd.bookly.utilities.ErrorCode;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.Part;

@MultipartConfig
@WebServlet(name = "AdminServlet", value = "/admin/*")
public class AdminServlet extends AbstractDatabaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction("AdminServlet");

        String operation = req.getPathInfo();
        LOGGER.info("GET operation: {}", operation);

        try {
            switch (operation) {
                case "/dashboard" ->
                    req.getRequestDispatcher("/jsp/admin/dashboard.jsp").forward(req, res);
                case "/addBook" ->
                    req.getRequestDispatcher("/jsp/admin/addBook.jsp").forward(req, res);
                case "/addAuthor" -> {
                    try (var con = getConnection()) {
                        var allBooks = new GetAllBooksDAO(con).access().getOutputParam();
                        req.setAttribute("all_books", allBooks);
                        LOGGER.info("Number of books fetched: {}", allBooks.size());
                    }
                    req.getRequestDispatcher("/jsp/admin/addAuthor.jsp").forward(req, res);
                }
                case "/addPublisher" -> {
                    try (var con = getConnection()) {
                        var allBooks = new GetAllBooksDAO(con).access().getOutputParam();
                        req.setAttribute("all_books", allBooks);
                        LOGGER.info("Number of books fetched: {}", allBooks.size());
                    }
                    req.getRequestDispatcher("/jsp/admin/addPublisher.jsp").forward(req, res);
                }
                case "/addDiscount" ->
                    req.getRequestDispatcher("/jsp/admin/addDiscount.jsp").forward(req, res);
                default ->
                    writeError(res, ErrorCode.OPERATION_UNKNOWN);
            }
        } catch (Exception e) {
            LOGGER.error("Exception during GET: ", e);
            writeError(res, ErrorCode.INTERNAL_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null || !"admin".equalsIgnoreCase(user.getRole())) {
            resp.sendRedirect(req.getContextPath() + "/user/login");
            return;
        }

        try {
            if (path == null) {
                ServletUtils.redirectToErrorPage(req, resp, "Missing admin action path.");
                return;
            }

            switch (path) {
                case "/addBook" ->
                    handleAddBook(req);
                case "/updateBook" ->
                    handleUpdateBook(req);
                case "/deleteBook" ->
                    handleDeleteBook(req);
                case "/addAuthor" ->
                    handleAddAuthor(req);
                case "/updateAuthor" ->
                    handleUpdateAuthor(req);
                case "/deleteAuthor" ->
                    handleDeleteAuthor(req);
                case "/addPublisher" ->
                    handleAddPublisher(req);
                case "/updatePublisher" ->
                    handleUpdatePublisher(req);
                case "/deletePublisher" ->
                    handleDeletePublisher(req);
                case "/addDiscount" ->
                    handleAddDiscount(req);
                case "/deleteDiscount" ->
                    handleDeleteDiscount(req);
                default ->
                    ServletUtils.redirectToErrorPage(req, resp, "Unsupported admin action: " + path);
            }

            resp.sendRedirect(req.getContextPath() + "/admin/dashboard");

        } catch (Exception e) {
            LOGGER.error("AdminServlet error: {}", e.getMessage(), e);
            ServletUtils.redirectToErrorPage(req, resp, "AdminServlet error: " + e.getMessage());
        }
    }

    // ========================== BOOK ==========================
    private void handleAddBook(HttpServletRequest req) throws Exception {
        Image image;
        Book book;
        try (var con = getConnection()) {
            book = new Book(
                    0,
                    req.getParameter("title"),
                    req.getParameter("language"),
                    req.getParameter("isbn"),
                    Double.parseDouble(req.getParameter("price")),
                    req.getParameter("edition"),
                    Integer.parseInt(req.getParameter("publication_year")),
                    Integer.parseInt(req.getParameter("number_of_pages")),
                    Integer.parseInt(req.getParameter("stock_quantity")),
                    0.0,
                    req.getParameter("summary")
            );

            new InsertBookDAO(con, book).access();
            LOGGER.info("Book '{}' added successfully.", book.getTitle());
        }
        // 3. Check if image was uploaded
        Part imagePart = req.getPart("image");
        try (var con = getConnection()) {
            if (imagePart != null && imagePart.getSize() > 0) {
                int bookId = book.getBookId();
                byte[] imageBytes = imagePart.getInputStream().readAllBytes();
                String contentType = imagePart.getContentType();
                image = new Image(imageBytes, contentType);

                // 4. Insert image for the book
                new InsertBookImageDAO(con, bookId, image).access();
                LOGGER.info("Image uploaded for book ID {}", bookId);
            }
        }
    }

    private void handleUpdateBook(HttpServletRequest req) throws Exception {
        try (var con = getConnection()) {
            int bookId = Integer.parseInt(req.getParameter("book_id"));

            // Fetch other fields
            String title = req.getParameter("title");
            String language = req.getParameter("language");
            String isbn = req.getParameter("isbn");
            double price = Double.parseDouble(req.getParameter("price"));
            String edition = req.getParameter("edition");
            int publicationYear = Integer.parseInt(req.getParameter("publication_year"));
            int numberOfPages = Integer.parseInt(req.getParameter("number_of_pages"));
            int stockQuantity = Integer.parseInt(req.getParameter("stock_quantity"));
            double averageRate = Double.parseDouble(req.getParameter("average_rate"));
            String summary = req.getParameter("summary");

            // Handle optional image (if any)
            Image image = null;
            var imagePart = req.getPart("image");  // Assumes form has <input type="file" name="image">
            if (imagePart != null && imagePart.getSize() > 0) {
                byte[] imageBytes = imagePart.getInputStream().readAllBytes();
                String imageType = imagePart.getContentType();
                image = new Image(imageBytes, imageType);
                LOGGER.info("Received image for book ID {} ({} bytes, type: {})", bookId, imageBytes.length, imageType);
            }

            new UpdateBookDAO(
                    con,
                    bookId,
                    title,
                    language,
                    isbn,
                    price,
                    edition,
                    publicationYear,
                    numberOfPages,
                    stockQuantity,
                    averageRate,
                    summary,
                    image
            ).access();

            LOGGER.info("Book ID {} updated successfully.", bookId);
        }
    }

    private void handleDeleteBook(HttpServletRequest req) throws Exception {
        try (var con = getConnection()) {
            int bookId = Integer.parseInt(req.getParameter("book_id"));
            new DeleteBookDAO(con, bookId).access();
            LOGGER.info("Book ID {} deleted.", bookId);
        }
    }

    // ========================== AUTHOR ==========================
    private void handleAddAuthor(HttpServletRequest req) throws Exception {
        String[] bookIds = req.getParameterValues("bookIds");
        Author author;

        int authorId;

        // Step 1: Insert the author and retrieve generated ID
        try (var con = getConnection()) {
            author = new Author(
                    req.getParameter("firstName"),
                    req.getParameter("lastName"),
                    req.getParameter("biography"),
                    req.getParameter("nationality")
            );
            new InsertAuthorDAO(con, author).access();
            authorId = author.getAuthorId(); // ✅ Now populated
            LOGGER.info("Author '{} {}' inserted with ID {}", author.getFirstName(), author.getLastName(), authorId);
        }

        // Step 2: Link the author to books, if any
        if (bookIds != null && bookIds.length > 0) {
            for (String bookIdStr : bookIds) {
                try (Connection con = getConnection()) {
                    int bookId = Integer.parseInt(bookIdStr);
                    new AddAuthorToBookDAO(con, bookId, authorId).access();
                } catch (NumberFormatException e) {
                    LOGGER.warn("Invalid book ID '{}', skipping...", bookIdStr);
                }
            }
        }
    }

    private void handleUpdateAuthor(HttpServletRequest req) throws Exception {
        try (var con = getConnection()) {
            Author author = new Author(
                    req.getParameter("firstName"),
                    req.getParameter("lastName"),
                    req.getParameter("biography"),
                    req.getParameter("nationality")
            );
            author.setAuthor_id(Integer.parseInt(req.getParameter("author_id")));
            new UpdateAuthorDAO(con, author).access();
            LOGGER.info("Author ID {} updated.", author.getAuthorId());
        }
    }

    private void handleDeleteAuthor(HttpServletRequest req) throws Exception {
        try (var con = getConnection()) {
            int authorId = Integer.parseInt(req.getParameter("author_id"));
            new DeleteAuthorDAO(con, authorId).access();
            LOGGER.info("Author ID {} deleted.", authorId);
        }
    }

    // ========================== PUBLISHER ==========================
    private void handleAddPublisher(HttpServletRequest req) throws Exception {
        String[] bookIds = req.getParameterValues("bookIds");
        Publisher publisher;
        int publisherId;
        try (var con = getConnection()) {
            publisher = new Publisher(
                    req.getParameter("publisher_name"),
                    req.getParameter("phone"),
                    req.getParameter("address")
            );

            // Insert and populate the generated ID
            new InsertPublisherDAO(con, publisher).access();  // sets publisherId inside
            publisherId = publisher.getPublisherId();     // now has real ID
        }

        if (bookIds != null && bookIds.length > 0) {
            for (String bookIdStr : bookIds) {
                try (Connection con = getConnection()) {
                    int bookId = Integer.parseInt(bookIdStr);
                    new AddPublisherToBookDAO(con, bookId, publisherId).access();
                } catch (NumberFormatException e) {
                    LOGGER.warn("Invalid book ID '{}', skipping...", bookIdStr);
                }
            }
        }
    }

    private void handleUpdatePublisher(HttpServletRequest req) throws Exception {
        try (var con = getConnection()) {
            Publisher publisher = new Publisher(
                    req.getParameter("publisher_name"),
                    req.getParameter("phone"),
                    req.getParameter("address")
            );
            publisher.setPublisherId(Integer.parseInt(req.getParameter("publisher_id")));
            new UpdatePublisherDAO(con, publisher).access();
            LOGGER.info("Publisher ID {} updated.", publisher.getPublisherId());
        }
    }

    private void handleDeletePublisher(HttpServletRequest req) throws Exception {
        try (var con = getConnection()) {
            int publisherId = Integer.parseInt(req.getParameter("publisher_id"));
            new DeletePublisherDAO(con, publisherId).access();
            LOGGER.info("Publisher ID {} deleted.", publisherId);
        }
    }

    // ========================== DISCOUNT ==========================
    private void handleAddDiscount(HttpServletRequest req) throws Exception {
        try (var con = getConnection()) {
            Discount discount = new Discount();
            discount.setCode(req.getParameter("code"));
            discount.setDiscountPercentage(Double.parseDouble(req.getParameter("percentage")));
            discount.setExpiredDate(Timestamp.valueOf(req.getParameter("expiredDate") + " 00:00:00"));
            new InsertDiscountDAO(con, discount).access();
            LOGGER.info("Discount '{}' added.", discount.getCode());
        }
    }

    private void handleDeleteDiscount(HttpServletRequest req) throws Exception {
        try (var con = getConnection()) {
            int discountId = Integer.parseInt(req.getParameter("discount_id"));
            new DeleteDiscountDAO(con, discountId).access();
            LOGGER.info("Discount ID {} deleted.", discountId);
        }
    }
}
