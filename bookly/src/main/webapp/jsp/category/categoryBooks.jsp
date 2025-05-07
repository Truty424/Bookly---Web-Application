<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Books in Category</title>
    <%@ include file="/html/cdn.html" %>
    <script src="${pageContext.request.contextPath}/static/js/format-number.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/base/root.css" type="text/css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/base/globals.css" type="text/css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/pages/listBooks.css" type="text/css" />
</head>
<body>
    <%@ include file="/html/header.html" %>
    <div class="container">
        <h1 class="page-title">Books in Selected Category</h1>
        <div class="books-grid">
            <c:choose>
                <c:when test="${not empty category_books}">
                    <c:forEach var="book" items="${category_books}">
                        <div class="book-card">
                            <div class="book-placeholder">
                                <!-- Placeholder for book image -->
                                <p>No Image</p>
                            </div>
                            <h3 class="book-title">${book.title}</h3>
                            <div class="book-details">
                                <p><strong>Publisher :</strong> ${book.edition}</p>
                                <p><strong>Price :</strong> $${book.price}</p>
                                <p><strong>Language :</strong> ${book.language}</p>
                                <p><strong>Average Rating :</strong> ${book.average_rate}/5</p>
                            </div>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <p class="no-books">No books found in this category.</p>
                </c:otherwise>
            </c:choose>
        </div>
        <div class="back-link-container">
            <a href="${pageContext.request.contextPath}/category" class="back-link">Back to all categories</a>
        </div>
    </div>
</body>
</html>