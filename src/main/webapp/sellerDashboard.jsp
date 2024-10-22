<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.List,com.Entity.Product,com.Entity.Category,com.DAO.productDAOImp1,com.DAO.CategoryDAOImpl,Dbconnection.DBConnectionManager" %>

<%
DBConnectionManager connection = new DBConnectionManager();
productDAOImp1 productDAO = new productDAOImp1();
CategoryDAOImpl categoryDAO = new CategoryDAOImpl(connection.getConnection());

// Get sellerId from session
Integer sellerId = (Integer) request.getSession().getAttribute("sellerId");

if (sellerId == null) {
    response.sendRedirect("sellerlogin.jsp"); // Redirect to login if no sellerId is found
    return;
}

// Get selectedCategoryId from request parameters
Integer selectedCategoryId = null;
String categoryParam = request.getParameter("category");
if (categoryParam != null) {
    try {
        selectedCategoryId = Integer.parseInt(categoryParam);
    } catch (NumberFormatException e) {
        selectedCategoryId = 0; // Default to 'All' if parsing fails
    }
}

List<Product> products = null;
if (selectedCategoryId == null || selectedCategoryId == 0) {
    products = productDAO.getProductsBySellerId(sellerId);
} else {
    products = productDAO.getProductsBySellerIdAndCategory(sellerId, selectedCategoryId);
}

// Retrieve all categories for the dropdown
List<Category> categories = categoryDAO.getAllCategories();
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Products</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f5f5f5;
            margin: 0;
            padding: 0;
        }

        .navbar {
            background-color: #333;
            overflow: hidden;
        }

        .navbar a {
            float: left;
            display: block;
            color: white;
            text-align: center;
            padding: 14px 20px;
            text-decoration: none;
        }

        .navbar a:hover {
            background-color: #575757;
        }

        .container {
            width: 80%;
            margin: auto;
            padding-top: 20px;
        }

        .dashboard-header h1 {
            color: #333;
            text-align: center;
            margin-bottom: 20px;
        }

        .category-filter {
            margin-bottom: 20px;
        }

        .category-filter select {
            padding: 5px;
            font-size: 14px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            background-color: #fff;
        }

        table, th, td {
            border: 1px solid #ddd;
        }

        th, td {
            padding: 12px;
            text-align: center;
        }

        th {
            background-color: #333;
            color: white;
        }

        td {
            background-color: #f9f9f9;
        }

        .product-image img {
            width: 50px;
            height: 50px;
            object-fit: cover;
        }

        .action-btn {
            background-color: #4CAF50;
            color: white;
            padding: 8px 16px;
            margin: 5px;
            border: none;
            cursor: pointer;
            border-radius: 4px;
        }

        .action-btn:hover {
            background-color: #45a049;
        }

        .update-form {
            background-color: #e9e9e9;
            padding: 10px;
            display: none; /* Initially hidden */
        }

        .update-form input, .update-form select {
            width: 100%;
            padding: 8px;
            margin: 5px 0;
            box-sizing: border-box;
        }

        .update-form label {
            margin-top: 10px;
            display: block;
            color: #333;
        }

        .update-form button {
            background-color: #f44336;
        }

        .update-form button:hover {
            background-color: #d73833;
        }

    </style>
    
</head>
<body>

<!-- Navigation Bar -->
<nav class="navbar">
    <a href="sellerDashboard.jsp">My Products</a>
    <a href="addProductForm.jsp">Add Product</a>
    <a href="sellerOrders.jsp">Orders</a>
    <a href="sellerlogin.jsp">Logout</a>
</nav>

<div class="container">
    <div class="dashboard-header">
        <h1>My Products</h1>
    </div>

    <!-- Category Filter -->
    <div class="category-filter">
        <form method="get" action="sellerDashboard.jsp">
            <label for="category">Filter by Category:</label>
            <select name="category" id="category" onchange="this.form.submit()">
                <option value="0" <%= (selectedCategoryId == null || selectedCategoryId == 0) ? "selected" : "" %>>All</option>
                <% for (Category category : categories) { %>
                    <option value="<%= category.getCategoryId() %>" <%= (selectedCategoryId != null && selectedCategoryId.equals(category.getCategoryId())) ? "selected" : "" %>>
                        <%= category.getCategoryName() %>
                    </option>
                <% } %>
            </select>
        </form>
    </div>

    <!-- Product Table -->
    <table>
        <thead>
            <tr>
                <th>Image</th>
                <th>Product Name</th>
                <th>Price</th>
                <th>Quantity</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <% for (Product product : products) { %>
    <tr>
        <td class="product-image">
            <img src="data:image/jpeg;base64,<%= java.util.Base64.getEncoder().encodeToString(product.getImageUrl()) %>" alt="<%= product.getProductName() %>">
        </td>
        <td><%= product.getProductName() %></td>
        <td>$<%= product.getPrice() %></td>
        <td><%= product.getQuantityInStock() %></td>
        <td>
    <button class="action-btn" onclick="toggleUpdateForm(this)" data-product-id="<%= product.getProductId() %>">Update</button>
    <%= product.getProductId() %>
    <form action="sellerDashboard" method="post" style="display:inline;">
        <input type="hidden" name="action" value="delete">
        <input type="hidden" name="productId" value="<%= product.getProductId() %>">
        <button type="submit" class="action-btn">Delete</button>
    </form>
</td>
</tr>
<tr class="update-form" style="display:none;" data-product-id="<%= product.getProductId() %>">
    <td colspan="5">
        <form action="sellerDashboard" method="post">
            <input type="hidden" name="action" value="update">
            <input type="hidden" name="productId" value="<%= product.getProductId() %>">
            <label for="productName">Product Name:</label>
            <input type="text" name="productName" value="<%= product.getProductName() %>" required>
            <label for="productDescription">Description:</label>
            <input type="text" name="productDescription" value="<%= product.getProductDescription() %>" required>
            <label for="price">Price:</label>
            <input type="number" name="price" value="<%= product.getPrice() %>" required>
            <label for="quantity">Quantity:</label>
            <input type="number" name="quantityInStock" value="<%= product.getQuantityInStock() %>" required>
            <label for="categoryId">Category:</label>
            <select name="categoryId">
                <% for (Category category : categories) { %>
                    <option value="<%= category.getCategoryId() %>" <%= (product.getCategoryId() == category.getCategoryId()) ? "selected" : "" %>>
                        <%= category.getCategoryName() %>
                    </option>
                <% } %>
            </select>
            <button type="submit" class="action-btn">Save Changes</button>
        </form>
    </td>
</tr>


<% } %>
        </tbody>
    </table>
</div>
<script>
    function toggleUpdateForm(button) {
        const productId = button.dataset.productId; // Get the productId from data attribute
        const form = document.querySelector(`.update-form[data-product-id="${productId}"]`); // Select form by data attribute

        if (form) {
            form.style.display = (form.style.display === "none" || form.style.display === "") ? "block" : "none";
        } else {
            console.error(`Form with data-product-id="${productId}" not found`);
        }
    }



    </script>

</body>
</html>
