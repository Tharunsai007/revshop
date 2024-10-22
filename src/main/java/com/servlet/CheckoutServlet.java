package com.servlet;

import com.DAO.*;
import com.Entity.*;
import Dbconnection.DBConnectionManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/CheckoutServlet")
public class CheckoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection connection = null;
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        // Redirect to login page if user is not logged in
        if (userId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            // Get database connection
            connection = DBConnectionManager.getConnection();
            CartDAO cartDAO = new CartDAO(connection);
            OrderDAOImpl orderDAO = new OrderDAOImpl(connection);
            OrderItemDAOImpl orderItemDAO = new OrderItemDAOImpl(connection);
            productDAOImp1 productDAO = new productDAOImp1(); // Instantiate the product DAO

            // Get cart items for the user
            List<cartdetails> cartItems = cartDAO.getCartItemsByBuyerId(userId);
            if (cartItems.isEmpty()) {
                response.sendRedirect("cart.jsp?message=Your cart is empty.");
                return;
            }

            BigDecimal totalAmount = BigDecimal.ZERO;

            // Calculate total order amount
            for (cartdetails item : cartItems) {
                Product product = productDAO.getProductById(item.getProductId());
                if (product != null) {
                    BigDecimal itemPrice = BigDecimal.valueOf(product.getPrice());
                    totalAmount = totalAmount.add(itemPrice.multiply(BigDecimal.valueOf(item.getQuantity())));
                } else {
                    response.sendRedirect("error.jsp?message=Product not found");
                    return;
                }
            }
            
            String shippingAddress = request.getParameter("shippingAddress");
            if (shippingAddress == null || shippingAddress.isEmpty()) {
                System.out.println("Shipping address is null or empty");
                response.sendRedirect("error.jsp?message=Shipping address can't be null.");
                return;
            }

            // Create the Order entity and persist it
            Order order = new Order();
            order.setUserId(userId);
            order.setTotalAmount(totalAmount);
            order.setShippingAddress(shippingAddress);
            order.setOrderStatus("Pending");
            order.setPaymentStatus("Not Paid");

            // Insert order and get the generated order ID
            orderDAO.addOrder(order);
            int orderId = orderDAO.getLatestOrderIdByUser(userId); // Get the latest orderId for the user
            
            // Check if orderId was properly generated
            if (orderId == 0) {
                response.sendRedirect("error.jsp?message=Order creation failed");
                return;
            }

            session.setAttribute("orderId", orderId); 
            session.setAttribute("billingInfo", shippingAddress); 
            session.setAttribute("totalPrice", totalAmount); 
            request.setAttribute("totalPrice", String.valueOf(totalAmount));


            // Add order items
            for (cartdetails cartItem : cartItems) {
                Product product = productDAO.getProductById(cartItem.getProductId());
                if (product != null) {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrderId(orderId);
                    orderItem.setProductId(cartItem.getProductId());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setPrice(product.getPrice());
                    orderItemDAO.addOrderItem(orderItem);
                }
            }

            // Clear cart after successful order placement
            cartDAO.clearCartByBuyerId(userId);

            // Redirect to payment page instead of order confirmation
            response.sendRedirect("orderConfirmation.jsp");  // Change to your payment page JSP name
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp?message=An error occurred while processing your order."); // Redirect to an error page
        } finally {
            if (connection != null) {
                try {
                    connection.close(); // Close the connection to the database
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
