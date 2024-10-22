package com.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.RequestDispatcher;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.DAO.OrderDAOImpl;
import com.Entity.Order; // Import your Order entity

@WebServlet("/orders")
public class OrdersServlet extends HttpServlet {
    private Connection connection; // Assume this is initialized elsewhere

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
    	OrderDAOImpl orderDAO = new OrderDAOImpl(connection); // Instantiate your DAO
        List<Order> orders = null; // Initialize orders

        try {
            // Fetch all orders
            orders = orderDAO.getAllOrders();
            // Store orders in session instead of request
            HttpSession session = request.getSession();
            session.setAttribute("orders", orders); // Set the orders in session
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions accordingly
        }

        // Forward to the JSP page to display orders
        RequestDispatcher dispatcher = request.getRequestDispatcher("orders.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
    	OrderDAOImpl orderDAO = new OrderDAOImpl(connection); // Instantiate your DAO
        String action = request.getParameter("action");

        if ("update".equals(action)) {
            String orderId = request.getParameter("orderId");
            String status = request.getParameter("status");

            try {
                // Update the order status in the database
                orderDAO.updateOrderStatus(orderId, status);
                // Optionally, you can add a success message here
            } catch (SQLException e) {
                e.printStackTrace(); // Handle exceptions accordingly
            }
        }

        // Redirect back to the orders page after update
        response.sendRedirect("orders");
    }
}
