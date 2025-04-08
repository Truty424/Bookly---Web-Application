<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="it.unipd.bookly.Resource.Wishlist" %>
<html>
<head>
    <title>Your Wishlists</title>
    <style>
        body {
            font-family: Arial, sans-serif;
        }
        h1 {
            color: #4CAF50;
        }
        .wishlist-item {
            margin: 10px;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
        }
        .wishlist-item a {
            text-decoration: none;
            color: #007bff;
        }
        .wishlist-item a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>

<h1>Your Wishlists</h1>

<% 
    List<Wishlist> wishlists = (List<Wishlist>) request.getAttribute("user_wishlists");
    if (wishlists != null && !wishlists.isEmpty()) {
        for (Wishlist wishlist : wishlists) {
%>
            <div class="wishlist-item">
                <a href="<%= request.getContextPath() %>/wishlist/<%= wishlist.getWishlistId() %>">Wishlist: <%= wishlist.getName() %></a>
            </div>
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
