package com.servlet;

import com.Entity.User_Registration;
import com.DAO.LoginDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/sellerLogin") // URL pattern to map this servlet
public class SellerLoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve form parameters from the request
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Create an instance of LoginDAO
        LoginDAO loginDAO = new LoginDAO();
        User_Registration user = null;

        try {
            // Validate seller login
            user = loginDAO.validateSellerLogin(email, password);
            HttpSession session = request.getSession();
            session.setAttribute("sellerId", user.getUserId());
            if (user != null) {
                // Redirect to seller dashboard if login is successful
                response.sendRedirect("sellerDashboard.jsp");
            } else {
                // Send error message for invalid seller credentials
                request.setAttribute("errorMessage", "Invalid Seller Credentials");
                request.getRequestDispatcher("sellerLogin.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Send error message in case of SQL Exception
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("sellerLogin.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            // Send error message in case of general Exception
            request.setAttribute("errorMessage", "Error occurred: " + e.getMessage());
            request.getRequestDispatcher("sellerLogin.jsp").forward(request, response);
        }
    }
}
