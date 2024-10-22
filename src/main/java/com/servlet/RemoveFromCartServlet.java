package com.servlet;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.DAO.CartDAO;
import Dbconnection.DBConnectionManager;

@WebServlet("/RemoveFromCartServlet")
public class RemoveFromCartServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the cart ID from the request
        String cartIdString = request.getParameter("cartId");
        int cartId = 0;
        if (cartIdString != null) {
            cartId = Integer.parseInt(cartIdString);
        }

        // Check if cartId is valid
        if (cartId > 0) {
            // Instantiate the CartDAO and remove the item from the cart
            CartDAO cartDAO = null;
            try {
                cartDAO = new CartDAO(DBConnectionManager.getConnection());
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
                request.getSession().setAttribute("message", "Database connection error.");
                response.sendRedirect("cart.jsp");
                return; // Ensure to exit if there's an error
            }

            boolean removed = cartDAO.removeFromCart(cartId);

            if (removed) {
                // Redirect back to the cart page with a success message
                request.getSession().setAttribute("message", "Item removed from cart successfully!");
            } else {
                // If removal failed, set an error message
                request.getSession().setAttribute("message", "Failed to remove item from cart.");
            }
        } else {
            request.getSession().setAttribute("message", "Invalid cart item.");
        }

        response.sendRedirect("cart.jsp");
    }
}
