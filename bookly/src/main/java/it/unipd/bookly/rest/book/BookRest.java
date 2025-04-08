// package it.unipd.bookly.rest.book;

// import it.unipd.bookly.Resource.Book;
// import it.unipd.bookly.Resource.Message;
// import it.unipd.bookly.dao.book.*;
// import it.unipd.bookly.rest.AbstractRestResource;

// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;

// import java.io.IOException;
// import java.sql.Connection;
// import java.util.List;

// /**
//  * Handles core book operations:
//  * - GET /api/book?id=123
//  * - GET /api/books
//  * - POST /api/book
//  * - PUT /api/book
//  * - DELETE /api/book?id=123
//  */
// public class BookRest extends AbstractRestResource {

//     public BookRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
//         super("book", req, res, con);
//     }

//     @Override
//     protected void doServe() throws IOException {
//         final String method = req.getMethod();
//         final String path = req.getRequestURI();
//         final String idParam = req.getParameter("id");

//         try {
//             switch (method) {
//                 case "GET":
//                     if (idParam != null) {
//                         handleGetBookById(Integer.parseInt(idParam));
//                     } else {
//                         handleGetAllBooks();
//                     }
//                     break;
//                 case "POST":
//                     handleInsertBook();
//                     break;
//                 case "PUT":
//                     handleUpdateBook();
//                     break;
//                 case "DELETE":
//                     if (idParam != null) {
//                         handleDeleteBook(Integer.parseInt(idParam));
//                     } else {
//                         res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//                         new Message("Missing 'id' parameter for DELETE.", "E400", "ID is required to delete a book.").toJSON(res.getOutputStream());
//                     }
//                     break;
//                 default:
//                     res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
//                     new Message("Method not allowed", "E405", "Only GET, POST, PUT, DELETE are supported.").toJSON(res.getOutputStream());
//             }

//         } catch (Exception e) {
//             LOGGER.error("BookRest exception: ", e);
//             res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//             new Message("Internal error while handling book operation.", "E500", e.getMessage()).toJSON(res.getOutputStream());
//         }
//     }

//     private void handleGetBookById(int bookId) throws Exception {
//         Book book = new GetBookByIdDAO(con, bookId).access().getOutputParam();

//         if (book != null) {
//             res.setContentType("application/json;charset=UTF-8");
//             book.toJSON(res.getOutputStream());
//         } else {
//             res.setStatus(HttpServletResponse.SC_NOT_FOUND);
//             new Message("Book not found.", "E404", "No book with ID " + bookId).toJSON(res.getOutputStream());
//         }
//     }

//     private void handleGetAllBooks() throws Exception {
//         List<Book> books = new GetAllBooksDAO(con).access().getOutputParam();
//         res.setContentType("application/json;charset=UTF-8");
//         for (Book b : books) {
//             b.toJSON(res.getOutputStream());
//         }
//     }

//     private void handleInsertBook() throws Exception {
//         Book book = Book.fromJSON(req.getInputStream());
//         Book inserted = new InsertBookDAO(con, book).access().getOutputParam();
//         res.setStatus(HttpServletResponse.SC_CREATED);
//         inserted.toJSON(res.getOutputStream());
//     }

//     private void handleUpdateBook() throws Exception {
//         Book book = Book.fromJSON(req.getInputStream());
//         boolean updated = new UpdateBookDAO(con, book).access().getOutputParam();

//         if (updated) {
//             res.setStatus(HttpServletResponse.SC_OK);
//             new Message("Book updated successfully.", "200", "Book updated.").toJSON(res.getOutputStream());
//         } else {
//             res.setStatus(HttpServletResponse.SC_NOT_FOUND);
//             new Message("Update failed.", "E404", "Book not found or not updated.").toJSON(res.getOutputStream());
//         }
//     }

//     private void handleDeleteBook(int bookId) throws Exception {
//         boolean deleted = new DeleteBookDAO(con, bookId).access().getOutputParam();
//         if (deleted) {
//             res.setStatus(HttpServletResponse.SC_OK);
//             new Message("Book deleted successfully.", "200", "Book removed from system.").toJSON(res.getOutputStream());
//         } else {
//             res.setStatus(HttpServletResponse.SC_NOT_FOUND);
//             new Message("Book not found.", "E404", "No book with ID " + bookId).toJSON(res.getOutputStream());
//         }
//     }
// }
