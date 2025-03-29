package it.unipd.bookly.servlet;

import it.unipd.bookly.dao.BookDAO;
import it.unipd.bookly.resources.Book;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Servlet that handles book-related operations
 */
@WebServlet("/book")
public class BookServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    private final BookDAO bookDAO = new BookDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            List<Book> books = bookDAO.getAllBooks();
            String jsonBooks = gson.toJson(books);
            response.getWriter().write(jsonBooks);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error retrieving books.");
        }
    }
}