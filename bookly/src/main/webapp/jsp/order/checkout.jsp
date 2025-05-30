<%@ page contentType="text/html;charset=UTF-8" %>
  <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ page import="java.util.List" %>

      <html>

      <head>
        <title>Checkout - Bookly</title>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <%@ include file="/html/cdn.html" %>
          <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/base/root.css" type="text/css" />
          <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/base/globals.css"
            type="text/css" />
          <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/pages/checkout.css"
            type="text/css" />
      </head>

      <body>
        <%@ include file="/html/header.html" %>

          <main class="checkout-grid-container">
            <!-- LEFT SIDE: BOOK CARDS -->
            <div class="checkout-left">
              <h2 class="section-title">Your Cart</h2>
              <c:forEach var="book" items="${cart_books}">
                <div class="cart-book-cell">
                  <img src="${pageContext.request.contextPath}/static/img/book/${book.bookId}.jpg" class="book-image"
                    alt="Cover of ${book.title}"
                    onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/static/img/book/default.jpg';" />
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
                    <div class="book-price"><span data-format="price">${book.price}</span></div>
                  </div>
                </div>
              </c:forEach>
            </div>

            <!-- RIGHT SIDE: SHIPPING + PAYMENT -->
            <div class="checkout-right">
              <h2 class="section-title">Shipping Information</h2>
              <!-- Payment Method Selection -->
              <form action="${pageContext.request.contextPath}/checkout" method="post" id="checkout-form">
                <div class="form-group">
                  <label for="paymentMethod"><strong>Payment Method</strong></label>
                  <select name="paymentMethod" id="paymentMethod" required>
                    <option value="">Select Payment Method</option>
                    <option value="credit_card">Credit Card</option>
                    <option value="in_person">Pay on Delivery</option>
                  </select>
                  <c:if test="${not empty errors.paymentMethod}">
                    <div class="error-message">${errors.paymentMethod}</div>
                  </c:if>
                </div>

                <!-- Address Suggestion -->
                <c:if test="${not empty savedAddress}">
                  <div class="form-group">
                    <input type="checkbox" id="useSavedAddress" onclick="fillSavedAddress()" />
                    <label for="useSavedAddress">Use my saved address</label>
                    <input type="hidden" id="savedAddress" value="${savedAddress}" />
                  </div>
                </c:if>

                <!-- Credit Card Fields -->
                <div id="credit-card-fields" style="display: none">
                  <div class="row">
                    <div class="form-group" style="flex: 1">
                      <label for="cardNumber">Card Number</label>
                      <input class="w-100" type="text" name="cardNumber" id="cardNumber"
                        placeholder="1234 5678 9012 3456" />
                      <c:if test="${not empty errors.cardNumber}">
                        <div class="error-message">${errors.cardNumber}</div>
                      </c:if>
                    </div>
                  </div>
                  <div class="row my-2">
                    <div class="form-group d-flex justify-content-center align-items-center gap-2 my-2">
                      <label for="expiry">Expiry Date</label>
                      <input type="month" name="expiry" id="expiry" />
                      <c:if test="${not empty errors.expiry}">
                        <div class="error-message">${errors.expiry}</div>
                      </c:if>
                    </div>
                    <div class="form-group d-flex justify-content-center align-items-center gap-2 my-2">
                      <label for="cvv">CVV</label>
                      <input type="text" name="cvv" placeholder="3200" id="cvv" maxlength="4" />
                      <c:if test="${not empty errors.cvv}">
                        <div class="error-message">${errors.cvv}</div>
                      </c:if>
                    </div>
                  </div>
                </div>

                <div id="address-field">
                  <div class="form-group">
                    <label for="address">Delivery Address</label>
                    <textarea name="address" id="address" rows="3" placeholder="Your shipping address..."></textarea>
                    <c:if test="${not empty errors.address}">
                      <div class="error-message">${errors.address}</div>
                    </c:if>
                  </div>
                </div>

                <p><strong>Final Total:</strong> <span data-format='price'>${final_total}</span> </p>
                <button type="submit" class="order-button" disabled>Order now</button>
              </form>
            </div>
          </main>

          <%@ include file="/html/footer.html" %>
            <script src="${pageContext.request.contextPath}/static/js/creditCard.js"></script>
            <script src="${pageContext.request.contextPath}/static/js/main.js"></script>
            <script src="${pageContext.request.contextPath}/static/js/header.js"></script>
            <script src="${pageContext.request.contextPath}/static/js/format-number.js"></script>
            <script src="${pageContext.request.contextPath}/static/js/address-suggestion.js"></script>
      </body>

      </html>