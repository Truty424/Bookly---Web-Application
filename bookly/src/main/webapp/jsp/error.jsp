<%@ page contentType="text/html;charset=UTF-8" %>
<html>
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Error</title>

    <!-- Base styles -->
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
    <%@ include file="/html/cdn.html" %>
  </head>
  <body>
    <%@ include file="/html/header.html" %>
    <h1>Oops! Something went wrong.</h1>
    <p><strong>Error:</strong> ${sessionScope.errorMessage}</p>

    <%@ include file="/html/footer.html" %>
  </body>
</html>
