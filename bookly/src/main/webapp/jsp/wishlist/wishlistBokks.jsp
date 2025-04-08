<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="it.unipd.bookly.Resource.Book" %>
<html>
<head>
    <title>Books in Wishlist</title>
    <style>
        body {
            font-family: Arial, sans-serif;
        }
        h1 {
            color: #4CAF50;
        }
        .book-item {
            margin: 10px;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
        }
        .book-item a {
            text-decoration: none;
            color: #007bff;
        }
        .book-item a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>

<h1>Books in Wishlist</h1>

<% 
    List<Book> books = (List<Book>) request.getAttribute("wishlist_books");
    int wishlistId = (int) request.getAttribute("wishlist_id");

    if (books != null && !books.isEmpty()) {
        for (Book book : books) {
%>
            <div class="book-item">
                <strong><a href="<%= request.getContextPath() %>/book/<%= book.getBookId() %>"><%= book.getTitle() %></a></strong><br/>
                <em><%= book.getAuthor() %></em><br/>
                <a href="<%= request.getContextPath() %>/wishlist/removeBook?wishlistId=<%= wishlistId %>&bookId=<%= book.getBookId() %>">Remove from Wishlist</a>
            </div>
<%
        }
    } else {
%>
        <p>No books found in this wishlist.</p>
<%
    }
%>

<a href="<%= request.getContextPath() %>/wishlist/user/<%= wishlistId %>">Back to Wishlists</a>

</body>
</html>
