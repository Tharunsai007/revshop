<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.DAO.CartDAO" %>
<%@ page import="com.DAO.productDAOImp1" %>
<%@ page import="com.Entity.cartdetails" %>
<%@ page import="com.Entity.Product" %>
<%@ page import="Dbconnection.DBConnectionManager" %>

<% 
    CartDAO cartDAO = new CartDAO(DBConnectionManager.getConnection());
    productDAOImp1 productDAO = new productDAOImp1(); 
    Integer userId = (Integer) request.getSession().getAttribute("userId");
    List<cartdetails> cartItems = cartDAO.getCartItemsByBuyerId(userId);
    double totalAmount = 0;
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Your Cart</title>
    <style>
        /* General page styling */
        body {
            font-family: 'Helvetica Neue', Arial, sans-serif;
            background-color: #f9f9f9;
            margin: 0;
            padding: 0;
            color: #333;
        }

        h1 {
            text-align: center;
            color: #444;
            font-weight: 300;
            margin-top: 20px;
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

        .cart-container {
            max-width: 960px;
            margin: 40px auto;
            padding: 20px;
            background-color: #fff;
            border-radius: 10px;
            box-shadow: 0px 2px 10px rgba(0, 0, 0, 0.1);
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
        }

        table, th, td {
            border: 1px solid #eaeaea;
        }

        th, td {
            padding: 15px;
            text-align: center;
            font-size: 1rem;
        }

        th {
            background-color: #f5f5f5;
            color: #333;
            font-weight: 600;
            text-transform: uppercase;
        }

        td {
            background-color: #fff;
        }

        img {
            max-width: 100px;
            height: auto;
            border-radius: 8px;
        }

        .remove-btn {
            background-color: #ff6b6b;
            color: white;
            padding: 8px 14px;
            border: none;
            cursor: pointer;
            border-radius: 4px;
            transition: background-color 0.3s ease;
        }

        .remove-btn:hover {
            background-color: #e63946;
        }

        .checkout-btn {
            background-color: #28a745;
            color: white;
            padding: 14px 30px;
            font-size: 1rem;
            border: none;
            cursor: pointer;
            border-radius: 4px;
            transition: background-color 0.3s ease;
            float: right;
            text-transform: uppercase;
        }

        .checkout-btn:hover {
            background-color: #218838;
        }

        .total-section {
            font-size: 1.5rem;
            text-align: right;
            margin-top: 10px;
        }

        .cart-total {
            font-weight: bold;
        }

        /* Responsive styling */
        @media screen and (max-width: 768px) {
            th, td {
                padding: 10px;
                font-size: 0.9rem;
            }

            .checkout-btn {
                width: 100%;
                margin-top: 10px;
            }

            .total-section {
                text-align: center;
                margin-bottom: 20px;
            }
        }
    </style>
    
    <script>
        function initiatePayment() {
            var totalAmount = <%= totalAmount %> * 100; // Convert to paise for Razorpay

            var options = {
                "key": "YOUR_RAZORPAY_KEY", // Replace with your Razorpay Key
                "amount": totalAmount, 
                "currency": "INR",
                "name": "RevShop",
                "description": "Order Payment",
                "image": "your-logo-url",
                "handler": function (response) {
                    console.log("Payment successful, response:", response);

                    // Call the servlet to process the order
                    fetch('<%= request.getContextPath() %>/CheckoutServlet', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        },
                        body: new URLSearchParams({
                            userId: "<%= userId %>",
                            razorpayPaymentId: response.razorpay_payment_id,  // Pass Razorpay payment ID
                            totalAmount: <%= totalAmount %>, // Send total amount in rupees
                            shippingAddress: document.getElementById('shippingAddress').value
                        })
                    })
                    .then(res => res.json())
                    .then(data => {
                        if (data.success) {
                            alert("Order placed successfully!");
                            window.location.href = "<%= request.getContextPath() %>/orderconfirmation.jsp";  // Redirect to confirmation page
                        } else {
                            alert("Order confirmation failed.");
                        }
                    })
                    .catch(err => console.error('Error confirming order:', err));
                },
                "prefill": {
                    "name": "User's Name",
                    "email": "user@example.com",  // Pre-fill with user's email
                    "contact": "user-phone"  // Pre-fill with user's contact number
                },
                "theme": {
                    "color": "#F37254"
                }
            };

            var rzp1 = new Razorpay(options);
            rzp1.open();  // Open the Razorpay checkout
        }
    </script>
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

    <h1>Your Shopping Cart</h1>
    <div class="cart-container">
        <table>
            <thead>
                <tr>
                    <th>Product Image</th>
                    <th>Product Name</th>
                    <th>Quantity</th>
                    <th>Price</th>
                    <th>Total</th>
                    <th>Remove</th>
                </tr>
            </thead>
            <tbody>
                <% for (cartdetails cartItem : cartItems) {
                    Product product = productDAO.getProductById(cartItem.getProductId());
                    if (product != null) { 
                %>
                <% 
    byte[] imageBytes = product.getImageUrl();  // Assuming this returns byte[] (image data)
    String base64Image = java.util.Base64.getEncoder().encodeToString(imageBytes); 
%>
                <tr>
                    <td><img src="data:image/jpg;base64,<%= base64Image %>" alt="<%= product.getProductName() %>"></td>
                    <td><%= product.getProductName() %></td>
                    <td><%= cartItem.getQuantity() %></td>
                    <td>$<%= product.getPrice() %></td>
                    <td>$<%= cartItem.getQuantity() * product.getPrice() %></td>
                    <td>
                        <form action="RemoveFromCartServlet" method="POST">
                            <input type="hidden" name="cartId" value="<%= cartItem.getCartId() %>">
                            <%System.out.println(cartItem.getCartId()); %>
                            <button type="submit" class="remove-btn">Remove</button>
                        </form>
                    </td>
                </tr>
                <% totalAmount += cartItem.getQuantity() * product.getPrice(); } %>
                <% } %>
            </tbody>
        </table>

        <!-- Total Amount -->
        <div class="total-section">
            <span class="cart-total">Total Amount: $<%= totalAmount %></span>
        </div>
        
        <!-- Your checkout form -->
    <form action="CheckoutServlet" method="POST">
    <label for="shippingAddress">Shipping Address:</label>
    <input type="text" id="shippingAddress" name="shippingAddress" required>

    <!-- Place Order Button -->
    <button type="submit" class="checkout-btn">Proceed to Payment</button>
</form>

    </div>

</body>
</html>
