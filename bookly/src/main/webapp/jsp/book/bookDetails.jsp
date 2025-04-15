<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="it.unipd.bookly.Resource.Book" %>
<%@ page import="it.unipd.bookly.Resource.Author" %>
<%@ page import="java.util.List" %>
<html>
<head>
    <title>Book Details</title>
</head>
<body>
    <h1>Book Details</h1>

    <%
        Book book = (Book) request.getAttribute("book_details");
        List<Author> authors = (List<Author>) request.getAttribute("authors");

        if (book != null) {
    %>
        <h2><%= book.getTitle() %></h2>
        <p><strong>ISBN:</strong> <%= book.getIsbn() %></p>
        <p><strong>Price:</strong> $<%= book.getPrice() %></p>
        <p><strong>Language:</strong> <%= book.getLanguage() %></p>
        <p><strong>Author(s):</strong>
        <%
            if (authors != null && !authors.isEmpty()) {
                for (int i = 0; i < authors.size(); i++) {
                    Author a = authors.get(i);
        %>
            <%= a.getName() %><%= (i < authors.size() - 1) ? ", " : "" %>
        <%
                }
            } else {
        %>
            Unknown
        <%
            }
        %>
        </p>
    <%
        } else {
    %>
        <p>Book details not found.</p>
    <%
        }
    %>
    <a href="<%= request.getContextPath() %>/book">Back to all books</a>
</body>
</html>
