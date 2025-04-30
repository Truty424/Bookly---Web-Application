package it.unipd.bookly.servlet.search;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import it.unipd.bookly.LogContext;
import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.Resource.Author;
import it.unipd.bookly.dao.book.SearchBookByTitleDAO;
import it.unipd.bookly.dao.author.GetAuthorsByBookListDAO;
import it.unipd.bookly.servlet.AbstractDatabaseServlet;
import it.unipd.bookly.utilities.ServletUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "SearchServlet", value = "/search")
public class SearchServlet extends AbstractDatabaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction("searchServlet");

        String query = req.getParameter("query");

        try {
            if (query == null || query.trim().isEmpty()) {
                ServletUtils.redirectToErrorPage(req, resp, "Search query is missing or empty.");
                return;
            }

            // Search books by title
            List<Book> books;
            try (Connection con = getConnection()) {
                books = new SearchBookByTitleDAO(con, query.trim()).access().getOutputParam();
            }
            // Extract book IDs
            List<Integer> bookIds = books.stream()
                    .map(Book::getBookId)
                    .collect(Collectors.toList());

            // Get authors for all found books in a single query
            Map<Integer, List<Author>> authorsMap;
            try (Connection con = getConnection()) {
                authorsMap = new GetAuthorsByBookListDAO(con, bookIds).access().getOutputParam();
            }
            // Pass data to JSP
            req.setAttribute("search_results", books);
            req.setAttribute("authors_map", authorsMap);
            req.setAttribute("search_query", query);

            req.getRequestDispatcher("/jsp/search/results.jsp").forward(req, resp);

        } catch (Exception e) {
            LOGGER.error("SearchServlet error: {}", e.getMessage(), e);
            ServletUtils.redirectToErrorPage(req, resp, "SearchServlet error: " + e.getMessage());
        } finally {
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }
}
