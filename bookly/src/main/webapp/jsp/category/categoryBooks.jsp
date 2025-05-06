<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="it.unipd.bookly.Resource.Book" %>
<%@ page import="java.util.List" %>
<html>
<head>
    <title>Books in Category</title>
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
        <h1 class="page-title">Books in Selected Category</h1>
        <div class="books-grid">
            <%
                List<Book> books = (List<Book>) request.getAttribute("category_books");
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
                <p class="no-books">No books found in this category.</p>
            <%
                }
            %>
        </div>
        <a href="<%= request.getContextPath() %>/category" class="back-link">Back to all categories</a>
    </div>
</body>
</html>