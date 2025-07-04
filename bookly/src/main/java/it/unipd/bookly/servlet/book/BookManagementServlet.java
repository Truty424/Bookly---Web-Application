package it.unipd.bookly.servlet.book;

import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.dao.book.*;
import it.unipd.bookly.servlet.AbstractDatabaseServlet;
import it.unipd.bookly.LogContext;
import it.unipd.bookly.utilities.ServletUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@WebServlet(name = "BookManagementServlet", value = "/admin/books/*")
public class BookManagementServlet extends AbstractDatabaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction("bookManagement");

        try (Connection con = getConnection()) {
            List<Book> books = new GetAllBooksDAO(con).access().getOutputParam();
            req.setAttribute("books", books);
            req.getRequestDispatcher("/jsp/admin/manageBooks.jsp").forward(req, resp);
        } catch (Exception e) {
            LOGGER.error("Error loading book management page: {}", e.getMessage(), e);
            ServletUtils.redirectToErrorPage(req, resp, "BookManagementServlet error: " + e.getMessage());
        } finally {
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction("bookManagement");

        String action = req.getParameter("action");

        try (Connection con = getConnection()) {
            switch (action) {
                case "create" ->
                    createBook(req, resp, con);
                case "delete" ->
                    deleteBook(req, resp, con);
                default ->
                    ServletUtils.redirectToErrorPage(req, resp, "Unknown action: " + action);
            }
        } catch (Exception e) {
            LOGGER.error("Error handling book POST action: {}", e.getMessage(), e);
            ServletUtils.redirectToErrorPage(req, resp, "BookManagementServlet error: " + e.getMessage());
        } finally {
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }

    private void createBook(HttpServletRequest req, HttpServletResponse resp, Connection con) throws Exception {
        try {
            String title = req.getParameter("title");
            String language = req.getParameter("language");
            String isbn = req.getParameter("isbn");
            double price = Double.parseDouble(req.getParameter("price"));
            String edition = req.getParameter("edition");
            int publicationYear = Integer.parseInt(req.getParameter("publication_year"));
            int numberOfPages = Integer.parseInt(req.getParameter("number_of_pages"));
            int stockQuantity = Integer.parseInt(req.getParameter("stock_quantity"));
            double averageRate = 4.0; // Initial average rate
            String summary = req.getParameter("summary");

            Book newBook = new Book(
                    0, // ID will be auto-generated
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
                    null // You can load an image if needed later
            );

            boolean created = new InsertBookDAO(con, newBook).access().getOutputParam();
            LOGGER.info("Book created: {}", created);
        } catch (Exception e) {
            LOGGER.error("Error creating book: {}", e.getMessage(), e);
            req.setAttribute("error_message", "Failed to create book: " + e.getMessage());
            req.getRequestDispatcher("/jsp/book/bookForm.jsp").forward(req, resp);
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/admin/books");
    }

    private void deleteBook(HttpServletRequest req, HttpServletResponse resp, Connection con) throws Exception {
        try {
            String bookIdParam = req.getParameter("book_id");

            if (bookIdParam == null || bookIdParam.isEmpty()) {
                LOGGER.warn("Missing bookId parameter for deletion.");
                req.setAttribute("error_message", "Invalid request: missing book ID.");
                req.getRequestDispatcher("/jsp/admin/manageBooks.jsp").forward(req, resp);
                return;
            }

            int bookId = Integer.parseInt(bookIdParam);
            LOGGER.info("Attempting to delete book with ID: {}", bookId);

            boolean deleted = new DeleteBookDAO(con, bookId).access().getOutputParam();

            if (deleted) {
                LOGGER.info("Book with ID {} deleted successfully.", bookId);
            } else {
                LOGGER.warn("No book found with ID {}. Deletion skipped.", bookId);
                req.setAttribute("error_message", "Book not found or could not be deleted.");
            }

            resp.sendRedirect(req.getContextPath() + "/admin/books");

        } catch (NumberFormatException e) {
            LOGGER.error("Invalid bookId format for deletion: {}", req.getParameter("bookId"), e);
            req.setAttribute("error_message", "Invalid book ID format.");
            req.getRequestDispatcher("/jsp/admin/manageBooks.jsp").forward(req, resp);

        } catch (Exception e) {
            LOGGER.error("Exception while deleting book: {}", e.getMessage(), e);
            req.setAttribute("error_message", "Internal error during book deletion.");
            req.getRequestDispatcher("/jsp/admin/manageBooks.jsp").forward(req, resp);
        }
    }
}
