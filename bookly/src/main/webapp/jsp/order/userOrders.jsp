<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="it.unipd.bookly.Resource.Order" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Objects" %>

<%
    List<Order> orders = (List<Order>) request.getAttribute("orders");
    if (orders == null) {
        orders = java.util.Collections.emptyList(); // Fallback safety
    }
%>

<% if (!orders.isEmpty()) { %>
    <table>
        <tr>
            <th>Order ID</th>
            <th>Status</th>
        </tr>
        <% for (Order order : orders) {
            if (order == null) continue;
            String status = Objects.toString(order.getStatus(), "unknown");
        %>
            <tr>
                <td><%= order.getOrderId() %></td>
                <td><%= status %></td>
            </tr>
        <% } %>
    </table>
<% } else { %>
    <p>No orders found.</p>
<% } %>
