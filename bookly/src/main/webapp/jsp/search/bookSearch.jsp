<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Search Books</title>
</head>
<body>
    <%@ include file="/html/cdn.html" %> 
    <%@ include file="/html/header.html" %>
    <h2>Search for a Book</h2>
    <form action="searchBook" method="get">
        <input type="text" name="title" placeholder="Enter book title" />
        <input type="submit" value="Search" />
    </form>
    <%@ include file="/html/footer.html" %>
</body>
</html>
