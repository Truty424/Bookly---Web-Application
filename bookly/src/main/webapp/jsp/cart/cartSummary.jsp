<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ page
import="java.util.List" %> <%@ page import="it.unipd.bookly.resource.CartItem"
%> <% List<CartItem>
  cartItems = (List<CartItem
    >) request.getAttribute("cart"); Double total = (Double)
    request.getAttribute("total"); %>
    <html>
      <head>
        <title>Your Cart</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/pages/cart-summary.css" />
        <%@ include file="/html/cdn.html" %> 
      </head>
      <body>

        <%@ include file="/html/header.html" %>
        <div class="cart-container">
        <h2>Shopping Cart</h2>
        <table border="cart-table">
          <tr>
            <th>Book</th>
            <th>Quantity</th>
            <th>Price</th>
          </tr>
          <% if (cartItems != null) { for (CartItem item : cartItems) { %>
          <tr>
            <td><%= item.getBook().getTitle() %></td>
            <td><%= item.getQuantity() %></td>
            <td><%= item.getBook().getPrice() %></td>
          </tr>
          <% } } else { %>
          <tr>
            <td colspan="3">Your cart is empty.</td>
          </tr>
          <% } %>
        </table>
        <div class="total-bar">
        Total: €<%= total != null ? total : "0.00" %>
        </div>
        </div>
      </body>
      <script src="${pageContext.request.contextPath}/static/js/format-number.js"></script>
    </html>
