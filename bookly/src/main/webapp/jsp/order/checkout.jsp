<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="it.unipd.bookly.Resource.Book" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.List" %>

<html>
<head>
    <title>Checkout - Bookly</title>
</head>
<body>

<h1>Checkout</h1>

<c:if test="${not empty cart_books}">
    <div>
        <h2>Your Cart</h2>
        <c:forEach var="book" items="${cart_books}">
            <div class="book-item">
                <img src="${pageContext.request.contextPath}/book/image?bookId=${book.bookId}" alt="Book Cover">
                <div>
                    <div class="book-title">${book.title}</div>
                    <div>${book.language}</div>
                </div>
                <div class="price">€${book.price}</div>
            </div>
        </c:forEach>
        <p><strong>Total:</strong> €${total_price}</p>
    </div>
</c:if>

<div class="checkout-form">
    <form method="post" action="${pageContext.request.contextPath}/checkout">
        <h3>Shipping Information</h3>
        <label>Address:</label><br/>
        <textarea name="address" required rows="3" placeholder="Enter delivery address"></textarea><br/>

        <label>Payment Method:</label><br/>
        <select name="paymentMethod" required>
            <option value="">-- Select --</option>
            <option value="credit_card">Credit Card</option>
            <option value="in_person">Pay on Delivery</option>
        </select><br/>
    </form>

    <form action="<%= request.getContextPath() %>/orders" method="post">
        <button type="submit">Order Now</button>
    </form>
</div>

</body>
</html>
