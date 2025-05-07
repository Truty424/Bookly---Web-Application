<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="it.unipd.bookly.Resource.Order" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Objects" %>

<html>
<head>
    <title>Your Orders</title>
    <%@ include file="/html/cdn.html" %> 
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/base/root.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/base/globals.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/pages/userOrders.css" />
</head>
<body>
<%@ include file="/html/header.html" %>

<div class="orders-container">
    <h1>Your Orders</h1>



    <%
        List<Order> orders = (List<Order>) request.getAttribute("orders");
        if (orders == null) {
            orders = java.util.Collections.emptyList(); // Fallback
        }
    %>

    <% if (!orders.isEmpty()) { %>
        <div class="orders-grid">
            <% for (Order order : orders) {
                if (order == null) continue;
            %>
            <div class="order-card">
                <div class="order-header">
                    <span class="order-id">Order #<%= order.getOrderId() %></span>
                    <span class="order-status"><%= Objects.toString(order.getStatus(), "Unknown") %></span>
                </div>
                <div class="order-body">
                    <p><strong>Total:</strong> €<%= order.getTotalPrice() %></p>
                    <p><strong>Date:</strong> <%= order.getOrderDate() %></p>
                    <a href="${pageContext.request.contextPath}/orders/<%= order.getOrderId() %>" class="btn btn-primary">View Details</a>
                </div>
            </div>
            <% } %>
        </div>
    <% } else { %>
        <p>No orders found.</p>
    <% } %>
</div>

<%@ include file="/html/footer.html" %>
</body>
</html>
