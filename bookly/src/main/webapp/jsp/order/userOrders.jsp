<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="it.unipd.bookly.Resource.Order" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Objects" %>

<html>
<head>
    <title>Your Orders</title>
</head>
<body>

<h1>Your Orders</h1>

<%-- Display error message if present --%>
<%
    String errorMessage = (String) request.getAttribute("error_message");
    if (errorMessage != null) {
%>
    <p style="color: red;"><strong>Error:</strong> <%= errorMessage %></p>
<%
    }
%>

<%
    List<Order> orders = (List<Order>) request.getAttribute("orders");
    if (orders == null) {
        orders = java.util.Collections.emptyList(); // Fallback safety
    }
%>

<% if (!orders.isEmpty()) { %>
    <table border="1" cellpadding="5" cellspacing="0">
        <tr>
            <th>Order ID</th>
            <th>Status</th>
            <th>Total Price</th>
            <th>Order Date</th>
        </tr>
        <% for (Order order : orders) {
            if (order == null) continue;
        %>
            <tr>
                <td><%= order.getOrderId() %></td>
                <td><%= Objects.toString(order.getStatus(), "unknown") %></td>
                <td>€<%= order.getTotalPrice() %></td>
                <td><%= order.getOrderDate() %></td>
            </tr>
        <% } %>
    </table>
<% } else { %>
    <p>No orders found.</p>
<% } %>

<br>
<form action="${pageContext.request.contextPath}/" method="get">
                <button type="submit">Home Page</button>
            </form>
</body>
</html>
