<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/pages/book-search.css">
    <title>Search Books</title>
    <%@ include file="/html/cdn.html" %> 
</head>
<body>

    <%@ include file="/html/header.html" %>
    <div class="search-container">
    <h2>Search for a Book</h2>
    <form action="search" method="get">
        <input type="text" name="query" placeholder="Enter book title" />
        <input type="submit" value="Search" />
    </form>
    </div>
    <%@ include file="/html/footer.html" %>
</body>
</html>
