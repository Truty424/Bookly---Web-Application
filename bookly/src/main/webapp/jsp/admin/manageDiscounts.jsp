<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="it.unipd.bookly.Resource.Discount" %>

<html>
<head>
    <title>Manage Discounts</title>
</head>
<body>

<h2>Manage Discounts</h2>

<a class="add-btn" href="<%= request.getContextPath() %>/jsp/admin/addDiscount.jsp">+ Create Discount</a>

<%
    List<Discount> discounts = (List<Discount>) request.getAttribute("discounts");
    if (discounts != null && !discounts.isEmpty()) {
%>
    <table>
        <tr>
            <th>ID</th>
            <th>Code</th>
            <th>Percentage</th>
            <th>Expires</th>
            <th>Actions</th>
        </tr>
        <% for (Discount discount : discounts) { %>
        <tr>
            <td><%= discount.getDiscountId() %></td>
            <td><%= discount.getCode() %></td>
            <td><%= discount.getDiscountPercentage() %>%</td>
            <td><%= discount.getExpiredDate() %></td>
            <td class="actions">
                <form action="<%= request.getContextPath() %>/admin/deleteDiscount" method="post" onsubmit="return confirm('Are you sure?');">
                    <input type="hidden" name="discount_id" value="<%= discount.getDiscountId() %>" />
                    <button class="delete-btn" type="submit">Delete</button>
                </form>
            </td>
        </tr>
        <% } %>
    </table>
<% } else { %>
    <p>No discounts available.</p>
<% } %>

<p><a href="<%= request.getContextPath() %>/admin/dashboard">Back to Dashboard</a></p>

</body>
</html>
