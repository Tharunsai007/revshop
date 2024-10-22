package com.servlet;

import com.DAO.CartDAO;
import com.DAO.DBConnectionManager;
import com.Entity.cartdetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/AddToCartServlet")
public class AddToCartServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection connection = null;
        try {
            // Get user ID from session
            Integer userId = (Integer) request.getSession().getAttribute("userId");

            // Check if the user is logged in (userId should not be null)
            if (userId == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            // Get productId and quantity from form data
            int productId = Integer.parseInt(request.getParameter("productId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            // Get a connection to the database
            connection = DBConnectionManager.getConnection();

            // Create CartDAO object
            CartDAO cartDAO = new CartDAO(connection);

            // Check if the product is already in the user's cart
            cartdetails existingCartItem = cartDAO.getCartItem(userId, productId);

            if (existingCartItem != null) {
                // If the product is already in the cart, update the quantity
                int newQuantity = existingCartItem.getQuantity() + quantity;
                cartDAO.updateCartQuantity(existingCartItem.getCartId(), newQuantity);
            } else {
                // If the product is not in the cart, add it
                cartdetails cartItem = new cartdetails(0, userId, productId, quantity, null);
                cartDAO.addToCart(cartItem);
            }

            request.getSession().setAttribute("cartMessage", "Product added to cart successfully!");
            response.sendRedirect("buyerDashboard.jsp");

        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to add product to cart.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            DBConnectionManager.closeConnection(connection);
        }
    }

}
