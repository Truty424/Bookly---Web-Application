package it.unipd.bookly.dao.book;

import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.Resource.Image;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static it.unipd.bookly.dao.book.BookQueries.INSERT_BOOK;
import static it.unipd.bookly.dao.book.BookQueries.INSERT_BOOK_IMAGE;

/**
 * DAO to insert a new book (with optional image).
 * This class provides functionality to add a new book record
 * to the database, including optional image data.
 */
public class InsertBookDAO extends AbstractDAO<Boolean> {

    /**
     * The {@link Book} object containing the details of the book to be inserted.
     */
    private final Book book;

    /**
     * Constructs a DAO to insert a new book (with optional image).
     *
     * @param con  The database connection to use.
     * @param book The {@link Book} object containing the details of the book to be inserted.
     * @throws Exception If an error occurs while setting up the DAO.
     */
    public InsertBookDAO(final Connection con, final Book book) throws Exception {
        super(con);
        this.book = book;
        con.setAutoCommit(false);
    }

    /**
     * Executes the query to insert a new book (with optional image) into the database.
     * If an image is provided, it is inserted into the database along with the book metadata.
     * Populates the {@link #outputParam} with the inserted {@link Book} object.
     *
     * @throws Exception If an error occurs during the database operation.
     */
    @Override
    protected void doAccess() throws Exception {

        try (PreparedStatement stmntImg = con.prepareStatement(INSERT_BOOK_IMAGE);
             PreparedStatement stmntBook = con.prepareStatement(INSERT_BOOK)) {

            Image image = book.getBook_pic();

            // Insert image if available
            if (image != null) {
                stmntImg.setString(1, book.getTitle());
                stmntImg.setBytes(2, image.getPhoto());
                stmntImg.setString(3, image.getPhotoMediaType());
                stmntImg.execute();
                LOGGER.info("Image for book '{}' added.", book.getTitle());
            }

            // Insert book metadata
            stmntBook.setString(1, book.getTitle());
            stmntBook.setString(2, book.getLanguage());
            stmntBook.setString(3, book.getIsbn());
            stmntBook.setDouble(4, book.getPrice());
            stmntBook.setString(5, book.getEdition());
            stmntBook.setInt(6, book.getPublication_year());
            stmntBook.setInt(7, book.getNumber_of_pages());
            stmntBook.setInt(8, book.getStockQuantity());
            stmntBook.setDouble(9, book.getAverage_rate());
            stmntBook.setString(10, book.getSummary());
            stmntBook.execute();

            LOGGER.info("Book '{}' inserted into the database.", book.getTitle());
            con.commit();

        } catch (Exception ex) {
            LOGGER.error("Error inserting book '{}': {}", book.getTitle(), ex.getMessage());
            con.rollback();
        } finally {
            con.setAutoCommit(true);
        }
    }
}