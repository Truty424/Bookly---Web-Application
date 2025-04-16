package it.unipd.bookly.servlet.home;

import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.Resource.Category;
import it.unipd.bookly.Resource.Author;
import it.unipd.bookly.dao.book.GetAllBooksDAO;
import it.unipd.bookly.dao.book.GetTopRatedBooksDAO;
import it.unipd.bookly.dao.category.GetAllCategoriesDAO;
import it.unipd.bookly.dao.author.GetAuthorsByBookDAO;
import it.unipd.bookly.servlet.AbstractDatabaseServlet;
import it.unipd.bookly.LogContext;
import it.unipd.bookly.utilities.ServletUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.util.*;

public class HomeServlet extends AbstractDatabaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction("HomeServlet");

        double minRating = 4.0;

        try (Connection con = getConnection()) {
            // Load categories for homepage
            List<Category> categories = new GetAllCategoriesDAO(con).access().getOutputParam();

            // Load featured (top-rated) books
            List<Book> topRatedBooks = new GetTopRatedBooksDAO(con, minRating).access().getOutputParam();

            // Load general listing
            List<Book> allBooks = new GetAllBooksDAO(con).access().getOutputParam();

            // Attach authors to books
            Map<Integer, List<Author>> bookAuthors = new HashMap<>();
            for (Book book : allBooks) {
                List<Author> authors = new GetAuthorsByBookDAO(con, book.getBookId()).access().getOutputParam();
                bookAuthors.put(book.getBookId(), authors);
            }

            // Set attributes for JSP
            req.setAttribute("categories", categories);
            req.setAttribute("topRatedBooks", topRatedBooks);
            req.setAttribute("allBooks", allBooks);
            req.setAttribute("bookAuthors", bookAuthors);

            req.getRequestDispatcher("/jsp/home.jsp").forward(req, resp);
        } catch (Exception e) {
            LOGGER.error("Error in HomeServlet: {}", e.getMessage(), e);
            ServletUtils.redirectToErrorPage(req, resp, "HomeServlet error: " + e.getMessage());
        } finally {
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }
}
