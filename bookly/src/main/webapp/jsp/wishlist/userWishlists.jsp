<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="it.unipd.bookly.Resource.Wishlist" %>
<html>
<head>
    <title>Your Wishlists</title>
</head>
<body>

<h1>Your Wishlists</h1>

<%
    List<Wishlist> wishlists = (List<Wishlist>) request.getAttribute("user_wishlists");
    if (wishlists != null && !wishlists.isEmpty()) {
        for (Wishlist wishlist : wishlists) {
%>
            <p>
                <a href="<%= request.getContextPath() %>/wishlist/<%= wishlist.getWishlistId() %>">
                    Wishlist #<%= wishlist.getWishlistId() %> — Created: <%= wishlist.getCreatedAt() %>
                </a>
            </p>
<%
        }
    } else {
%>
        <p>No wishlists found.</p>
<%
    }
%>

</body>
</html>
