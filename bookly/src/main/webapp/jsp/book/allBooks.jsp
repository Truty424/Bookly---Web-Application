<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="it.unipd.bookly.Resource.Book" %>
<%@ page import="java.util.List" %>
<html>
<head>
    <title>All Books</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/base/root.css" type="text/css" />
    <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/base/globals.css" type="text/css" />
    <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/components/header.css" type="text/css" />
    <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/pages/allBooks.css" type="text/css" />
</head>
<body>
    <%@ include file="/html/header.html" %>
    <div class="container">
        <h1 class="page-title">All Books</h1>
        <div class="books-grid">
            <%
                List<Book> books = (List<Book>) request.getAttribute("all_books");
                if (books != null) {
                    for (Book book : books) {
            %>
                        <div class="book-card">
                            <img src="<%= request.getContextPath() %>/load-book-img?bookId=<%= book.getBookId() %>" alt="<%= book.getTitle() %>" class="book-image" />
                            <h3 class="book-title">
                                <a href="<%= request.getContextPath() %>/book/<%= book.getBookId() %>" class="book-title">
                                    <%= book.getTitle() %>
                                </a>
                            </h3>
                        </div>
            <%
                    }
                } else {
            %>
                <p class="no-books">No books found.</p>
            <%
                }
            %>
        </div>
        <form action="<%= request.getContextPath() %>/" method="get" class="back-home-form">
            <button type="submit" class="btn btn-primary">Go to Home</button>
        </form>
    </div>
    <%@ include file="/html/footer.html" %>
</body>
</html>