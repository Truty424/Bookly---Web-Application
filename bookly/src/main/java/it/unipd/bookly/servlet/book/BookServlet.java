package it.unipd.bookly.servlet.book;

import java.io.IOException;
import java.util.List;

import it.unipd.bookly.LogContext;
import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.dao.book.GetAllBooksDAO;
import it.unipd.bookly.dao.book.GetBookByIdDAO;
import it.unipd.bookly.servlet.AbstractDatabaseServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import it.unipd.bookly.Resource.Author;
import it.unipd.bookly.dao.author.GetAuthorsByBookDAO;


/**
 * Handles GET requests for:
 * - /book               → show all books
 * - /book/{bookId}      → show details of a specific book
 */
@WebServlet(name = "BookServlet", value = "/book/*")
public class BookServlet extends AbstractDatabaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction("bookServlet");

        String path = req.getRequestURI();

        try {
            if (path.matches(".*/book/?")) {
                showAllBooks(req, resp);
            } else if (path.matches(".*/book/\\d+")) {
                showBookDetails(req, resp);
            } else {
                resp.sendRedirect("/html/error.html");
            }
        } catch (Exception e) {
            LOGGER.error("BookServlet error: {}", e.getMessage());
            resp.sendRedirect("/html/error.html");
        } finally {
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }

    private void showAllBooks(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        List<Book> books = new GetAllBooksDAO(getConnection()).access().getOutputParam();
        req.setAttribute("all_books", books);
        req.getRequestDispatcher("/jsp/book/allBooks.jsp").forward(req, resp);
    }

    private void showBookDetails(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String[] segments = req.getRequestURI().split("/");
        int bookId = Integer.parseInt(segments[segments.length - 1]);

        Book book = new GetBookByIdDAO(getConnection(), bookId).access().getOutputParam();
        List<Author> authors = new GetAuthorsByBookDAO(getConnection(), bookId).access().getOutputParam(); // 💡

        if (book != null) {
            req.setAttribute("book_details", book);
            req.setAttribute("authors", authors);
            req.getRequestDispatcher("/jsp/book/bookDetails.jsp").forward(req, resp);
        } else {
            resp.sendRedirect("/html/error.html");
        }
    }
}