package com.servlet;

import com.DAO.productDAOImp1;
import com.Entity.Product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

@WebServlet("/addProduct")
@MultipartConfig(maxFileSize = 16177215) // Maximum file size to handle image upload
public class AddProductServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Retrieve product details from the request
        String productName = request.getParameter("productName");
        String productDescription = request.getParameter("productDescription");
        double price = Double.parseDouble(request.getParameter("price"));
        String discountedPriceStr = request.getParameter("discountedPrice");
        Double discountedPrice = (discountedPriceStr.isEmpty()) ? null : Double.parseDouble(discountedPriceStr);
        int quantityInStock = Integer.parseInt(request.getParameter("quantityInStock"));
        int categoryId = Integer.parseInt(request.getParameter("categoryId"));
        int sellerId = Integer.parseInt(request.getParameter("sellerId"));

        // Retrieve the image file (as a part) from the request
        Part filePart = request.getPart("imageFile"); 
        InputStream imageInputStream = null;

        if (filePart != null) {
            // Get the input stream to read the file's content
            imageInputStream = filePart.getInputStream();
        }

        // Create a Product object (with null for productId as it will be auto-generated)
        Product product = new Product(0, sellerId, productName, productDescription, price, discountedPrice, quantityInStock, categoryId, null); 

        try {
            // Use the DAO to add the product to the database, passing the image stream
            productDAOImp1 productDAO = new productDAOImp1();
            productDAO.addProductWithImage(product, imageInputStream);

            // Redirect to a confirmation page or the seller dashboard
            response.sendRedirect("sellerDashboard.jsp");

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            // If there's an error, redirect back to the form with an error message
            request.setAttribute("errorMessage", "Error adding product: " + e.getMessage());
            request.getRequestDispatcher("addProductForm.jsp").forward(request, response);
        }
    }
}
