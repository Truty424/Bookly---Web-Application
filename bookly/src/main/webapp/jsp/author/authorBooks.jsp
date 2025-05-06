<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="it.unipd.bookly.Resource.Author" %>
<%@ page import="it.unipd.bookly.dao.author.GetAuthorByIdDAO" %>
<%@ page import="java.sql.Connection" %>
<html>
<head>
    <title>Books by Author</title>
    <%@ include file="/html/cdn.html" %>
    <script src="<%= request.getContextPath() %>/static/js/format-number.js"></script>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/base/root.css" type="text/css" />
    <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/base/globals.css" type="text/css" />
    <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/components/header.css" type="text/css" />
    <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/components/footer.css" type="text/css" />
    <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/pages/allBooks.css" type="text/css" />
</head>
<body>
    <%@ include file="/html/header.html" %>
    <div class="container py-5">
        <%
            String pathInfo = request.getPathInfo();
            int authorId = (pathInfo != null && pathInfo.length() > 1) ? Integer.parseInt(pathInfo.substring(1)) : -1;

            String authorName = "Unknown Author";
            try (Connection con = (Connection) request.getAttribute("connection")) {
                if (authorId != -1) {
                    GetAuthorByIdDAO dao = new GetAuthorByIdDAO(con, authorId);
                    dao.access();
                    Author author = dao.getOutputParam();
                    if (author != null) {
                        authorName = author.getFirstName() + " " + author.getLastName();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        %>
        <h1 class="page-title">Books by <%= authorName %></h1>
        <div class="books-grid">
            <c:choose>
                <c:when test="${not empty author_books}">
                    <c:forEach var="book" items="${author_books}">
                        <a href="${pageContext.request.contextPath}/book/${book.bookId}" class="book-title">
                            <div class="book-card">
                                <img src="${pageContext.request.contextPath}/load-book-img?bookId=${book.bookId}" 
                                     alt="${book.title}" class="book-image" />
                                <h3 class="book-title">${book.title}</h3>
                            </div>
                        </a>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <p class="no-books-message">No books found by this author.</p>
                </c:otherwise>
            </c:choose>
        </div>
        <a href="${pageContext.request.contextPath}/author" class="back-link">Back to all authors</a>
    </div>
    <%@ include file="/html/footer.html" %>
</body>
</html>