package it.unipd.bookly.servlet.category;

import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.Resource.Category;
import it.unipd.bookly.dao.category.GetBooksByCategoryDAO;
import it.unipd.bookly.dao.category.GetAllCategoriesDAO;
import it.unipd.bookly.servlet.AbstractDatabaseServlet;
import it.unipd.bookly.LogContext;
import it.unipd.bookly.utilities.ServletUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "CategoryServlet", value = "/category/*")
public class CategoryServlet extends AbstractDatabaseServlet {

    /**
     * Handles GET requests for:
     * - /category                 → show all categories
     * - /category/{categoryId}   → show books for a specific category
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction("categoryServlet");

        String path = req.getPathInfo(); // /3 or null

        try {
            if (path == null || path.equals("/")) {
                showAllCategories(req, resp);
            } else if (path.matches("/\\d+")) {
                showBooksByCategory(req, resp, path);
            } else {
                ServletUtils.redirectToErrorPage(req, resp, "Invalid category path.");
            }
        } catch (Exception e) {
            LOGGER.error("CategoryServlet error: {}", e.getMessage());
            ServletUtils.redirectToErrorPage(req, resp, "An error occurred while processing the category request.");
        } finally {
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }

    private void showAllCategories(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        try (var con = getConnection()) {
            List<Category> categories = new GetAllCategoriesDAO(con).access().getOutputParam();
            req.setAttribute("all_categories", categories);
            req.getRequestDispatcher("/jsp/category/allCategories.jsp").forward(req, resp);
        }
    }

    private void showBooksByCategory(HttpServletRequest req, HttpServletResponse resp, String path) throws Exception {
        try (var con = getConnection()) {
            int category_id = Integer.parseInt(path.substring(1)); // remove leading slash
            List<Book> books = new GetBooksByCategoryDAO(con, category_id).access().getOutputParam();

            req.setAttribute("category_books", books);
            req.setAttribute("category_id", category_id);
            req.getRequestDispatcher("/jsp/category/categoryBooks.jsp").forward(req, resp);
        }
    }
}
