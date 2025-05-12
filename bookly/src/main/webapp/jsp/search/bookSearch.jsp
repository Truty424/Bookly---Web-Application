<%@ page contentType="text/html;charset=UTF-8" %>
<html>
  <head>
    <title>Search Books</title>
    <%@ include file="/html/cdn.html" %>
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/static/css/base/root.css"
      type="text/css"
    />
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/static/css/base/globals.css"
      type="text/css"
    />
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/static/css/pages/book-search.css"
      type="text/css"
    />
  </head>
  <body>
    <%@ include file="/html/header.html" %>
    <main class="container">
      <h2>Search for a Book</h2>
      <form action="search" method="get">
        <input type="text" name="query" placeholder="Enter book title" />
        <input type="submit" value="Search" />
      </form>
    </main>
    <%@ include file="/html/footer.html" %>
    <script src="${pageContext.request.contextPath}/static/js/main.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/header.js"></script>
  </body>
</html>
