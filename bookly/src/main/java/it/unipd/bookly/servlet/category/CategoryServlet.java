package it.unipd.bookly.servlet.category;

import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.Resource.Category;
import it.unipd.bookly.dao.category.GetBooksByCategoryDAO;
import it.unipd.bookly.dao.category.GetAllCategoriesDAO;
import it.unipd.bookly.servlet.AbstractDatabaseServlet;
import it.unipd.bookly.LogContext;
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

        String path = req.getRequestURI();

        try {
            if (path.matches(".*/category/?")) {
                showAllCategories(req, resp);
            } else if (path.matches(".*/category/\\d+")) {
                showBooksByCategory(req, resp);
            } else {
                resp.sendRedirect("/html/error.html");
            }
        } catch (Exception e) {
            LOGGER.error("CategoryServlet error: {}", e.getMessage());
            resp.sendRedirect("/html/error.html");
        } finally {
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }

    private void showAllCategories(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        List<Category> categories = new GetAllCategoriesDAO(getConnection()).access().getOutputParam();
        req.setAttribute("all_categories", categories);
        req.getRequestDispatcher("/jsp/category/allCategories.jsp").forward(req, resp);
    }

    private void showBooksByCategory(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String[] segments = req.getRequestURI().split("/");
        int categoryId = Integer.parseInt(segments[segments.length - 1]);

        List<Book> books = new GetBooksByCategoryDAO(getConnection(), categoryId).access().getOutputParam();
        req.setAttribute("category_books", books);
        req.setAttribute("category_id", categoryId);
        req.getRequestDispatcher("/jsp/category/categoryBooks.jsp").forward(req, resp);
    }
}
