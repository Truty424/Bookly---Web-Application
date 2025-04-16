<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Change Password</title>
</head>
<body>
    <h2>Change Password</h2>
    <form action="${pageContext.request.contextPath}/user/changePassword" method="post">
        <input type="password" name="newPassword" placeholder="New Password" required />
        <button type="submit">Update</button>
    </form>
    
    <c:if test="${not empty error_message}">
        <p style="color: red;">${error_message}</p>
    </c:if>
    <c:if test="${not empty success_message}">
        <p style="color: green;">${success_message}</p>
    </c:if>
</body>
</html>