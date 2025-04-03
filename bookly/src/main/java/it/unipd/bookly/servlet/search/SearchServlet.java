package it.unipd.bookly.servlet.search;

import java.io.IOException;
 import java.util.List;

 import it.unipd.bookly.LogContext;
 import it.unipd.bookly.Resource.Book;
 import it.unipd.bookly.dao.book.SearchBookByTitleDAO;
 import it.unipd.bookly.servlet.AbstractDatabaseServlet;
 import jakarta.servlet.ServletException;
 import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
 import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "SearchServlet", value = "/search") public class SearchServlet extends AbstractDatabaseServlet {

@Override
protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    LogContext.setIPAddress(req.getRemoteAddr());
    LogContext.setResource(req.getRequestURI());
    LogContext.setAction("searchServlet");

    String query = req.getParameter("query");

    try {
        if (query == null || query.trim().isEmpty()) {
            resp.sendRedirect("/html/error.html");
            return;
        }

        List<Book> books = new SearchBookByTitleDAO(getConnection(), query.trim()).access().getOutputParam();
        req.setAttribute("search_results", books);
        req.setAttribute("search_query", query);
        req.getRequestDispatcher("/jsp/search/results.jsp").forward(req, resp);
    } catch (Exception e) {
        LOGGER.error("SearchServlet error: {}", e.getMessage());
        resp.sendRedirect("/html/error.html");
    } finally {
        LogContext.removeAction();
        LogContext.removeResource();
    }
}

}