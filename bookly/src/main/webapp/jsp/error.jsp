<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Error</title>
</head>
<body>
    <h1>Oops! Something went wrong.</h1>
    <p><strong>Error:</strong> ${sessionScope.errorMessage}</p>

    <a href="<%= request.getContextPath() %>/">Back to Home</a>
</body>
</html>
