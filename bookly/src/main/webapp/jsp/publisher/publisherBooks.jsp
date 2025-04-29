<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%@ page
contentType="text/html;charset=UTF-8" %>
<html>
  <head>
    <title>Books by Publisher</title>
    <%@ include file="/html/cdn.html" %>
  </head>
  <body>
    <%@ include file="/html/header.html" %>
    <h1>Books from Publisher ID: ${publisher_id}</h1>
    <ul>
      <c:forEach var="book" items="${publisher_books}">
        <li>${book.title} - ${book.isbn}</li>
      </c:forEach>
    </ul>

    <%@ include file="/html/footer.html" %>
  </body>
</html>
