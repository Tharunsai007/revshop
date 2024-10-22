<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.Entity.Product" %>
<%@ page import="com.DAO.productDAOImp1" %>
<%
    productDAOImp1 productDAO = new productDAOImp1();

    List<Product> products = null;
    Integer selectedCategoryId = null;

    // Retrieve 'selectedCategoryId' from request parameters instead of attributes
    String categoryParam = request.getParameter("category");
    if (categoryParam != null) {
        try {
            selectedCategoryId = Integer.parseInt(categoryParam);
        } catch (NumberFormatException e) {
            selectedCategoryId = 0; // Default to 'All' if parsing fails
        }
    }

    if (selectedCategoryId == null || selectedCategoryId == 0) {
        products = productDAO.getAllProducts();
    } else {
        products = productDAO.getProductsByCategory(selectedCategoryId);
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <!-- [Your existing head content] -->
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Buyer Dashboard</title>
    <style>
        /* Basic CSS styling */
body {
    font-family: 'Arial', sans-serif;
    margin: 0;
    padding: 0;
    color: #333;
    line-height: 1.6;
   /* background-image: url('https://as1.ftcdn.net/v2/jpg/02/91/63/00/1000_F_291630046_OCk4onorVs3CBgVFwpe3dSagXGweSbXg.jpg'); /* Vibrant background */
    background-size: cover;
    background-attachment: fixed;
    min-height: 100vh;
}

/* Navbar Styling */
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

/* Container Styling */
.container {
/*background-image: url('https://as2.ftcdn.net/v2/jpg/03/75/90/33/1000_F_375903328_klyRiXanbUvaJJ7gnKfBd590BqnEXP78.jpg');*/
    width: 90%;
    max-width: 1200px;
    margin: 30px auto;
    padding: 20px;
    background-color: #fff;
    background-color: rgba(255,255,255,0.5);
    border-radius: 15px;
    box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
    animation: fadeIn 0.5s ease;
}

/* Animation for container */
@keyframes fadeIn {
    from { opacity: 0; transform: translateY(20px); }
    to { opacity: 1; transform: translateY(0); }
}

/* Header Styling */
h1 {
    text-align: center;
    margin: 20px 0;
    color: #5a3f99;
    text-transform: uppercase;
    letter-spacing: 2px;
}

/* Product Grid Styling */
.product-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
    gap: 30px;
}

/* Product Item Styling */
.product-item {
    background-color: white;
    border-radius: 10px;
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
    overflow: hidden;
    transition: transform 0.3s, box-shadow 0.3s ease;
    animation: scaleIn 0.4s ease; /* Add scale animation */
}

@keyframes scaleIn {
    from { transform: scale(0.9); opacity: 0; }
    to { transform: scale(1); opacity: 1; }
}

.product-item:hover {
    transform: translateY(-10px) scale(1.05); /* Slightly grow on hover */
    box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
}

/* Product Image Styling */
.product-item img {
    width: 100%;
    height: 200px;
    object-fit: cover;
    border-bottom: 3px solid #5a3f99;
}

/* Product Details Styling */
.product-details {
    padding: 20px;
}

.product-details h2 {
    font-size: 1.4rem;
    color: #5a3f99;
    margin-bottom: 10px;
}

.product-details p {
    margin: 10px 0;
    color: #666;
}

/* Product Actions Styling */
.product-actions {
    display: flex;
    justify-content: space-between;
    margin-top: 15px;
}

.add-to-cart-btn, .add-to-wishlist-btn {
    background-color: #5a3f99;
    color: white;
    border: none;
    padding: 10px 15px;
    border-radius: 5px;
    cursor: pointer;
    font-weight: bold;
    transition: all 0.3s ease;
    animation: buttonHover 0.4s ease; /* Add button hover animation */
}

@keyframes buttonHover {
    from { transform: scale(1); }
    to { transform: scale(1.05); }
}

.add-to-cart-btn:hover, .add-to-wishlist-btn:hover {
    background-color: #7b5cc7;
    transform: translateY(-2px);
}

/* Input Fields Styling */
input[type="number"] {
    width: 60px;
    padding: 8px;
    border: 2px solid #ccc;
    border-radius: 5px;
    font-size: 1rem;
}

/* Select Dropdown Styling */
select {
    margin: 20px 0;
    padding: 10px;
    border: 2px solid #5a3f99;
    border-radius: 5px;
    font-size: 16px;
    background-color: white;
    color: #5a3f99;
}

/* Mobile Responsiveness */
@media (max-width: 768px) {
    .navbar ul {
        flex-direction: column;
        align-items: center;
    }
    .navbar li {
        margin: 10px 0;
    }
    .product-grid {
        grid-template-columns: 1fr;
    }
}
    </style>
</head>
<body>

<!-- Navigation Bar -->
<nav class="navbar">
    <ul>
        <li><a href="buyerDashboard.jsp">Home</a></li>
        <li><a href="cart.jsp">Cart</a></li>
        <li><a href="wishlist.jsp">WishList</a></li>
        <li><a href="orderHistory.jsp">Order History</a></li>
        <li><a href="login.jsp">Logout</a></li>
    </ul>
</nav>

<div class="container">
    <h1>Buyer Dashboard</h1>

    <!-- Category Selection Form -->
    <form method="get" action="buyerDashboard.jsp" class="form-inline">
        <label for="category">Browse by Category:</label>
        <select name="category" id="category" onchange="this.form.submit()">
            <option value="0" <%= (selectedCategoryId == null || selectedCategoryId == 0) ? "selected" : "" %>>All</option>
            <option value="1" <%= (selectedCategoryId != null && selectedCategoryId == 1) ? "selected" : "" %>>Mobiles</option>
            <option value="2" <%= (selectedCategoryId != null && selectedCategoryId == 2) ? "selected" : "" %>>Laptops</option>
            <option value="3" <%= (selectedCategoryId != null && selectedCategoryId == 3) ? "selected" : "" %>>Electronics</option>
            <option value="4" <%= (selectedCategoryId != null && selectedCategoryId == 4) ? "selected" : "" %>>Fashion</option>
            <option value="5" <%= (selectedCategoryId != null && selectedCategoryId == 5) ? "selected" : "" %>>Kitchen Appliances</option>
            <option value="6" <%= (selectedCategoryId != null && selectedCategoryId == 6) ? "selected" : "" %>>Gaming Accessories</option>
            <option value="7" <%= (selectedCategoryId != null && selectedCategoryId == 7) ? "selected" : "" %>>Video Games</option>
        </select>
    </form>

    <!-- Product Grid -->
    <div class="product-grid">
        <% for (Product product : products) { %>
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
                <p>Discounted Price: $<%= product.getDiscountedPrice() %></p>
                
                 <div class="product-actions">
        <!-- Add to Cart Form -->
        <form action="AddToCartServlet" method="POST" style="display: inline;">
    <input type="hidden" name="productId" value="<%= product.getProductId() %>">
    <input type="number" name="quantity" min="1" max="<%= product.getQuantityInStock() %>" value="1">
    <button class="add-to-cart-btn" type="submit">Add to Cart</button>
</form>

        <!-- Add to Wishlist Form -->
        <form action="AddToWishlistServlet" method="POST" style="display: inline;">
            <input type="hidden" name="productId" value="<%= product.getProductId() %>">
            <button class="add-to-wishlist-btn" type="submit">Add to Wishlist</button>
        </form>
    </div>
            </div>
        </div>
        <% } %>
    </div>
</div>

</body>
</html>
