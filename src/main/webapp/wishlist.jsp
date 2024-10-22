<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.Entity.Product" %>
<%@ page import="com.DAO.WishlistDAOImpl" %>
<%
    WishlistDAOImpl wishlistService = new WishlistDAOImpl();
    Integer userId = (Integer) request.getSession().getAttribute("userId");
    List<Product> wishlistProducts = wishlistService.getWishlistByUserId(userId);
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Your Wishlist</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            color: #333;
            margin: 0;
            padding: 0;
        }
        .navbar {
            background-color: #5a3f99;
            padding: 10px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .navbar a {
            color: white;
            text-decoration: none;
            padding: 10px 15px;
        }
        .navbar a:hover {
            background-color: rgba(255, 255, 255, 0.2);
            border-radius: 5px;
        }
        .container {
            width: 90%;
            max-width: 1200px;
            margin: 20px auto;
            background-color: white;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }
        h1 {
            text-align: center;
            color: #5a3f99;
        }
        .product-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px;
        }
        .product-item {
            background-color: white;
            border: 1px solid #ddd;
            padding: 15px;
            border-radius: 8px;
            text-align: center;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
            transition: transform 0.3s;
        }
        .product-item:hover {
            transform: translateY(-5px);
        }
        .product-item img {
            width: 100%;
            height: 200px;
            object-fit: cover;
            border-bottom: 3px solid #5a3f99;
        }
        .product-details {
            margin: 10px 0;
        }
        .product-details h2 {
            font-size: 1.4rem;
            color: #5a3f99;
        }
        .product-details p {
            color: #666;
        }
        .product-actions {
            display: flex;
            justify-content: space-between;
        }
        .btn {
            padding: 10px 15px;
            border: none;
            border-radius: 5px;
            color: white;
            cursor: pointer;
        }
        .btn-cart {
            background-color: #5a3f99;
        }
        .btn-remove {
            background-color: #e74c3c;
        }
        .btn:hover {
            opacity: 0.8;
        }
    </style>
</head>
<body>

<div class="navbar">
    <div class="navbar-links">
        <a href="buyerDashboard.jsp">Home</a>
        <a href="wishlist.jsp">Wishlist</a>
        <a href="cart.jsp">Cart</a>
        <a href="orders.jsp">Orders</a>
    </div>
    <div class="navbar-user">
        <a href="profile.jsp">Profile</a>
        <a href="login.jsp">Logout</a>
    </div>
</div>

<div class="container">
    <h1>Your Wishlist</h1>

    <div class="product-grid">
        <% if (wishlistProducts != null && !wishlistProducts.isEmpty()) { 
            for (Product product : wishlistProducts) { %>
            <div class="product-item">
                <% 
                    byte[] imageBytes = product.getImageUrl();
                    String base64Image = java.util.Base64.getEncoder().encodeToString(imageBytes);
                %>
                <img src="data:image/jpeg;base64,<%= base64Image %>" alt="<%= product.getProductName() %>">
                <div class="product-details">
                    <h2><%= product.getProductName() %></h2>
                    <p><%= product.getProductDescription() %></p>
                    <p>Price: $<%= product.getPrice() %></p>
                </div>
                <div class="product-actions">
                    <form action="AddToCartServlet" method="POST">
                        <input type="hidden" name="productId" value="<%= product.getProductId() %>">
                        <input type="number" name="quantity" value="1" min="1" required>
                        <button class="btn btn-cart" type="submit">Add to Cart</button>
                    </form>

                    <form action="RemoveFromWishlistServlet" method="POST">
                        <input type="hidden" name="productId" value="<%= product.getProductId() %>">
                        <button class="btn btn-remove" type="submit">Remove</button>
                    </form>
                </div>
            </div>
        <% } } else { %>
            <p>No items in your wishlist. Browse products to add some!</p>
        <% } %>
    </div>
</div>

</body>
</html>
