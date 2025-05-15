<%@ page contentType="text/html;charset=UTF-8" %> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib prefix="fmt"
uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
  <head>
    <title>Your Orders</title>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <%@ include file="/html/cdn.html" %>
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/static/css/base/root.css"
    />
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/static/css/base/globals.css"
    />
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/static/css/pages/userOrders.css"
    />
  </head>
  <body>
    <%@ include file="/html/header.html" %>

    <main class="orders-container">
      <h2>Your Orders</h2>

      <c:choose>
        <c:when test="${not empty orders}">
          <div class="orders-grid">
            <c:forEach var="order" items="${orders}">
              <c:if test="${order != null}">
                <div class="order-card">
                  <div class="order-header">
                    <span class="order-id">Order #${order.orderId}</span>
                    <span class="order-status">
                      <c:out value="${order.status}" default="Unknown" />
                    </span>
                  </div>
                  <div class="order-body">
                    <p>
                      <strong>Total:</strong> €<fmt:formatNumber
                        value="${order.totalPrice}"
                        type="number"
                        minFractionDigits="2"
                      />
                    </p>
                    <p>
                      <strong>Date:</strong>
                      <fmt:formatDate
                        value="${order.orderDate}"
                        pattern="yyyy-MM-dd HH:mm"
                      />
                    </p>
                    <a
                      href="${pageContext.request.contextPath}/orders/${order.orderId}"
                      class="btn btn-primary"
                      >View Details</a
                    >
                  </div>
                </div>
              </c:if>
            </c:forEach>
          </div>
        </c:when>
        <c:otherwise>
          <p>No orders found.</p>
        </c:otherwise>
      </c:choose>
    </main>

    <%@ include file="/html/footer.html" %>
    <script src="${pageContext.request.contextPath}/static/js/main.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/header.js"></script>
  </body>
</html>
