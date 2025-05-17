package it.unipd.bookly.servlet.category;

import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.Resource.Category;
import it.unipd.bookly.dao.category.GetAllCategoriesDAO;
import it.unipd.bookly.dao.category.GetBooksByCategoryDAO;
import it.unipd.bookly.dao.review.GetAvgRatingForBookDAO;
import it.unipd.bookly.servlet.AbstractDatabaseServlet;
import it.unipd.bookly.utilities.ServletUtils;
import it.unipd.bookly.LogContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import it.unipd.bookly.dao.category.GetCategoryByIdDAO;

@WebServlet(name = "CategoryServlet", value = "/category/*")
public class CategoryServlet extends AbstractDatabaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction("CategoryServlet");

        String path = req.getPathInfo(); // e.g. / or /3

        try (Connection con = getConnection()) {
            // Normalize path (remove leading/trailing slashes)
            String cleanPath = (path == null) ? "" : path.replaceAll("^/+", "").replaceAll("/+$", "");

            switch (resolveRoute(cleanPath)) {
                case "all" ->
                    showAllCategories(req, resp, con);
                case "categoryId" ->
                    showBooksByCategory(req, resp, Integer.parseInt(cleanPath));
                default -> {
                    LOGGER.warn("Invalid category path: {}", cleanPath);
                    ServletUtils.redirectToErrorPage(req, resp, "Invalid category path.");
                }
            }

        } catch (Exception e) {
            LOGGER.error("Error in CategoryServlet: {}", e.getMessage(), e);
            ServletUtils.redirectToErrorPage(req, resp, "An error occurred while processing the category request.");
        } finally {
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }

    private String resolveRoute(String path) {
        if (path.isEmpty()) {
            return "all";
        }
        if (path.matches("\\d+")) {
            return "categoryId";
        }
        return "unknown";
    }

    private void showAllCategories(HttpServletRequest req, HttpServletResponse resp, Connection con) throws Exception {
        List<Category> categories = new GetAllCategoriesDAO(con).access().getOutputParam();
        req.setAttribute("all_categories", categories);
        req.getRequestDispatcher("/jsp/category/allCategories.jsp").forward(req, resp);
    }

    private void showBooksByCategory(HttpServletRequest req, HttpServletResponse resp, int categoryId) throws Exception {
        List<Book> books;
        Category category;
        Map<Integer, Double> bookRatings = new HashMap<>();

        // Get all books in the given category
        try (Connection con = getConnection()) {
            books = new GetBooksByCategoryDAO(con, categoryId).access().getOutputParam();
        }

        // For each book, retrieve its average rating using a separate connection
        for (Book book : books) {
            try (Connection con = getConnection()) {
                Double rating = new GetAvgRatingForBookDAO(con, book.getBookId()).access().getOutputParam();
                bookRatings.put(book.getBookId(), rating); // Store in map with bookId as key
            }
        }

        // Retrieve the category information (e.g. name)
        try (Connection con = getConnection()) {
            category = new GetCategoryByIdDAO(con, categoryId).access().getOutputParam();
        }

        if (category == null) {
            LOGGER.warn("No category found for ID {}", categoryId);
            ServletUtils.redirectToErrorPage(req, resp, "Category not found.");
            return;
        }

        // Pass all data to the JSP
        req.setAttribute("category_books", books);
        req.setAttribute("category_id", categoryId);
        req.setAttribute("category_name", category.getCategory_name());
        req.setAttribute("book_ratings", bookRatings); // <-- NEW: Pass ratings to JSP

        req.getRequestDispatcher("/jsp/category/categoryBooks.jsp").forward(req, resp);
    }

}
