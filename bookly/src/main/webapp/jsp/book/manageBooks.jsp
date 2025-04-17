<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head><title>Manage Books</title></head>
<body>
<h1>Book Management</h1>

<h2>Create Book</h2>
<form action="${pageContext.request.contextPath}/admin/books" method="post">
    <input type="hidden" name="action" value="create"/>
    Title: <input type="text" name="title"/><br/>
    ISBN: <input type="text" name="isbn"/><br/>
    Price: <input type="text" name="price"/><br/>
    Language: <input type="text" name="language"/><br/>
    Author ID: <input type="text" name="author_id"/><br/>
    <button type="submit">Create</button>
</form>

<hr/>

<h2>Update/Delete Books</h2>
<c:forEach var="book" items="${books}">
    <form action="${pageContext.request.contextPath}/admin/books" method="post">
        <input type="hidden" name="bookId" value="${book.bookId}"/>
        Title: <input type="text" name="title" value="${book.title}"/><br/>
        ISBN: <input type="text" name="isbn" value="${book.isbn}"/><br/>
        Price: <input type="text" name="price" value="${book.price}"/><br/>
        Language: <input type="text" name="language" value="${book.language}"/><br/>
        Author ID: <input type="text" name="author_id" value="${book.authorId}"/><br/>
        <button type="submit" name="action" value="update">Update</button>
        <button type="submit" name="action" value="delete">Delete</button>
    </form>
</c:forEach>

</body>
</html>
