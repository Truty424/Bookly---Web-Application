<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Add Discount</title>
</head>
<body>

<h2>Create Discount Code</h2>

<form action="<%= request.getContextPath() %>/admin/addDiscount" method="post">
    <label>Code: <input type="text" name="code" required /></label><br />
    <label>Percentage: <input type="number" name="percentage" step="0.01" min="0" max="100" required /></label><br />
    <label>Expiration Date (yyyy-mm-dd): <input type="text" name="expiredDate" required /></label><br /><br />
    <button type="submit">Add Discount</button>
</form>

<p><a href="<%= request.getContextPath() %>/admin/dashboard">Back to Dashboard</a></p>

</body>
</html>
