package com.servlet;

import com.DAO.productDAOImp1;
import com.Entity.Product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/buyerDashboard")
public class BuyerDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get the session object
        HttpSession session = request.getSession();

        // Retrieve the selected category ID from the session
        Integer selectedCategoryId = (Integer) session.getAttribute("selectedCategoryId");

        // If the request has a new category, update the session
        String newCategory = request.getParameter("category");
        if (newCategory != null && !newCategory.isEmpty()) {
            try {
                selectedCategoryId = Integer.parseInt(newCategory); // Convert to Integer
                session.setAttribute("selectedCategoryId", selectedCategoryId);
            } catch (NumberFormatException e) {
                // Handle the case where conversion fails (e.g., invalid category ID)
                selectedCategoryId = null; // Set to null or handle as needed
            }
        }

        List<Product> products = null;

        try {
            // Create an instance of ProductDAO
            productDAOImp1 productDAO = new productDAOImp1();

            // If no category is selected, show all products
            if (selectedCategoryId == null) {
                products = productDAO.getAllProducts();
            } else {
                // Filter products by selected category ID
                products = productDAO.getProductsByCategory(selectedCategoryId);
            }

            // Set the products as a request attribute
            request.setAttribute("products", products);
            request.setAttribute("selectedCategoryId", selectedCategoryId);

            // Forward the request to the buyer dashboard JSP
            request.getRequestDispatcher("buyerDashboard.jsp").forward(request, response);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            // Handle errors by setting an error message and forwarding to an error page
            request.setAttribute("errorMessage", "Error retrieving products: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}
