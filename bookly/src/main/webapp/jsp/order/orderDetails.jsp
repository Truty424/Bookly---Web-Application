<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
  <title>Order Details</title>
  <%@ include file="/html/cdn.html" %>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/base/root.css" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/base/globals.css" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/pages/orderDetails.css" />
</head>
<body>
<%@ include file="/html/header.html" %>

<main class="checkout-grid-container">
  <!-- LEFT SIDE: BOOKS IN ORDER -->
  <div class="checkout-left">
    <h2 class="section-title">Your Order</h2>
    <c:forEach var="book" items="${order.books}">
      <div class="cart-book-cell">
        <img
          src="${pageContext.request.contextPath}/static/img/book/${book.bookId}.jpg"
          class="book-image"
          alt="Cover of ${book.title}"
          onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/static/img/book/default.jpg';"
        />
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
          <div class="book-price">
            <fmt:formatNumber value="${book.price}" type="number" minFractionDigits="2" />
          </div>
        </div>
      </div>
    </c:forEach>
  </div>

  <!-- RIGHT SIDE: ORDER INFO -->
  <div class="checkout-right">
    <h2 class="section-title">Order Summary</h2>
    <p><strong>Order ID:</strong> ${order.orderId}</p>
    <p><strong>Status:</strong> ${order.status}</p>
    <p><strong>Total Price:</strong> €<fmt:formatNumber value="${order.totalPrice}" type="number" minFractionDigits="2" /></p>
    <p><strong>Payment Method:</strong>
      <c:choose>
        <c:when test="${order.paymentMethod == 'in_person'}">Pay on Delivery</c:when>
        <c:when test="${order.paymentMethod == 'credit_card'}">Credit Card</c:when>
        <c:otherwise>${order.paymentMethod}</c:otherwise>
      </c:choose>
    </p>
    <p><strong>Address:</strong> ${order.address}</p>
    <p><strong>Shipment Code:</strong>
      <c:choose>
        <c:when test="${order.status == 'shipped'}">
          ${order.shipmentCode}
        </c:when>
        <c:otherwise>
          Shipment not yet generated
        </c:otherwise>
      </c:choose>
    </p>
    <p><strong>Order Date:</strong> <fmt:formatDate value="${order.orderDate}" pattern="yyyy-MM-dd HH:mm" /></p>
    <a href="${pageContext.request.contextPath}/orders" class="order-button">&larr; Back to Orders</a>
  </div>
</main>

<%@ include file="/html/footer.html" %>
<script src="${pageContext.request.contextPath}/static/js/main.js"></script>
<script src="${pageContext.request.contextPath}/static/js/header.js"></script>
</body>
</html>
