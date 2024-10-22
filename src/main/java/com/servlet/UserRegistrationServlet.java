package com.servlet;

import com.Entity.User_Registration;
import com.DAO.UserDAOImplementation1;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/register")  // URL pattern to map this servlet
public class UserRegistrationServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Retrieve form parameters from the request
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String role = request.getParameter("role");
        String businessDetails = request.getParameter("businessDetails");

        // Create a User_Registration object and set the user details
        User_Registration user = new User_Registration();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);
        user.setBusinessDetails(businessDetails != null ? businessDetails : ""); // Handle nullable field

        // Use UserDAOImplementation1 to save the user in the database
        UserDAOImplementation1 userDAO = new UserDAOImplementation1();
        try {
            if(user.getRole().equals("buyer"))
            {
            	userDAO.addUser(user); // Call the addUser method to store user data

                // Show a success message to the user
                //response.getWriter().println("User registration successful!");
                response.sendRedirect("login.jsp");
           }
            else if(user.getRole().equals("seller"))
            {
            	userDAO.addUser(user);
                response.sendRedirect("sellerlogin.jsp");
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();

            // If an error occurs, send an error message to the user
            response.getWriter().println("Error occurred during registration: " + e.getMessage());
        }
    }
}
