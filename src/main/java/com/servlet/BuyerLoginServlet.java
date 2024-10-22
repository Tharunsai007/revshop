package com.servlet;

import com.Entity.User_Registration;
import com.DAO.LoginDAO;
import com.DAO.UserDAOImplementation1;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/buyerLogin") // URL pattern to map this servlet
public class BuyerLoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve form parameters from the request
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Create an instance of LoginDAO
        LoginDAO loginDAO = new LoginDAO();
        User_Registration user = null;
        User_Registration user1 = null;
        UserDAOImplementation1 userDAO=new UserDAOImplementation1();

        try {
            // Validate buyer login
        	user1 = userDAO.getUserByEmail(email);
           user = loginDAO.validateBuyerLogin(email, password);
           HttpSession session = request.getSession();
           Object retrievedUserId = user1.getUserId();
		session.setAttribute("userId", retrievedUserId);
            if (user != null ) {
                // Redirect to buyer dashboard if login is successful
                response.sendRedirect("buyerDashboard.jsp");
            } else {
                // Send error message for invalid buyer credentials
                request.setAttribute("errorMessage", "Invalid Buyer Credentials");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Send error message in case of SQL Exception
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            // Send error message in case of general Exception
            request.setAttribute("errorMessage", "Error occurred: " + e.getMessage());
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
