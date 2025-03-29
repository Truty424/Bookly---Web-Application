package it.unipd.bookly.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.unipd.bookly.utilities.ErrorCode;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Abstract servlet that manages database connection pooling via DataSource.
 * All Bookly servlets interacting with the DB should extend this.
 */
public abstract class AbstractDatabaseServlet extends HttpServlet {

    protected static final Logger LOGGER = LogManager.getLogger(AbstractDatabaseServlet.class);

    private DataSource ds;

    @Override
    public void init(ServletConfig config) throws ServletException {
        try {
            InitialContext ctx = new InitialContext();
            ds = (DataSource) ctx.lookup("java:/comp/env/jdbc/booklyDB");
            LOGGER.info("Successfully acquired the database connection pool.");
        } catch (NamingException e) {
            ds = null;
            LOGGER.error("Failed to acquire database connection pool.", e);
            throw new ServletException("Unable to acquire the connection pool to the database", e);
        }
    }

    @Override
    public void destroy() {
        ds = null;
        LOGGER.info("Database connection pool released.");
    }

    /**
     * Helper method for writing a standardized error response.
     */
    public void writeError(HttpServletResponse res, ErrorCode ec) throws IOException {
        res.setStatus(ec.getHTTPCode());
        res.setContentType("application/json");
        res.getWriter().write(ec.toJSON().toString());
    }

    /**
     * Returns a database connection from the pool.
     */
    protected final Connection getConnection() throws SQLException {
        try {
            return ds.getConnection();
        } catch (SQLException e) {
            LOGGER.error("Unable to get connection from the pool.", e);
            throw e;
        }
    }
}
