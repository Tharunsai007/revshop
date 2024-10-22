<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add New Product</title>
    <style>
        /* CSS Styles */
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
            color: #333;
        }

        /* Navbar styles */
        .navbar {
            background-color: #3498db;
            overflow: hidden;
            display: flex;
            justify-content: space-around;
            padding: 10px 20px;
        }

        .navbar a {
            color: white;
            text-decoration: none;
            padding: 14px 20px;
            transition: background-color 0.3s;
        }

        .navbar a:hover {
            background-color: #2980b9;
        }

        h1 {
            text-align: center;
            color: #2c3e50;
            margin-top: 30px;
        }

        form {
            max-width: 600px;
            margin: 0 auto;
            background-color: white;
            padding: 20px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
            margin-top: 20px;
        }

        label {
            font-weight: bold;
            color: #2c3e50;
        }

        input[type="text"], 
        input[type="number"],
        input[type="file"], 
        textarea {
            width: 100%;
            padding: 10px;
            margin: 8px 0;
            box-sizing: border-box;
            border: 1px solid #ccc;
            border-radius: 4px;
        }

        textarea {
            resize: vertical;
        }

        input[type="submit"] {
            background-color: #3498db;
            color: white;
            padding: 12px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            width: 100%;
            font-size: 16px;
            margin-top: 20px;
        }

        input[type="submit"]:hover {
            background-color: #2980b9;
        }

        .form-group {
            margin-bottom: 15px;
        }
    </style>
</head>
<body>

    <!-- Navbar -->
    <div class="navbar">
        <a href="sellerDashboard.jsp">Home</a>
        <a href="addProduct.jsp">Add Product</a>
        <a href="viewProducts.jsp">View Products</a>
        <a href="logout">Logout</a>
    </div>

    <h1>Add New Product</h1>

    <form action="addProduct" method="post" enctype="multipart/form-data">
        <div class="form-group">
            <label for="sellerId">Seller ID:</label>
            <input type="number" id="sellerId" name="sellerId" required>
        </div>

        <div class="form-group">
            <label for="productName">Product Name:</label>
            <input type="text" id="productName" name="productName" required>
        </div>

        <div class="form-group">
            <label for="productDescription">Product Description:</label>
            <textarea id="productDescription" name="productDescription" required></textarea>
        </div>

        <div class="form-group">
            <label for="price">Price:</label>
            <input type="number" step="0.01" id="price" name="price" required>
        </div>

        <div class="form-group">
            <label for="discountedPrice">Discounted Price (optional):</label>
            <input type="number" step="0.01" id="discountedPrice" name="discountedPrice">
        </div>

        <div class="form-group">
            <label for="quantityInStock">Quantity in Stock:</label>
            <input type="number" id="quantityInStock" name="quantityInStock" required>
        </div>

        <div class="form-group">
            <label for="categoryId">Category ID:</label>
            <input type="number" id="categoryId" name="categoryId" required>
        </div>

        <div class="form-group">
            <label for="imageFile">Product Image:</label>
            <input type="file" id="imageFile" name="imageFile" accept="image/*" required>
        </div>

        <input type="submit" value="Add Product">
    </form>

</body>
</html>
