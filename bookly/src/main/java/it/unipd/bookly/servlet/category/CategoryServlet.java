package it.unipd.bookly.servlet.category;

import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.Resource.Category;
import it.unipd.bookly.dao.category.GetAllCategoriesDAO;
import it.unipd.bookly.dao.category.GetBooksByCategoryDAO;
import it.unipd.bookly.servlet.AbstractDatabaseServlet;
import it.unipd.bookly.utilities.ServletUtils;
import it.unipd.bookly.LogContext;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

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
                case "all" -> showAllCategories(req, resp, con);
                case "categoryId" -> showBooksByCategory(req, resp, con, Integer.parseInt(cleanPath));
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
        if (path.isEmpty()) return "all";
        if (path.matches("\\d+")) return "categoryId";
        return "unknown";
    }

    private void showAllCategories(HttpServletRequest req, HttpServletResponse resp, Connection con) throws Exception {
        List<Category> categories = new GetAllCategoriesDAO(con).access().getOutputParam();
        req.setAttribute("all_categories", categories);
        req.getRequestDispatcher("/jsp/category/allCategories.jsp").forward(req, resp);
    }

    private void showBooksByCategory(HttpServletRequest req, HttpServletResponse resp, Connection con, int categoryId) throws Exception {
        List<Book> books = new GetBooksByCategoryDAO(con, categoryId).access().getOutputParam();
        req.setAttribute("category_books", books);
        req.setAttribute("category_id", categoryId);
        req.getRequestDispatcher("/jsp/category/categoryBooks.jsp").forward(req, resp);
    }
}
