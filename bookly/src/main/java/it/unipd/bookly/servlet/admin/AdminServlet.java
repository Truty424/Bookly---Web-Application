package it.unipd.bookly.servlet.admin;

import it.unipd.bookly.Resource.*;
import it.unipd.bookly.dao.author.*;
import it.unipd.bookly.dao.book.*;
import it.unipd.bookly.dao.publisher.*;
import it.unipd.bookly.dao.discount.*;
import it.unipd.bookly.servlet.AbstractDatabaseServlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Timestamp;

@WebServlet(name = "AdminServlet", value = "/admin/*")
public class AdminServlet extends AbstractDatabaseServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();

        try {
            if (path == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing action.");
                return;
            }

            switch (path) {
                // Book Operations
                case "/addBook" -> handleAddBook(req, resp);
                case "/updateBook" -> handleUpdateBook(req, resp);
                case "/deleteBook" -> handleDeleteBook(req, resp);

                // Author Operations
                case "/addAuthor" -> handleAddAuthor(req, resp);
                case "/updateAuthor" -> handleUpdateAuthor(req, resp);
                case "/deleteAuthor" -> handleDeleteAuthor(req, resp);

                // Publisher Operations
                case "/addPublisher" -> handleAddPublisher(req, resp);
                case "/updatePublisher" -> handleUpdatePublisher(req, resp);
                case "/deletePublisher" -> handleDeletePublisher(req, resp);

                // Discount Operations
                case "/addDiscount" -> handleAddDiscount(req, resp);
                case "/deleteDiscount" -> handleDeleteDiscount(req, resp);

                default -> resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Unsupported action.");
            }

        } catch (Exception e) {
            LOGGER.error("AdminServlet error: {}", e.getMessage());
            resp.sendRedirect("/html/error.html");
        }
    }

    // ========================= BOOK =========================
    private void handleAddBook(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Book book = new Book(
                req.getParameter("book_id") != null ? Integer.parseInt(req.getParameter("book_id")) : null,
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
        new InsertBookDAO(getConnection(), book).access();
        resp.sendRedirect("/admin/dashboard.jsp");
    }

    private void handleUpdateBook(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        int bookId = Integer.parseInt(req.getParameter("book_id"));
        new UpdateBookDAO(
                getConnection(),
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
        resp.sendRedirect("/admin/dashboard.jsp");
    }

    private void handleDeleteBook(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        int bookId = Integer.parseInt(req.getParameter("book_id"));
        new DeleteBookDAO(getConnection(), bookId).access();
        resp.sendRedirect("/admin/dashboard.jsp");
    }

    // ========================= AUTHOR =========================
    private void handleAddAuthor(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Author author = new Author(
                req.getParameter("first_name"),
                req.getParameter("last_name"),
                req.getParameter("biography"),
                req.getParameter("nationality")
        );
        new InsertAuthorDAO(getConnection(), author).access();
        resp.sendRedirect("/admin/dashboard.jsp");
    }

    private void handleUpdateAuthor(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Author author = new Author(
                req.getParameter("first_name"),
                req.getParameter("last_name"),
                req.getParameter("biography"),
                req.getParameter("nationality")
        );
        author.setAuthor_id(Integer.parseInt(req.getParameter("author_id")));
        new UpdateAuthorDAO(getConnection(), author).access();
        resp.sendRedirect("/admin/dashboard.jsp");
    }

    private void handleDeleteAuthor(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        int authorId = Integer.parseInt(req.getParameter("author_id"));
        new DeleteAuthorDAO(getConnection(), authorId).access();
        resp.sendRedirect("/admin/dashboard.jsp");
    }

    // ========================= PUBLISHER =========================
    private void handleAddPublisher(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Publisher publisher = new Publisher(
                req.getParameter("publisher_name"),
                req.getParameter("phone"),
                req.getParameter("address")
        );
        new InsertPublisherDAO(getConnection(), publisher).access();
        resp.sendRedirect("/admin/dashboard.jsp");
    }

    private void handleUpdatePublisher(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Publisher publisher = new Publisher(
                req.getParameter("publisher_name"),
                req.getParameter("phone"),
                req.getParameter("address")
        );
        publisher.setPublisherId(Integer.parseInt(req.getParameter("publisher_id")));
        new UpdatePublisherDAO(getConnection(), publisher).access();
        resp.sendRedirect("/admin/dashboard.jsp");
    }

    private void handleDeletePublisher(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        int publisherId = Integer.parseInt(req.getParameter("publisher_id"));
        new DeletePublisherDAO(getConnection(), publisherId).access();
        resp.sendRedirect("/admin/dashboard.jsp");
    }

    // ========================= DISCOUNT =========================

    private void handleAddDiscount(HttpServletRequest req, HttpServletResponse resp) throws Exception {
    Discount discount = new Discount();
    discount.setCode(req.getParameter("code"));
    discount.setDiscountPercentage(Double.parseDouble(req.getParameter("percentage")));
    discount.setExpiredDate(Timestamp.valueOf(req.getParameter("expiredDate") + " 00:00:00"));

    new InsertDiscountDAO(getConnection(), discount).access();
    resp.sendRedirect("/admin/dashboard.jsp");
}

    private void handleDeleteDiscount(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        int discountId = Integer.parseInt(req.getParameter("discount_id"));
        new DeleteDiscountDAO(getConnection(), discountId).access();
        resp.sendRedirect("/admin/dashboard.jsp");
    }
}
