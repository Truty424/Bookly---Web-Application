<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.List" %>

<html>
<head>
    <title>Checkout - Bookly</title>
    <%@ include file="/html/cdn.html" %>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/base/root.css" type="text/css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/base/globals.css" type="text/css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/pages/checkout.css" type="text/css" />
</head>
<body>
<%@ include file="/html/header.html" %>

<div class="checkout-grid-container">
    <!-- LEFT SIDE: BOOK CARDS -->
    <div class="checkout-left">
        <h2 class="section-title">Your Cart</h2>
        <c:forEach var="book" items="${cart_books}">
            <div class="cart-book-cell">
                <img src="${pageContext.request.contextPath}/load-book-img?bookId=${book.bookId}" class="book-image" alt="Cover of ${book.title}" />
                <div class="book-details">
                    <div class="book-title">${book.title}</div>
                    <div class="book-authors">
                        <c:choose>
                            <c:when test="${not empty authors_map[book.bookId]}">
                                <c:forEach var="author" items="${authors_map[book.bookId]}" varStatus="loop">
                                    ${author.name}<c:if test="${!loop.last}">, </c:if>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>Unknown</c:otherwise>
                        </c:choose>
                    </div>
                    <div class="book-price">€${book.price}</div>
                </div>
            </div>
        </c:forEach>
    </div>

    <!-- RIGHT SIDE: SHIPPING + PAYMENT -->
    <div class="checkout-right">
        <h2 class="section-title">Shipping Information</h2>
        <form method="post" action="${pageContext.request.contextPath}/checkout">
            <label for="address">Address:</label>
            <textarea id="address" name="address" required rows="3" placeholder="Enter delivery address"></textarea>

            <label for="paymentMethod">Payment Method:</label>
            <select id="paymentMethod" name="paymentMethod" required>
                <option value="">-- Select --</option>
                <option value="credit_card">Credit Card</option>
                <option value="in_person">Pay on Delivery</option>
            </select>

            <!-- 💰 Total in styled box -->
            <div class="total-price-box">
                <p><strong>Total:</strong> €${total_price}</p>
            </div>

            <button class="order-button" type="submit">Order Now</button>
        </form>
    </div>
</div>

<%@ include file="/html/footer.html" %>
</body>
</html>
