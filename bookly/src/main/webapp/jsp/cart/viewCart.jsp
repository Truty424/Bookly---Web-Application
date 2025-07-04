<%@ page contentType="text/html;charset=UTF-8" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> <%@ page
isELIgnored="false" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<html>
  <head>
    <title>Your Cart</title>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <%@ include file="/html/cdn.html" %>
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/static/css/base/root.css"
      type="text/css"
    />
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/static/css/base/globals.css"
      type="text/css"
    />
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/static/css/pages/cart.css"
      type="text/css"
    />
  </head>
  <body>
    <%@ include file="/html/header.html" %>

    <div class="cart-container">
      <!-- LEFT: Book Items -->
      <main class="cart-items">
        <h2>Your Cart</h2>
        <c:forEach var="book" items="${cart_books}">
          <div class="book-card-horizontal">
            <div class="book-image">
              <img
                src="${pageContext.request.contextPath}/static/img/book/${book.bookId}.jpg"
                alt="Cover of ${book.title}"
                onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/static/img/book/default.jpg';"
              />
            </div>
            <div class="book-info">
              <h2><strong>${book.title}</strong></h2>
              <p>
                <c:choose>
                  <c:when test="${not empty authors_map[book.bookId]}">
                    <c:forEach var="author" items="${authors_map[book.bookId]}" varStatus="loop">
                      ${author.firstName} ${author.lastName}<c:if test="${!loop.last}">, </c:if>
                    </c:forEach>
                  </c:when>
                  <c:otherwise>Unknown</c:otherwise>
                </c:choose>
              </p>
              <div class="book-price"><span data-format="price">${book.price}</span></div>
              <form
                action="${pageContext.request.contextPath}/cart/remove/${book.bookId}"
                method="post"
              >
                <button type="submit" class="btn-cart">Remove from cart</button>
              </form>
            </div>
          </div>
        </c:forEach>
      </main>

      <!-- RIGHT: Summary & Checkout -->
      <div class="cart-summary">
        <a href="${pageContext.request.contextPath}/book" class="back-cart">
          <i class="fas fa-arrow-left"></i> Back to market
        </a>
        <form
          class="d-flex flex-column justify-content-center align-items-center"
          method="post"
          action="${pageContext.request.contextPath}/cart/apply-discount"
        >
          <input
            type="text"
            name="discount"
            placeholder="Discount code"
            required
          />
          <button type="submit" class="btn-cart">Apply Discount</button>
        </form>

        <c:if test="${not empty discount_error}">
          <p class="text-danger">${discount_error}</p>
        </c:if>

        <c:if test="${not empty applied_discount}">
          <p class="text-success">
            Discount Applied: ${applied_discount.code} -
            ${applied_discount.discountPercentage}% OFF
          </p>
        </c:if>

        <p><strong>Total:</strong> <span data-format="price">${total_price}</span> </p>
        <p><strong>Final Total:</strong> <span data-format='price'>${final_total}</span> </p>


        <form action="${pageContext.request.contextPath}/checkout" method="get">
          <button type="submit" class="btn-cart w-100">Go to Checkout</button>
        </form>
      </div>
    </div>

    <%@ include file="/html/footer.html" %>
    <script src="${pageContext.request.contextPath}/static/js/main.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/header.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/format-number.js"></script>
  </body>
</html>
