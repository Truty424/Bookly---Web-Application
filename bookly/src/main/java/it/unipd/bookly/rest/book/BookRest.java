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

import it.unipd.bookly.dao.AbstractDAO;

/**
 * Unified REST controller for book-related operations.
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
                case "GET" -> handleGet(path, idParam);
                case "POST" -> handleInsertBook();
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
                default -> respondError("HTTP method not allowed", "E405");
            }
        } catch (Exception e) {
            LOGGER.error("BookRest error: ", e);
            respondError("Internal server error: " + e.getMessage(), "E500");
        }
    }

    private void handleGet(String path, String idParam) throws Exception {
        res.setContentType("application/json;charset=UTF-8");

        if (path.endsWith("/books/top-rated")) {
            List<Book> books = new GetTopRatedBooksDAO(con, 4.0).access().getOutputParam();
            mapper.writeValue(res.getOutputStream(), books);

        } else if (path.contains("/books/search")) {
            String title = req.getParameter("title");
            if (title == null || title.isBlank()) {
                respondError("Missing 'title' parameter for search", "E400");
                return;
            }
            List<Book> books = new SearchBookByTitleDAO(con, title).access().getOutputParam();
            mapper.writeValue(res.getOutputStream(), books);

        } else if (path.contains("/books/author")) {
            handleGetByParam(new GetBooksByAuthorIdDAO(con, parseIdParam()), "author");

        } else if (path.contains("/books/category")) {
            handleGetByParam(new GetBooksByCategoryIdDAO(con, parseIdParam()), "category");

        } else if (path.contains("/books/publisher")) {
            handleGetByParam(new GetBooksByPublisherIdDAO(con, parseIdParam()), "publisher");

        } else if (idParam != null) {
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

    private void handleGetByParam(AbstractDAO<List<Book>> dao, String type) throws Exception {
        List<Book> books = dao.access().getOutputParam();
        if (books.isEmpty()) {
            respondNotFound("No books found for " + type + " ID: " + req.getParameter("id"));
        } else {
            mapper.writeValue(res.getOutputStream(), books);
        }
    }

    private int parseIdParam() throws IOException {
        String idParam = req.getParameter("id");
        if (idParam == null) {
            respondError("Missing 'id' parameter", "E400");
            throw new IllegalArgumentException("Missing 'id'");
        }
        return Integer.parseInt(idParam);
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
            respondError("Failed to create book", "400");
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
            respondSuccess("Book updated successfully.", "200", book.getTitle());
        } else {
            respondNotFound("Book not found for update.");
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

        boolean updated = new UpdateBookStockDAO(con, bookId, quantity).access().getOutputParam();
        if (updated) {
            respondSuccess("Stock updated successfully", "200", null);
        } else {
            respondNotFound("Failed to update stock: Book with ID " + bookId + " not found");
        }
    }

    private void handleDeleteBook(int bookId) throws Exception {
        boolean deleted = new DeleteBookDAO(con, bookId).access().getOutputParam();
        if (deleted) {
            respondSuccess("Book deleted", "200", null);
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

    private void respondSuccess(String msg, String code, String data) throws IOException {
        res.setStatus(HttpServletResponse.SC_OK);
        mapper.writeValue(res.getOutputStream(), new Message(msg, code, data));
    }
}
