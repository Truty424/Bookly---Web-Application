<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="it.unipd.bookly.Resource.Book" %>
<html>
<head>
    <title>Book Details</title>
</head>
<body>
    <h1>Book Details</h1>

    <%
        Book book = (Book) request.getAttribute("book_details");
        if (book != null) {
    %>
        <h2><%= book.getTitle() %></h2>
        <p><strong>ISBN:</strong> <%= book.getIsbn() %></p>
        <p><strong>Price:</strong> $<%= book.getPrice() %></p>
        <p><strong>Language:</strong> <%= book.getLanguage() %></p>
        <p><strong>Author:</strong> <%= book.getAuthorName() %></p>
    <%
        } else {
    %>
        <p>Book details not found.</p>
    <%
        }
    %>
    <a href="<%= request.getContextPath() %>/books">Back to all books</a>
</body>
</html>
