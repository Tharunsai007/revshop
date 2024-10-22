package com.servlet;

import com.DAO.WishlistDAOImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/AddToWishlistServlet")
public class AddToWishlistServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private WishlistDAOImpl wishlistDAO;

    @Override
    public void init() throws ServletException {
        try {
            wishlistDAO = new WishlistDAOImpl();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new ServletException("Failed to initialize WishlistDAO", e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve the single productId from the request
        String productIdStr = request.getParameter("productId");

        // Get the current session and retrieve userId
        HttpSession session = request.getSession(false);
        Integer userId = (session != null) ? (Integer) session.getAttribute("userId") : null;

        if (userId == null) {
            response.sendRedirect("login.jsp"); // Redirect if the user is not logged in
            return;
        }

        if (productIdStr != null && !productIdStr.isEmpty()) {
            try {
                int productId = Integer.parseInt(productIdStr);
                wishlistDAO.addToWishlist(productId, userId);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        // Redirect back to the dashboard or wishlist page
        response.sendRedirect("buyerDashboard.jsp"); // Or "WishlistServlet" to view the wishlist
    }
}
