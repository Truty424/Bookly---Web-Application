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
 * Gets the {@code DataSource} for managing the connection pool to the database.
 *
 * @author Nicola Ferro (ferro@dei.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public abstract class AbstractDatabaseServlet extends HttpServlet {

    /**
     * A LOGGER available for all the subclasses.
     */
    protected static final Logger LOGGER = LogManager.getLogger(AbstractDatabaseServlet.class);

    /**
     * The connection pool to the database.
     */
    private DataSource ds;

    /**
     * Gets the {@code DataSource} for managing the connection pool to the database.
     *
     * @param config a {@code ServletConfig} object containing the servlet's
     *               configuration and initialization
     *               parameters.
     *
     * @throws ServletException if an exception has occurred that interferes with
     *                          the servlet's normal operation
     */
    public void init(ServletConfig config) throws ServletException {

        // the JNDI lookup context
        InitialContext cxt;

        try {
            cxt = new InitialContext();
            ds = (DataSource) cxt.lookup("java:/comp/env/jdbc/BooklyDB");

            LOGGER.info("Connection pool to the database pool successfully acquired.");
        } catch (NamingException e) {
            ds = null;

            LOGGER.error("Unable to acquire the connection pool to the database.", e);

            throw new ServletException("Unable to acquire the connection pool to the database", e);
        }
    }

    /**
     * Releases the {@code DataSource} for managing the connection pool to the
     * database.
     */
    public void destroy() {
        ds = null;
        LOGGER.info("Connection pool to the database pool successfully released.");
    }

    public void writeError(HttpServletResponse res, ErrorCode ec) throws IOException {
        res.setStatus(ec.getHTTPCode());
        res.getWriter().write(ec.toJSON().toString());
    }

    /**
     * Returns a {@link Connection} for accessing the database.
     *
     * @return a {@link Connection} for accessing the database
     *
     * @throws java.sql.SQLException if anything goes wrong in obtaining the
     *                               connection.
     */
    protected final Connection getConnection() throws SQLException {
        try {
            return ds.getConnection();
        } catch (final SQLException e) {
            LOGGER.error("Unable to acquire the connection from the pool.", e);
            throw e;
        }
    }

}
