<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>All Publishers</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/base/root.css" type="text/css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/base/globals.css" type="text/css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/pages/allPublishers.css" type="text/css" />
    <%@ include file="/html/cdn.html" %>
</head>
<body>
    <%@ include file="/html/header.html" %>
    <div class="container">
        <h1 class="page-title">All Publishers</h1>
        <div class="publishers-grid">
            <c:choose>
                <c:when test="${not empty all_publishers}">
                    <c:forEach var="publisher" items="${all_publishers}">
                        <div class="publisher-card">
                            <h3 class="publisher-name">
                                <a href="${pageContext.request.contextPath}/publisher/${publisher.publisherId}">
                                    ${publisher.publisherName}
                                </a>
                            </h3>
                            <p class="publisher-phone"><strong>Phone:</strong> ${publisher.phone}</p>
                            <p class="publisher-address"><strong>Address:</strong> ${publisher.address}</p>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <p class="no-publishers-message">No publishers found.</p>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    <%@ include file="/html/footer.html" %>
</body>
</html>