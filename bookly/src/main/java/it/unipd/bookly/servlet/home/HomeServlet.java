package it.unipd.bookly.servlet.home;

import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.unipd.bookly.LogContext;
import it.unipd.bookly.Resource.Author;
import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.Resource.Category;
import it.unipd.bookly.dao.author.GetAuthorsByBookDAO;
import it.unipd.bookly.dao.book.GetTopRatedBooksDAO;
import it.unipd.bookly.dao.category.GetAllCategoriesDAO;
import it.unipd.bookly.dao.category.GetBooksByCategoryDAO;
import it.unipd.bookly.servlet.AbstractDatabaseServlet;
import it.unipd.bookly.utilities.ServletUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "HomeServlet", value = "/home")
public class HomeServlet extends AbstractDatabaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction("homeServlet");

        double minRating = 4.0;

        try {

            List<Category> categories;
            try (Connection con = getConnection()) {
                categories = new GetAllCategoriesDAO(con).access().getOutputParam();
                LOGGER.info("Loaded categories: {}", categories);
            }

            Map<Integer, Category> categoryMap = new HashMap<>();
            for (Category category : categories) {
                categoryMap.put(category.getCategory_id(), category);
            }

            List<Book> topRatedBooks;
            try (Connection con = getConnection()) {
                topRatedBooks = new GetTopRatedBooksDAO(con, minRating).access().getOutputParam();
            }

            Map<Integer, List<Book>> booksByCategory = new HashMap<>();
            for (Category category : categories) {
                List<Book> books;
                try (Connection con = getConnection()) {
                    books = new GetBooksByCategoryDAO(con, category.getCategory_id()).access().getOutputParam();
                }
                booksByCategory.put(category.getCategory_id(), books);
                LOGGER.info("Loaded category: ID={} Name='{}'",
                        category.getCategory_id(), category.getCategory_name());
            }

            Map<Integer, List<Author>> bookAuthors = new HashMap<>();
            for (List<Book> books : booksByCategory.values()) {
                for (Book book : books) {
                    List<Author> authors;
                    try (Connection con = getConnection()) {
                        authors = new GetAuthorsByBookDAO(con, book.getBookId()).access().getOutputParam();
                    }
                    bookAuthors.put(book.getBookId(), authors);
                }
            }

            // ✅ Set attributes
            req.setAttribute("categories", categories);
            req.setAttribute("topRatedBooks", topRatedBooks);
            req.setAttribute("categoryMap", categoryMap);
            req.setAttribute("booksByCategory", booksByCategory);
            req.setAttribute("bookAuthors", bookAuthors);

            req.getRequestDispatcher("/jsp/home.jsp").forward(req, resp);

        } catch (Exception e) {
            LOGGER.error("HomeServlet error: {}", e.getMessage(), e);
            ServletUtils.redirectToErrorPage(req, resp, "HomeServlet error: " + e.getMessage());
        } finally {
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }
}
