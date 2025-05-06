<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="it.unipd.bookly.Resource.Book" %>
<%@ page import="it.unipd.bookly.Resource.Author" %>
<%@ page import="it.unipd.bookly.dao.author.GetAuthorByIdDAO" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.util.List" %>
<html>
<head>
    <title>Books by Author</title>
    <%@ include file="/html/cdn.html" %>
    <script src="<%= request.getContextPath() %>/static/js/format-number.js"></script>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/base/root.css" type="text/css" />
    <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/base/globals.css" type="text/css" />
    <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/components/header.css" type="text/css" />
    <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/components/footer.css" type="text/css" />
    <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/pages/listBooks.css" type="text/css"/>
</head>
<body>
    <%@ include file="/html/header.html" %>
    <div class="container">
        <%
            String pathInfo = request.getPathInfo();
            int authorId = (pathInfo != null && pathInfo.length() > 1) ? Integer.parseInt(pathInfo.substring(1)) : -1;

            String authorName = "Unknown Author";
            try (Connection con = (Connection) request.getAttribute("connection")) {
                if (authorId != -1) {
                    GetAuthorByIdDAO dao = new GetAuthorByIdDAO(con, authorId);
                    dao.access();
                    Author author = dao.getOutputParam();
                    if (author != null) {
                        authorName = author.getFirstName() + " " + author.getLastName();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        %>
        <h1 class="page-title">Books by <%= authorName %> </h1>
        <div class="books-grid">
            <%
                List<Book> books = (List<Book>) request.getAttribute("author_books");
                if (books != null && !books.isEmpty()) {
                    for (Book book : books) {
            %>
                        <div class="book-card">
                            <div class="book-placeholder">
                                <!-- Placeholder for book image -->
                                <p>No Image</p>
                            </div>
                            <h3 class="book-title"><%= book.getTitle() %></h3>
                            <div class="book-details">
                                <p><strong>Publisher:</strong> <%= book.getEdition() %></p>
                                <p><strong>Price:</strong> $<%= book.getPrice() %></p>
                                <p><strong>Language:</strong> <%= book.getLanguage() %></p>
                                <p><strong>Rating:</strong> <%= book.getAverage_rate() %>/5</p>
                            </div>
                        </div>
            <%
                    }
                } else {
            %>
                <p class="no-books">No books found by this author.</p>
            <%
                }
            %>
        </div>
        <a href="<%= request.getContextPath() %>/author" class="back-link">Back to all authors</a>
    </div>
</body>
</html>