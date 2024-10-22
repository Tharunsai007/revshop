<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java.util.List, com.Entity.Order, com.DAO.OrderDAOImpl, Dbconnection.DBConnectionManager" %>

<%
List<Order> orders = null;//(List<Order>) session.getAttribute("orders");
OrderDAOImpl orderDAO = new OrderDAOImpl(DBConnectionManager.getConnection());
orders = orderDAO.getAllOrders();
    
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Orders</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f4f4f4;
            color: #333;
        }
        
        h1, h2 {
            text-align: center;
            color: #2c3e50;
        }

        .orders-table {
            width: 100%;
            border-collapse: collapse;
            margin: 20px 0;
        }

        .orders-table th, .orders-table td {
            padding: 10px;
            border: 1px solid #ddd;
            text-align: center;
        }

        .orders-table th {
            background-color: #2980b9;
            color: white;
        }

        .update-btn {
            padding: 5px 10px;
            background-color: #3498db;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        .update-btn:hover {
            background-color: #2980b9;
        }
    </style>
</head>
<body>
    <h1>All Orders</h1>
    <table class="orders-table">
        <thead>
            <tr>
                <th>Order ID</th>
                <th>User ID</th>
                <th>Order Status</th>
                <th>Update Status</th>
            </tr>
        </thead>
        <tbody>
            <%
                if (orders != null && !orders.isEmpty()) {
                    for (Order order : orders) {
            %>
            <tr>
                <td><%= order.getOrderId() %></td>
                <td><%= order.getUserId() %></td>
                <td><%= order.getOrderStatus() %></td>
                <td>
                    <form action="orders" method="post">
                        <input type="hidden" name="orderId" value="<%= order.getOrderId() %>">
                        <select name="status" required>
                            <option value="" disabled selected>Select Status</option>
                            <option value="Pending">Pending</option>
                            <option value="Shipped">Shipped</option>
                            <option value="Delivered">Delivered</option>
                            <option value="Cancelled">Cancelled</option>
                        </select>
                        <input type="submit" class="update-btn" value="Update">
                        <input type="hidden" name="action" value="update">
                    </form>
                </td>
            </tr>
            <%
                    }
                } else {
            %>
            <tr>
                <td colspan="4">No orders found.</td>
            </tr>
            <%
                }
            %>
        </tbody>
    </table>
</body>
</html>
