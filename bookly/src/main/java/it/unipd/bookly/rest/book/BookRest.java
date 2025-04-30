package it.unipd.bookly.rest.book;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.dao.book.*;
import it.unipd.bookly.rest.AbstractRestResource;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

/**
 * Unified REST controller for book-related operations:
 * - GET /api/books → get all books
 * - GET /api/books?id=X → get book by ID
 * - POST /api/books → insert a new book
 * - PUT /api/books → update book info
 * - PUT /api/books/stock?id=X&amp;quantity=Y → update stock
 * - DELETE /api/books?id=X → delete book
 * - GET /api/books/top-rated → books with rating &gt;= 4
 * - GET /api/books/search?title=abc → search books
 * - GET /api/books/author?id=X → by author
 * - GET /api/books/category?id=X → by category
 * - GET /api/books/publisher?id=X → by publisher
 */
public class BookRest extends AbstractRestResource {

    private final ObjectMapper mapper = new ObjectMapper();

    public BookRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("book", req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        final String method = req.getMethod();
        final String path = req.getRequestURI();
        final String idParam = req.getParameter("id");

        try {
            switch (method) {
                case "GET" ->
                    handleGetRequests(path, idParam);
                case "POST" ->
                    handleInsertBook();
                case "PUT" -> {
                    if (path.contains("/stock")) {
                        handleUpdateStock();
                    } else {
                        handleUpdateBook();
                    }
                }
                case "DELETE" -> {
                    if (idParam != null) {
                        handleDeleteBook(Integer.parseInt(idParam));
                    } else {
                        respondError("Missing 'id' for deletion", "E400");
                    }
                }
                default ->
                    respondError("HTTP method not allowed", "E405");
            }
        } catch (Exception e) {
            LOGGER.error("BookRest error: ", e);
            respondError("Internal server error: " + e.getMessage(), "E500");
        }
    }

    private void handleGetRequests(String path, String idParam) throws Exception {
        res.setContentType("application/json;charset=UTF-8");

        if (path.endsWith("/books/top-rated")) {
            List<Book> books = new GetTopRatedBooksDAO(con, 4.0).access().getOutputParam();
            mapper.writeValue(res.getOutputStream(), books);
        } else if (path.contains("/books/search")) {
            String title = req.getParameter("title");
            if (title == null) {
                respondError("Missing 'title' parameter for search", "E400");
                return;
            }
            List<Book> books = new SearchBookByTitleDAO(con, title).access().getOutputParam();
            mapper.writeValue(res.getOutputStream(), books);
        } else if (path.contains("/books/author")) {
            List<Book> books = new GetBooksByAuthorIdDAO(con, Integer.parseInt(req.getParameter("id"))).access().getOutputParam();
            mapper.writeValue(res.getOutputStream(), books);
        } else if (path.contains("/books/category")) {
            List<Book> books = new GetBooksByCategoryIdDAO(con, Integer.parseInt(req.getParameter("id"))).access().getOutputParam();
            mapper.writeValue(res.getOutputStream(), books);
        } else if (path.contains("/books/publisher")) {
            List<Book> books = new GetBooksByPublisherIdDAO(con, Integer.parseInt(req.getParameter("id"))).access().getOutputParam();
            mapper.writeValue(res.getOutputStream(), books);
        } else if (path.contains("/books") && idParam != null) {
            Book book = new GetBookByIdDAO(con, Integer.parseInt(idParam)).access().getOutputParam();
            if (book != null) {
                mapper.writeValue(res.getOutputStream(), book);
            } else {
                respondNotFound("Book not found with ID: " + idParam);
            }
        } else {
            List<Book> books = new GetAllBooksDAO(con).access().getOutputParam();
            mapper.writeValue(res.getOutputStream(), books);
        }
    }

    private void handleInsertBook() throws Exception {
        Book book = mapper.readValue(req.getInputStream(), Book.class);
        book.setBookId(0);

        if (book.getAverage_rate() == 0.0) {
            book.setAverage_rate(0.0);
        }

        boolean inserted = new InsertBookDAO(con, book).access().getOutputParam();

        if (inserted) {
            res.setStatus(HttpServletResponse.SC_CREATED);
            mapper.writeValue(res.getOutputStream(), new Message("Book created successfully", "201", book.getTitle()));
        } else {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            mapper.writeValue(res.getOutputStream(), new Message("Failed to create book", "400", null));
        }
    }

    private void handleUpdateBook() throws Exception {
        Book book = mapper.readValue(req.getInputStream(), Book.class);

        boolean updated = new UpdateBookDAO(
                con,
                book.getBookId(),
                book.getTitle(),
                book.getLanguage(),
                book.getIsbn(),
                book.getPrice(),
                book.getEdition(),
                book.getPublication_year(),
                book.getNumber_of_pages(),
                book.getStockQuantity(),
                book.getAverage_rate(),
                book.getSummary()
        ).access().getOutputParam();

        if (updated) {
            res.setStatus(HttpServletResponse.SC_OK);
            new Message("book updated successfully.", "200", book.getTitle()).toJSON(res.getOutputStream());
        } else {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            new Message("Update failed.", "404", "book not found.").toJSON(res.getOutputStream());
        }
    }

    private void handleUpdateStock() throws Exception {
        String idParam = req.getParameter("id");
        String quantityParam = req.getParameter("quantity");

        if (idParam == null || quantityParam == null) {
            respondError("Missing 'id' or 'quantity' for stock update", "E400");
            return;
        }

        int bookId = Integer.parseInt(idParam);
        int quantity = Integer.parseInt(quantityParam);

        new UpdateBookStockDAO(con, bookId, quantity).access();
        res.setStatus(HttpServletResponse.SC_OK);
        mapper.writeValue(res.getOutputStream(), new Message("Stock updated", "200", null));
    }

    private void handleDeleteBook(int bookId) throws Exception {
        boolean deleted = new DeleteBookDAO(con, bookId).access().getOutputParam();

        if (deleted) {
            res.setStatus(HttpServletResponse.SC_OK);
            mapper.writeValue(res.getOutputStream(), new Message("Book deleted", "200", null));
        } else {
            respondNotFound("Book with ID " + bookId + " not found");
        }
    }

    private void respondError(String message, String code) throws IOException {
        res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        mapper.writeValue(res.getOutputStream(), new Message(message, code, null));
    }

    private void respondNotFound(String msg) throws IOException {
        res.setStatus(HttpServletResponse.SC_NOT_FOUND);
        mapper.writeValue(res.getOutputStream(), new Message(msg, "E404", null));
    }
}
