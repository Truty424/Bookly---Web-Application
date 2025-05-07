<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>All Authors</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/base/root.css" type="text/css" />
    <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/base/globals.css" type="text/css" />
    <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/pages/allAuthors.css" type="text/css" />
    <%@ include file="/html/cdn.html" %>
</head>
<body>
    <%@ include file="/html/header.html" %>
    <div class="container">
        <h1 class="page-title">Book Authors</h1>
        <div class="authors-grid">
            <c:choose>
                <c:when test="${not empty all_authors}">
                    <c:forEach var="author" items="${all_authors}">
                        <div class="author-card">
                            <h3 class="author-name">
                                <a href="${pageContext.request.contextPath}/author/${author.authorId}">
                                    ${author.name}
                                </a>
                            </h3>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <p class="no-authors">No authors found.</p>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    <%@ include file="/html/footer.html" %>
</body>
</html>