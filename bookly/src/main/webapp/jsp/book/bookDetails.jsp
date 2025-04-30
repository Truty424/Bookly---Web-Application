<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="it.unipd.bookly.Resource.Book" %>
<%@ page import="it.unipd.bookly.Resource.Author" %>
<%@ page import="it.unipd.bookly.Resource.Review" %>
<%@ page import="java.util.List" %>
<html>
<head>
    <title>Book Details</title>
    <%@ include file="/html/cdn.html" %> 
</head>
<body>

    <%@ include file="/html/header.html" %>
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

        <form action="<%= request.getContextPath() %>/cart/add/<%= book.getBookId() %>" method="post" style="margin-top: 20px;">
            <button type="submit">Add to Cart</button>
        </form>

        <form action="<%= request.getContextPath() %>/wishlist/add/<%= book.getBookId() %>" method="post" style="margin-top: 10px;">
            <button type="submit"> Add to Wishlist</button>
        </form>

        <h2>Reviews</h2>
        <%
            List<Review> reviews = (List<Review>) request.getAttribute("reviews");
            if (reviews != null && !reviews.isEmpty()) {
        %>
            <ul>
                <%
                    for (it.unipd.bookly.Resource.Review review : reviews) {
                %>
                    <li>
                        <p><strong>Rating:</strong> <%= review.getRating() %> / 5</p>
                        <p><strong>Comment:</strong> <%= review.getReviewText() %></p>
                        <p><em>By User ID:</em> <%= review.getUserId() %></p>
                        <p><em>Date:</em> <%= review.getReviewDate() %></p>
                        <p>👍 <%= review.getNumberOfLikes() %> | 👎 <%= review.getNumberOfDislikes() %></p>
                        <hr/>
                    </li>
                <%
                    }
                %>
            </ul>
        <%
            } else {
        %>
            <p>No reviews available for this book.</p>
        <%
            }
        %>

 
    
    <a href="<%= request.getContextPath() %>/book">Back to all books</a>
    <%@ include file="/html/footer.html" %>
</body>
</html>










