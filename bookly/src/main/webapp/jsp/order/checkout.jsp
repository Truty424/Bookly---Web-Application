<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="it.unipd.bookly.Resource.Book" %>
<%@ page import="javax.servlet.http.HttpSession" %>

<%
    HttpSession session = request.getSession(false);
    Book book = (Book) session.getAttribute("checkout_book");
    if (book == null) {
%>
    <p>No book selected for checkout.</p>
    <a href="<%= request.getContextPath() %>/book">Go back to books</a>
<%
    return;
    }
%>

<html>
<head>
    <title>Checkout</title>

</head>
<body>
    <div class="checkout-box">
        <h2>Confirm Your Order</h2>
        <div class="book-info">
            <h3><%= book.getTitle() %></h3>
            <p>Author: <%= book.getLanguage() %></p>
            <p>Price: <span class="price">€<%= book.getPrice() %></span></p>
        </div>

        <form action="<%= request.getContextPath() %>/order/confirm" method="post">
            <input type="hidden" name="bookId" value="<%= book.getBookId() %>">
            <label>Shipping Address:</label><br/>
            <textarea name="address" required rows="3" cols="50"></textarea><br/><br/>

            <label>Payment Method:</label><br/>
            <select name="paymentMethod" required>
                <option value="credit_card">Credit Card</option>
                <option value="in_person">Pay on Delivery</option>
            </select><br/><br/>

            <button type="submit">Place Order</button>
        </form>
    </div>
</body>
</html>
