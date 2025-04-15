<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="it.unipd.bookly.Resource.Book" %>
<%@ page import="java.util.List" %>

<html>
<head>
    <title>My Wishlist</title>
</head>
<body>

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
                <button class="remove-btn" onclick="removeFromWishlist(<%= wishlistId %>, <%= book.getBookId() %>)">Remove</button>
            </li>
        <% } %>
    </ul>
<% } else { %>
    <p>Your wishlist is currently empty.</p>
<% } %>

<script>
function removeFromWishlist(wishlistId, bookId) {
    fetch(`/api/wishlist/books/remove?wishlistId=${wishlistId}&bookId=${bookId}`, {
        method: "DELETE"
    }).then(res => {
        if (res.ok) {
            location.reload();
        } else {
            alert("Failed to remove item.");
        }
    });
}
</script>

<p><a href="<%= request.getContextPath() %>/wishlist/user">Back to My Wishlists</a></p>

</body>
</html>
