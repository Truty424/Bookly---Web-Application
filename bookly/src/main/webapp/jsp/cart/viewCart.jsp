<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="it.unipd.bookly.Resource.Book" %>
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

    <form method="get" action="/cart">
        <input type="text" name="discount" placeholder="Discount code" />
        <button type="submit">Apply</button>
    </form>
    
    <c:if test="${not empty applied_discount}">
        <p>Discount Applied: ${applied_discount.code} - ${applied_discount.discountRate * 100}% off</p>
    </c:if>

    <%-- Clear Cart Button --%>
    <form action="<%= request.getContextPath() %>/cart/clear" method="post">
        <button type="submit">Clear Cart</button>
    </form>

    <a href="<%= request.getContextPath() %>/book">Continue Shopping</a>
</body>
</html>
