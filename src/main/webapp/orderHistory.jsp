<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.Entity.Order" %>
<%@ page import="com.Entity.Product" %>
<%@ page import="com.Entity.OrderItem" %>
<%@ page import="com.DAO.OrderDAOImpl" %>
<%@ page import="Dbconnection.DBConnectionManager" %>

<%
    Integer userId = (Integer) session.getAttribute("userId"); // Assuming user ID is stored in session
    DBConnectionManager connection = new DBConnectionManager();
    OrderDAOImpl orderDAO = new OrderDAOImpl(connection.getConnection());
    OrderItem orderItem = new OrderItem();
    Product product = new Product();
    List<Order> orderHistory = null;

    if (userId != null) {
        orderHistory = orderDAO.getOrdersByUserId(userId);
    } else {
        response.sendRedirect("login.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order History</title>
    <style>
        body {
            font-family: 'Arial', sans-serif;
            margin: 0;
            padding: 0;
            color: #333;
            background-color: #f4f4f4;
            min-height: 100vh;
        }

        .navbar {
            background-color: rgba(90, 63, 153, 0.9);
            padding: 15px 0;
            position: sticky;
            top: 0;
            z-index: 1000;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }

        .navbar ul {
            list-style-type: none;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
        }

        .navbar li {
            margin: 0 15px;
        }

        .navbar a {
            color: white;
            text-decoration: none;
            font-weight: bold;
            font-size: 1.1em;
            transition: all 0.3s ease;
            padding: 10px 15px;
            border-radius: 5px;
        }

        .navbar a:hover {
            background-color: rgba(255,255,255,0.2);
            transform: translateY(-3px);
        }

        .container {
            width: 90%;
            max-width: 1200px;
            margin: 30px auto;
            padding: 20px;
            background-color: #fff;
            border-radius: 15px;
            box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
        }

        h1 {
            text-align: center;
            margin: 20px 0;
            color: #5a3f99;
            text-transform: uppercase;
            letter-spacing: 2px;
        }

        /* Grid Styling for Order History */
        .order-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 20px;
            margin-top: 20px;
        }

        .order-item {
            background-color: white;
            border-radius: 10px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
            padding: 20px;
            transition: transform 0.3s, box-shadow 0.3s ease;
        }

        .order-item:hover {
            transform: translateY(-10px) scale(1.05);
            box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
        }

        .order-details h2 {
            font-size: 1.2rem;
            color: #5a3f99;
            margin-bottom: 10px;
        }

        .order-details p {
            margin: 5px 0;
            color: #666;
        }

        .price {
            font-weight: bold;
            color: #5a3f99;
        }

        .status {
            font-style: italic;
            color: #28a745;
        }
    </style>
</head>
<body>
    <nav class="navbar">
        <ul>
            <li><a href="buyerDashboard.jsp">Home</a></li>
            <li><a href="cart.jsp">Cart</a></li>
            <li><a href="wishlist.jsp">WishList</a></li>
            <li><a href="orderHistory.jsp">Order History</a></li>
            <li><a href="logout.jsp">Logout</a></li>
        </ul>
    </nav>

    <div class="container">
        <h1>Your Order History</h1>

        <div class="order-grid">
            <% if (orderHistory != null && !orderHistory.isEmpty()) {
                for (Order order : orderHistory) {
                    %>
                    <div class="order-item">
                        <div class="order-details">
                            <h2>Order ID: <%= order.getOrderId() %></h2>
                            <p>Product Name: <%= product.getProductName() %></p>
                            <p>Quantity: <%= orderItem.getQuantity() %></p>
                            <p>Price: <span class="price">$<%= product.getPrice() %></span></p>
                            <p>Order Date: <%= order.getCreatedAt() %></p>
                            <p>Status: <span class="status"><%= order.getOrderStatus() %></span></p>
                        </div>
                    </div>
            <% } } else { %>
                <p>No order history available.</p>
            <% } %>
        </div>
    </div>
</body>
</html>
