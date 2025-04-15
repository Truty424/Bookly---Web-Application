<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Add Publisher</title>
</head>
<body>

<h2>Add New Publisher</h2>

<form action="<%= request.getContextPath() %>/admin/addPublisher" method="post">
    <label>Publisher Name: <input type="text" name="publisher_name" required /></label><br />
    <label>Phone: <input type="text" name="phone" /></label><br />
    <label>Address: <textarea name="address" rows="3" cols="30"></textarea></label><br /><br />
    <button type="submit">Add Publisher</button>
</form>

<p><a href="<%= request.getContextPath() %>/admin/dashboard">Back to Dashboard</a></p>

</body>
</html>
