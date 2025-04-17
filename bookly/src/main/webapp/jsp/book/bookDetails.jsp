<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
    <h2>Reviews</h2>
<c:choose>
    <c:when test="${not empty reviews}">
        <ul>
            <c:forEach var="review" items="${reviews}">
                <li>
                    <strong>Rating:</strong> ${review.rating} / 5<br/>
                    <strong>Review:</strong> ${review.reviewText}<br/>
                    <em>By User ID: ${review.userId}</em>
                    <br/><br/>
                </li>
            </c:forEach>
        </ul>
    </c:when>
    <c:otherwise>
        <p>No reviews yet for this book.</p>
    </c:otherwise>
</c:choose>
    <a href="<%= request.getContextPath() %>/book">Back to all books</a>
</body>
</html>
