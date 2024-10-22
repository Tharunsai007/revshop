<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.Entity.Product" %>
<%@ page import="com.DAO.productDAOImp1" %>
<%
productDAOImp1 productDAO = new productDAOImp1();
List<Product> products = productDAO.getAllProducts();
%> 
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Product Display</title>
    <style>
        /* Basic CSS styling */
        table {
            width: 100%;
            border-collapse: collapse;
        }
        table, th, td {
            border: 1px solid black;
        }
        th, td {
            padding: 10px;
            text-align: left;
        }
        th {
            background-color: #f2f2f2;
        }
        img {
            max-width: 100px; /* Limit image size */
            height: auto;
        }
        /* Styling for Add to Cart button */
        .add-to-cart-btn {
            background-color: #28a745;
            color: white;
            padding: 8px 16px;
            border: none;
            cursor: pointer;
            text-decoration: none;
        }
        .add-to-cart-btn:hover {
            background-color: #218838;
        }
    </style>
</head>
<body>
    <h1>Available Products</h1>
    <form action="AddToCartServlet" method="POST">
        <table>
            <thead>
                <tr>
                    <th>Product Image</th>
                    <th>Product Name</th>
                    <th>Price</th>
                    <th>Description</th>
                    <th>Quantity</th> <!-- Allow quantity input for each product -->
                    <th>Select</th> <!-- Checkbox to select product -->
                </tr>
            </thead>
            <tbody>
                <% for (Product product : products) { %>
                <% 
    byte[] imageBytes = product.getImageUrl();  // Assuming this returns byte[] (image data)
    String base64Image = java.util.Base64.getEncoder().encodeToString(imageBytes); 
%>
                    <tr>
                        <td><img src="data:image/jpg;base64,<%= base64Image %> %>" alt="<%= product.getProductName() %>"></td>
                        <td><%= product.getProductName() %></td>
                        <td>$<%= product.getPrice() %></td>
                        <td><%= product.getProductDescription() %></td>
                        <td>
                            <input type="number" name="quantities_<%= product.getProductId() %>" min="1" value="1" required> <!-- Quantity input -->
                        </td>
                        <td>
                            <input type="checkbox" name="productIds" value="<%= product.getProductId() %>">
                             <%System.out.println(product.getProductId()); %>
                        </td>
                    </tr>
                <% } %>
            </tbody>
        </table>
        <button type="submit" class="add-to-cart-btn">Add Selected Products to Cart</button>
    </form>
</body>
</html>
