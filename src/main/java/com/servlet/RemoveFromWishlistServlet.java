package com.servlet;

import com.DAO.DBConnectionManager;
import com.DAO.WishlistDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;

@WebServlet("/RemoveFromWishlistServlet")
public class RemoveFromWishlistServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve the user ID from the session
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        
        // Check if the user is logged in
        if (userId == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not logged in.");
            return;
        }
        
        // Retrieve the product ID from the request parameters
        int productId = Integer.parseInt(request.getParameter("productId"));

        try (Connection connection = DBConnectionManager.getConnection()) {
            // Create an instance of WishlistDAOImpl
            WishlistDAOImpl wishlistDAO = new WishlistDAOImpl();
            // Call the method to remove the item from the wishlist
            wishlistDAO.removeFromWishlist(userId, productId);

            // Redirect back to the wishlist page
            response.sendRedirect("wishlist.jsp");
        } catch (Exception e) {
            // Log the exception and return an error response
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to remove product from wishlist.");
        }
    }
}
