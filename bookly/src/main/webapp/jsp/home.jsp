<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="it.unipd.bookly.Resource.Book" %>
<%@ page import="it.unipd.bookly.Resource.Category" %>
<%@ page import="java.util.List" %>
<%@ page isELIgnored="false" %>

<html>
<head>
    <title>Home - Bookly</title>
</head>
<body>

<h1>Welcome to Bookly</h1>

<!-- Categories -->
<h2>Categories</h2>
<ul>
    <c:forEach var="category" items="${categories}">
        <li>${category.category_name}</li>
    </c:forEach>
</ul>

<!-- Top Rated Books -->
<h2>Top Rated Books (4+ stars)</h2>
<c:forEach var="book" items="${topRatedBooks}">
    <div>
        <h3>${book.title}</h3>
        <p>Rating: ${book.average_rate}</p>
        <p>Price: €${book.price}</p>
    </div>
    <hr/>
</c:forEach>

<!-- All Books -->
<h2>All Books</h2>
<c:forEach var="book" items="${allBooks}">
    <div>
        <h3>${book.title}</h3>
        <p><strong>Author(s):</strong>
            <c:choose>
                <c:when test="${not empty bookAuthors[book.bookId]}">
                    <c:forEach var="author" items="${bookAuthors[book.bookId]}" varStatus="loop">
                        ${author.first_name} ${author.last_name}<c:if test="${!loop.last}">, </c:if>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    Unknown
                </c:otherwise>
            </c:choose>
        </p>
        <p>Rating: ${book.average_rate}</p>
        <p>Price: €${book.price}</p>
    </div>
    <hr/>
</c:forEach>

</body>
</html>
