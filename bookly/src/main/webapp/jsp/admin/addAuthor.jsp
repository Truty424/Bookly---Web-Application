<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Add Author</title>
</head>
<body>

<h2>Add New Author</h2>

<form action="<%= request.getContextPath() %>/admin/addAuthor" method="post">
    <label>First Name: <input type="text" name="firstName" required /></label><br />
    <label>Last Name: <input type="text" name="lastName" required /></label><br />
    <label>Nationality: <input type="text" name="nationality" required /></label><br />
    <label>Biography: <textarea name="biography" rows="4" cols="30"></textarea></label><br /><br />
    <button type="submit">Add Author</button>
</form>

<p><a href="<%= request.getContextPath() %>/admin/dashboard">Back to Dashboard</a></p>

</body>
</html>
