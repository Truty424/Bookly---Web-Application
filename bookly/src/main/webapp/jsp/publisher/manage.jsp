<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head><title>Manage Publishers</title></head>
<body>
<h1>Publisher Management</h1>

<h2>Create Publisher</h2>
<form action="${pageContext.request.contextPath}/admin/publishers" method="post">
    <input type="hidden" name="action" value="create"/>
    Name: <input type="text" name="name"/><br/>
    Phone: <input type="text" name="phone"/><br/>
    Address: <input type="text" name="address"/><br/>
    <button type="submit">Create</button>
</form>

<hr/>

<h2>Update/Delete Publishers</h2>
<c:forEach var="publisher" items="${publishers}">
    <form action="${pageContext.request.contextPath}/admin/publishers" method="post">
        <input type="hidden" name="publisherId" value="${publisher.publisherId}"/>
        Name: <input type="text" name="name" value="${publisher.publisherName}"/>
        Phone: <input type="text" name="phone" value="${publisher.phone}"/>
        Address: <input type="text" name="address" value="${publisher.address}"/>
        <button type="submit" name="action" value="update">Update</button>
        <button type="submit" name="action" value="delete">Delete</button>
    </form>
</c:forEach>

</body>
</html>
