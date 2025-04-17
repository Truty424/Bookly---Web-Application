<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="it.unipd.bookly.Resource.Book" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.List" %>
<html>
<head>
    <title>Your Shopping Cart</title>
</head>
<body>
    <h1>Your Cart</h1>

    <ul>
        <%
            List<Book> books = (List<Book>) request.getAttribute("cart_books");
            if (books != null && !books.isEmpty()) {
                for (Book book : books) {
        %>
                    <li>
                        <strong><%= book.getTitle() %></strong><br/>
                        ISBN: <%= book.getIsbn() %><br/>
                        Price: €<%= book.getPrice() %><br/>
                        Language: <%= book.getLanguage() %><br/>
                        <form action="<%= request.getContextPath() %>/cart/remove/<%= book.getBookId() %>" method="post" style="display:inline;">
                            <button type="submit">Remove from cart</button>
                        </form>
                    </li>
                    <hr/>
        <%
                }
            } else {
        %>
            <li>Your cart is empty.</li>
        <%
            }
        %>
    </ul>

    <form method="post" action="<%= request.getContextPath() %>/cart/apply-discount">
        <input type="text" name="discount_code" placeholder="Discount code" required />
        <button type="submit">Apply Discount</button>
    </form>

    <c:if test="${not empty discount_error}">
        <p style="color:red;">${discount_error}</p>
    </c:if>
    
    <c:if test="${not empty applied_discount}">
        <p>Discount Applied: ${applied_discount.code} - ${applied_discount.discountRate * 100}% off</p>
    </c:if>

    <p><strong>Total:</strong> €${total_price}</p>
    <p><strong>Final Total:</strong> €${final_total}</p>

    <%-- Order Now Button --%>
    <form action="<%= request.getContextPath() %>/orders" method="post">
        <button type="submit">Order Now</button>
    </form>

    <%-- Clear Cart Button --%>
    <form action="<%= request.getContextPath() %>/checkout" method="get">
        <button type="submit">Order Now for me</button>
    </form>

    <a href="<%= request.getContextPath() %>/book">Continue Shopping</a>
</body>
</html>
