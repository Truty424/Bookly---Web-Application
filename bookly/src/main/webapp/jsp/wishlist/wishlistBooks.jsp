<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="it.unipd.bookly.Resource.Book" %>
<%@ page import="java.util.List" %>

<html>
<head>
    <title>My Wishlist</title>
</head>
<body>
    <%@ include file="/html/cdn.html" %> 
    <%@ include file="/html/header.html" %>
<h1>My Wishlist</h1>

<%
    Integer wishlistId = (Integer) request.getAttribute("wishlist_id");
    List<Book> books = (List<Book>) request.getAttribute("wishlist_books");
%>

<% if (books != null && !books.isEmpty()) { %>
    <ul class="book-list">
        <% for (Book book : books) { %>
            <li class="book-item">
                <a href="<%= request.getContextPath() %>/book?id=<%= book.getBookId() %>"><%= book.getTitle() %></a>
                <p>Price: €<%= book.getPrice() %></p>
                <form action="<%= request.getContextPath() %>/wishlist/remove/<%= book.getBookId() %>" method="post" style="display:inline;">
                    <button type="submit">Remove from wishlist</button>
                </form>
            </li>
        <% } %>
    </ul>
<% } else { %>
    <p>Your wishlist is currently empty.</p>
<% } %>

<form action="<%= request.getContextPath() %>/" method="get">
    <button type="submit">Go to Home</button>
</form>
<%@ include file="/html/footer.html" %>

</body>
</html>
