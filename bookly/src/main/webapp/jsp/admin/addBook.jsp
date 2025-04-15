<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Add New Book</title>
</head>
<body>

<h2>Add Book</h2>

<form action="<%= request.getContextPath() %>/admin/addBook" method="post">
    <label>Title: <input type="text" name="title" required /></label><br />
    <label>Language: <input type="text" name="language" required /></label><br />
    <label>ISBN: <input type="text" name="isbn" required /></label><br />
    <label>Price: <input type="number" name="price" step="0.01" required /></label><br />
    <label>Edition: <input type="text" name="edition" /></label><br />
    <label>Publication Year: <input type="number" name="publication_year" required /></label><br />
    <label>Number of Pages: <input type="number" name="number_of_pages" required /></label><br />
    <label>Stock Quantity: <input type="number" name="stock_quantity" required /></label><br />
    <label>Summary: <textarea name="summary" rows="4" cols="30"></textarea></label><br /><br />
    <button type="submit">Add Book</button>
</form>

<p><a href="<%= request.getContextPath() %>/admin/dashboard">Back to Dashboard</a></p>

</body>
</html>
