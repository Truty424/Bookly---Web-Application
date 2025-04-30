<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>All Books</title>
    <meta charset="UTF-8" />
    <%@ include file="/html/cdn.html" %>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/base/root.css" type="text/css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/base/globals.css" type="text/css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/base/allBooks.css" type="text/css" />
</head>
<body>
    <%@ include file="/html/header.html" %>

    <div class="container">
        <h2 class="all-books-title">All Books</h2>

        <div class="books-grid">
            <c:choose>
                <c:when test="${not empty all_books}">
                    <c:forEach var="book" items="${all_books}">
                        <div class="book-card">
                            <img src="${pageContext.request.contextPath}/load-book-img?bookId=${book.bookId}"
                                 alt="${book.title}" class="book-image" />
                            <h3 class="book-title">
                                <a href="${pageContext.request.contextPath}/book/${book.bookId}" class="book-title">
                                    ${book.title}
                                </a>
                            </h3>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <p class="no-books-message">No books found.</p>
                </c:otherwise>
            </c:choose>
        </div>

    </div>

    <%@ include file="/html/footer.html" %>
</body>
</html>
