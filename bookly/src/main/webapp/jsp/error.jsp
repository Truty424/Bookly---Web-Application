<%@ page contentType="text/html;charset=UTF-8" %>
<html>
  <head>
    <title>Error</title>
  </head>
  <body>
    <%@ include file="/html/cdn.html" %> <%@ include file="/html/header.html" %>
    <h1>Oops! Something went wrong.</h1>
    <p><strong>Error:</strong> ${sessionScope.errorMessage}</p>

    <%@ include file="/html/footer.html" %>
  </body>
</html>
