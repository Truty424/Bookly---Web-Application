package it.unipd.bookly.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.StringFormatterMessageFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Provides support for the actual implementation of a DAO object in the Bookly project.
 *
 * <p>
 * {@code AbstractDAO} guarantees consistent database access and exception handling for
 * subclasses interacting with the DBMS, while abstracting from DBMS-specific code.
 * </p>
 *
 * @param <T> the type of the output parameter returned by the DAO.
 */
public abstract class AbstractDAO<T> implements DataAccessObject<T> {

    /**
     * A LOGGER available for all subclasses.
     */
    protected static final Logger LOGGER = LogManager.getLogger(AbstractDAO.class,
            StringFormatterMessageFactory.INSTANCE);

    /**
     * The connection used to access the database.
     */
    protected final Connection con;

    /**
     * The output parameter to return.
     */
    protected T outputParam = null;

    /**
     * Flag to ensure database is accessed only once.
     */
    private boolean accessed = false;

    /**
     * Synchronization lock.
     */
    private final Object lock = new Object();

    /**
     * Creates a new DAO object.
     *
     * @param con the connection to be used for accessing the database.
     */
    protected AbstractDAO(final Connection con) {
        if (con == null) {
            LOGGER.error("The connection cannot be null.");
            throw new NullPointerException("The connection cannot be null.");
        }

        this.con = con;

        try {
            // Ensure autocommit is true by default
            con.setAutoCommit(true);
            LOGGER.debug("Auto-commit set to default value true.");
        } catch (final SQLException e) {
            LOGGER.warn("Unable to set connection auto-commit to true.", e);
        }
    }

    /**
     * Executes the database access logic defined in subclasses via doAccess().
     *
     * @return this DAO instance after execution
     * @throws SQLException if an error occurs during access
     */
    public final DataAccessObject<T> access() throws SQLException {
        synchronized (lock) {
            if (accessed) {
                LOGGER.error("Cannot use a DataAccessObject more than once.");
                throw new SQLException("Cannot use a DataAccessObject more than once.");
            }
            accessed = true;
        }

        try {
            doAccess();
                LOGGER.debug("Connection successfully closed.");

            // try {
            //     // con.close();
            // } catch (final SQLException e) {
            //     LOGGER.error("Unable to close the connection to the database.", e);
            //     throw e;
            // }

        } catch (final Throwable t) {
            LOGGER.error("Unable to perform the requested database access operation.", t);

            try {
                if (!con.getAutoCommit()) {
                    con.rollback();
                    LOGGER.info("Transaction successfully rolled-back.");
                }
            } catch (final SQLException e) {
                LOGGER.error("Unable to roll-back the transaction.", e);
            } finally {
                try {
                    con.close();
                    LOGGER.debug("Connection successfully closed.");
                } catch (final SQLException e) {
                    LOGGER.error("Unable to close the connection to the database.", e);
                }
            }

            if (t instanceof SQLException) {
                throw (SQLException) t;
            } else {
                throw new SQLException("Unable to perform the requested database access operation.", t);
            }
        }

        return this;
    }

    /**
     * Retrieves the output parameter after successful access.
     *
     * @return the result of the DAO operation
     */
    @Override
    public final T getOutputParam() {
        synchronized (lock) {
            if (!accessed) {
                LOGGER.error("Cannot retrieve the output parameter before accessing the database.");
                throw new IllegalStateException("Cannot retrieve the output parameter before accessing the database.");
            }
        }

        return outputParam;
    }

    /**
     * Abstract method to be implemented by subclasses to define DB access logic.
     *
     * @throws Exception if any issue occurs during the DB operation
     */
    protected abstract void doAccess() throws Exception;
}
