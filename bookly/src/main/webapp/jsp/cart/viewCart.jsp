<%@ page contentType="text/html;charset=UTF-8" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> <%@ page
isELIgnored="false" %>

<html>
  <head>
    <title>Your Cart</title>
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
      <div class="cart-items">
        <h1>Your Cart</h1>
        <c:forEach var="book" items="${cart_books}">
          <div class="book-card-horizontal">
            <div class="book-image">
              <img
                src="${pageContext.request.contextPath}/load-book-img?bookId=${book.bookId}"
                alt="Cover of ${book.title}"
              />
            </div>
            <div class="book-info">
              <h2><strong>${book.title}</strong></h2>
              <p>
                <c:choose>
                  <c:when test="${not empty authors}">
                    <c:forEach var="author" items="${authors}" varStatus="loop">
                      ${author.name}<c:if test="${!loop.last}">, </c:if>
                    </c:forEach>
                  </c:when>
                  <c:otherwise>Unknown</c:otherwise>
                </c:choose>
              </p>
              <p class="book-price">€${book.price}</p>
              <form
                action="${pageContext.request.contextPath}/cart/remove/${book.bookId}"
                method="post"
              >
                <button type="submit" class="btn-cart">Remove from cart</button>
              </form>
            </div>
          </div>
        </c:forEach>
      </div>

      <!-- RIGHT: Summary & Checkout -->
      <div class="cart-summary">
        <form
          method="post"
          action="${pageContext.request.contextPath}/cart/apply-discount"
        >
          <input
            type="text"
            name="discount_code"
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
            ${applied_discount.discountRate * 100}% off
          </p>
        </c:if>

        <p><strong>Total:</strong> €${total_price}</p>
        <p><strong>Final Total:</strong> €${final_total}</p>

        <form action="${pageContext.request.contextPath}/checkout" method="get">
          <button type="submit" class="btn-cart">Go to Checkout</button>
        </form>

        <a href="${pageContext.request.contextPath}/book" class="btn-cart"
          >Continue Shopping</a
        >
      </div>
    </div>

    <%@ include file="/html/footer.html" %>
  </body>
</html>
