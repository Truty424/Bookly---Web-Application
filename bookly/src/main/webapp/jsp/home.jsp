<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
        <!-- <p>Author: ${book.author_name}</p> -->
        <p>Rating: ${book.average_rate}</p>
        <p>Price: €${book.price}</p>
    </div>
    <hr/>
</c:forEach>

</body>
</html>
