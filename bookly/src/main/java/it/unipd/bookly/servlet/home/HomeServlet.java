package it.unipd.bookly.servlet.home;

import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.Resource.Category;
import it.unipd.bookly.dao.book.GetAllBooksDAO;
import it.unipd.bookly.dao.book.GetTopRatedBooksDAO;
import it.unipd.bookly.dao.category.GetAllCategoriesDAO;
import it.unipd.bookly.servlet.AbstractDatabaseServlet;
import it.unipd.bookly.LogContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class HomeServlet extends AbstractDatabaseServlet {

@Override
protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    LogContext.setIPAddress(req.getRemoteAddr());
    LogContext.setResource(req.getRequestURI());
    LogContext.setAction("HomeServlet");
    double minRating = 4.0;
    
    try {
        // Fetch all categories
        List<Category> categories = new GetAllCategoriesDAO(getConnection()).access().getOutputParam();
        
        // Fetch top-rated books
        List<Book> topRatedBooks = new GetTopRatedBooksDAO(getConnection(), minRating)
                                        .access()
                                        .getOutputParam();
        
        // Fetch all books (optional, if needed)
        List<Book> allBooks = new GetAllBooksDAO(getConnection()).access().getOutputParam();
        
        // Set attributes for rendering in JSP
        req.setAttribute("categories", categories);
        req.setAttribute("topRatedBooks", topRatedBooks);
        req.setAttribute("allBooks", allBooks);
        
        // Forward to home.jsp
        req.getRequestDispatcher("/jsp/home.jsp").forward(req, resp);
    } catch (Exception e) {
        LOGGER.error("Error in HomeServlet: {}", e.getMessage());
        req.getRequestDispatcher("/html/error.html").forward(req, resp);
    } finally {
        LogContext.removeAction();
        LogContext.removeResource();
    }
}

}