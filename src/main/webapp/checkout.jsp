<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*, java.util.*, com.DAO.*, com.Entity.*" %>
<%@ page import="Dbconnection.DBConnectionManager" %>
<%@ page import="java.math.BigDecimal" %>

<%
    // Assume userId is already available in the session
    Integer userId = (Integer) session.getAttribute("userId");
    List<cartdetails> cartItems = null; // You might want to get cart items here for review

    if (userId == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    try (Connection connection = DBConnectionManager.getConnection()) {
        CartDAO cartDAO = new CartDAO(connection);
        cartItems = cartDAO.getCartItemsByBuyerId(userId); // Fetch user's cart items
    } catch (SQLException | ClassNotFoundException e) {
        e.printStackTrace();
    }

    if (cartItems == null || cartItems.isEmpty()) {
        out.println("<h2>Your cart is empty. Please add items to your cart first.</h2>");
        return;
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Checkout</title>
    <link rel="stylesheet" href="css/style.css"> <!-- Link to your CSS -->
</head>
<body>
    <div class="container">
        <h1>Checkout</h1>
        <h2>Your Cart Items</h2>
        <table border="1">
            <thead>
                <tr>
                    <th>Product ID</th>
                    <th>Quantity</th>
                    <th>Price</th>
                </tr>
            </thead>
            <tbody>
                <%
                    for (cartdetails item : cartItems) {
                %>
                <tr>
                    <td><%= item.getProductId() %></td>
                    <td><%= item.getQuantity() %></td>
                </tr>
                <% } %>
            </tbody>
        </table>

        <form action="CheckoutServlet" method="post">
    <label for="shippingAddress">Shipping Address:</label>
    <input type="text" id="shippingAddress" name="shippingAddress" required>
    <button type="submit">Place Order</button>
</form>
        
        <a href="buyerDashboard.jsp">Back to Shopping</a>
    </div>
</body>
</html>
