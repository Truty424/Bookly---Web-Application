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
import java.sql.Timestamp;

@WebServlet(name = "AdminServlet", value = "/admin/*")
public class AdminServlet extends AbstractDatabaseServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        // ✅ Check if user is admin
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
                case "/addBook" -> handleAddBook(req);
                case "/updateBook" -> handleUpdateBook(req);
                case "/deleteBook" -> handleDeleteBook(req);
                case "/addAuthor" -> handleAddAuthor(req);
                case "/updateAuthor" -> handleUpdateAuthor(req);
                case "/deleteAuthor" -> handleDeleteAuthor(req);
                case "/addPublisher" -> handleAddPublisher(req);
                case "/updatePublisher" -> handleUpdatePublisher(req);
                case "/deletePublisher" -> handleDeletePublisher(req);
                case "/addDiscount" -> handleAddDiscount(req);
                case "/deleteDiscount" -> handleDeleteDiscount(req);
                default -> ServletUtils.redirectToErrorPage(req, resp, "Unsupported admin action: " + path);
            }

            // ✅ Success: redirect back to dashboard
            resp.sendRedirect(req.getContextPath() + "/admin/dashboard");

        } catch (Exception e) {
            LOGGER.error("AdminServlet error: {}", e.getMessage(), e);
            ServletUtils.redirectToErrorPage(req, resp, "AdminServlet error: " + e.getMessage());
        }
    }

    // ========== BOOK ==========
    private void handleAddBook(HttpServletRequest req) throws Exception {
        try (var con = getConnection()) {
            Book book = new Book(
                    Integer.parseInt(req.getParameter("book_id")),
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
        }
    }

    private void handleUpdateBook(HttpServletRequest req) throws Exception {
        try (var con = getConnection()) {
            int bookId = Integer.parseInt(req.getParameter("book_id"));
            new UpdateBookDAO(
                    con,
                    bookId,
                    req.getParameter("title"),
                    req.getParameter("language"),
                    req.getParameter("isbn"),
                    Double.parseDouble(req.getParameter("price")),
                    req.getParameter("edition"),
                    Integer.parseInt(req.getParameter("publication_year")),
                    Integer.parseInt(req.getParameter("number_of_pages")),
                    Integer.parseInt(req.getParameter("stock_quantity")),
                    Double.parseDouble(req.getParameter("average_rate")),
                    req.getParameter("summary")
            ).access();
        }
    }

    private void handleDeleteBook(HttpServletRequest req) throws Exception {
        try (var con = getConnection()) {
            int bookId = Integer.parseInt(req.getParameter("book_id"));
            new DeleteBookDAO(con, bookId).access();
        }
    }

    // ========== AUTHOR ==========
    private void handleAddAuthor(HttpServletRequest req) throws Exception {
        try (var con = getConnection()) {
            Author author = new Author(
                    req.getParameter("first_name"),
                    req.getParameter("last_name"),
                    req.getParameter("biography"),
                    req.getParameter("nationality")
            );
            new InsertAuthorDAO(con, author).access();
        }
    }

    private void handleUpdateAuthor(HttpServletRequest req) throws Exception {
        try (var con = getConnection()) {
            Author author = new Author(
                    req.getParameter("first_name"),
                    req.getParameter("last_name"),
                    req.getParameter("biography"),
                    req.getParameter("nationality")
            );
            author.setAuthor_id(Integer.parseInt(req.getParameter("author_id")));
            new UpdateAuthorDAO(con, author).access();
        }
    }

    private void handleDeleteAuthor(HttpServletRequest req) throws Exception {
        try (var con = getConnection()) {
            int authorId = Integer.parseInt(req.getParameter("author_id"));
            new DeleteAuthorDAO(con, authorId).access();
        }
    }

    // ========== PUBLISHER ==========
    private void handleAddPublisher(HttpServletRequest req) throws Exception {
        try (var con = getConnection()) {
            Publisher publisher = new Publisher(
                    req.getParameter("publisher_name"),
                    req.getParameter("phone"),
                    req.getParameter("address")
            );
            new InsertPublisherDAO(con, publisher).access();
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
        }
    }

    private void handleDeletePublisher(HttpServletRequest req) throws Exception {
        try (var con = getConnection()) {
            int publisherId = Integer.parseInt(req.getParameter("publisher_id"));
            new DeletePublisherDAO(con, publisherId).access();
        }
    }

    // ========== DISCOUNT ==========
    private void handleAddDiscount(HttpServletRequest req) throws Exception {
        try (var con = getConnection()) {
            Discount discount = new Discount();
            discount.setCode(req.getParameter("code"));
            discount.setDiscountPercentage(Double.parseDouble(req.getParameter("percentage")));
            discount.setExpiredDate(Timestamp.valueOf(req.getParameter("expiredDate") + " 00:00:00"));
            new InsertDiscountDAO(con, discount).access();
        }
    }

    private void handleDeleteDiscount(HttpServletRequest req) throws Exception {
        try (var con = getConnection()) {
            int discountId = Integer.parseInt(req.getParameter("discount_id"));
            new DeleteDiscountDAO(con, discountId).access();
        }
    }
}
