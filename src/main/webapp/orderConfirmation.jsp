<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*, java.util.*, com.DAO.*, com.Entity.*" %>
<%@ page import="Dbconnection.DBConnectionManager" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.text.NumberFormat" %>

<%
    // Retrieve orderId from the session
    Integer orderId = (Integer) session.getAttribute("orderId");

    if (orderId == null) {
        out.println("<h2>Error: Missing or invalid order ID.</h2>");
        return;
    }

    Order order = null;
    List<OrderItem> orderItems = new ArrayList<>();

    try (Connection connection = DBConnectionManager.getConnection()) {
        OrderDAOImpl orderDAO = new OrderDAOImpl(connection);
        OrderItemDAOImpl orderItemDAO = new OrderItemDAOImpl(connection);
        
        // Fetch the order by orderId
        order = orderDAO.getOrderById(orderId);
        
        if (order == null) {
            out.println("<h2>Error: Order not found for ID: " + orderId + ".</h2>");
            return;
        }

        // Fetch order items associated with this order
        orderItems = orderItemDAO.getOrderItemsByOrderId(orderId);
    } catch (SQLException | ClassNotFoundException e) {
        e.printStackTrace(); // Consider logging this error
        out.println("<h2>An error occurred while fetching order details.</h2>");
        return;
    }

    // Number formatting for currency
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order Confirmation</title>
    <style>
        body {
            font-family: 'Arial', sans-serif;
            background-color: #e9ecef;
            margin: 0;
            padding: 0;
            color: #343a40;
        }

        .container {
            max-width: 800px;
            margin: 50px auto;
            padding: 30px;
            background-color: #ffffff;
            border-radius: 8px;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
            transition: transform 0.2s;
        }

        .container:hover {
            transform: translateY(-5px);
        }

        h1 {
            text-align: center;
            color: #007bff;
            margin-bottom: 20px;
        }

        h2 {
            color: #495057;
            margin-top: 20px;
            border-bottom: 2px solid #007bff;
            padding-bottom: 10px;
        }

        p {
            font-size: 1.1rem;
            margin: 5px 0;
        }

        /* Grid Layout for Order Items */
        .order-grid {
            display: grid;
            grid-template-columns: repeat(4, 1fr);
            gap: 20px;
            margin-top: 20px;
            padding: 0;
            list-style: none;
        }

        .order-grid div {
            background-color: #f8f9fa;
            padding: 15px;
            text-align: center;
            border: 1px solid #dee2e6;
            border-radius: 5px;
        }

        .order-grid div.header {
            background-color: #007bff;
            color: white;
            font-weight: bold;
        }

        a {
            display: inline-block;
            margin-top: 30px;
            padding: 10px 20px;
            text-decoration: none;
            color: #ffffff;
            background-color: #28a745;
            font-weight: bold;
            border-radius: 5px;
            transition: background-color 0.3s;
        }

        a:hover {
            background-color: #218838;
        }

        .footer {
            text-align: center;
            margin-top: 40px;
            font-size: 0.9rem;
            color: #6c757d;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Order Confirmation</h1>

        <h2>Order Details</h2>
        <p><strong>Order ID:</strong> <%= orderId %></p>
        <p><strong>Total Amount:</strong> <%= currencyFormat.format(order.getTotalAmount()) %></p>
        <p><strong>Shipping Address:</strong> <%= order.getShippingAddress() %></p>
        <p><strong>Order Status:</strong> <%= order.getOrderStatus() %></p>
        <p><strong>Payment Status:</strong> <%= order.getPaymentStatus() %></p>

        <h2>Order Items</h2>

        <!-- Grid Layout for Order Items -->
        <div class="order-grid">
            <div class="header">Product ID</div>
            <div class="header">Quantity</div>
            <div class="header">Price</div>
            <div class="header">Total</div>

            <%
                for (OrderItem item : orderItems) {
                    BigDecimal totalItemPrice = BigDecimal.valueOf(item.getPrice())
                                                         .multiply(BigDecimal.valueOf(item.getQuantity()));
            %>
            <div><%= item.getProductId() %></div>
            <div><%= item.getQuantity() %></div>
            <div><%= currencyFormat.format(item.getPrice()) %></div>
            <div><%= currencyFormat.format(totalItemPrice) %></div>
            <% } %>
        </div>

        <form action="paymentPage.jsp" method="post">
    <button type="submit">Place Order</button>
</form>

    </div>
    

    <div class="footer">
        &copy; <%= new java.util.Date().getYear() + 1900 %> Your Company Name. All rights reserved.
    </div>
</body>
</html>
