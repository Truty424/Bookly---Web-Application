<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="it.unipd.bookly.Resource.Order" %>
<%@ page import="java.util.List" %>

<html>
<head>
    <title>My Orders</title>
</head>
<body>

<h2>My Orders</h2>

<%
    List<Order> orders = (List<Order>) request.getAttribute("orders");
%>

<% if (orders != null && !orders.isEmpty()) { %>
    <table>
        <tr>
            <th>Order ID</th>
            <th>Total Price (€)</th>
            <th>Payment Method</th>
            <th>Status</th>
            <th>Order Date</th>
            <th>Shipping Address</th>
            <th>Shipment Code</th>
            <th>Action</th>
        </tr>
        <% for (Order order : orders) {
            String status = order.getStatus();
            boolean cancellable = "pending".equalsIgnoreCase(status) || "placed".equalsIgnoreCase(status);
        %>
            <tr>
                <td><%= order.getOrderId() %></td>
                <td><%= order.getTotalPrice() %></td>
                <td><%= order.getPaymentMethod() %></td>
                <td><%= status %></td>
                <td><%= order.getOrderDate() %></td>
                <td><%= order.getAddress() %></td>
                <td><%= order.getShipmentCode() != null ? order.getShipmentCode() : "N/A" %></td>
                <td>
                    <% if (cancellable) { %>
                        <form action="<%= request.getContextPath() %>/user/orders/cancel" method="post" onsubmit="return confirm('Are you sure you want to cancel this order?');">
                            <input type="hidden" name="orderId" value="<%= order.getOrderId() %>" />
                            <button type="submit" class="cancel-btn">Cancel</button>
                        </form>
                    <% } else { %>
                        <em>Not Cancellable</em>
                    <% } %>
                </td>
            </tr>
        <% } %>
    </table>
<% } else { %>
    <p class="no-orders">You haven’t placed any orders yet.</p>
<% } %>

<a class="back-link" href="<%= request.getContextPath() %>/user/profile">← Back to Profile</a>

</body>
</html>
