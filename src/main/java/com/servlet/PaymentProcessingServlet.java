package com.servlet;

import com.servlet.MailUtility;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.mail.MessagingException;  // Updated import statement
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/PaymentProcessingServlet")
public class PaymentProcessingServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get session for billing info, total price, and user details
        HttpSession session = request.getSession();
        String billingInfo = (String) session.getAttribute("billingInfo");
        String totalPriceParam = request.getParameter("totalPrice");
        BigDecimal totalPrice = new BigDecimal(totalPriceParam);
        String userEmail = (String) session.getAttribute("userEmail");
        String userName = (String) session.getAttribute("userName");

        // Extract payment method from request
        String paymentMethod = request.getParameter("paymentMethod");

        
        // Process the payment (stubbed out for now)
        boolean paymentSuccess = true;//processPayment(paymentMethod, billingInfo);

        if (paymentSuccess) {
            String orderDetails = "Total Price: ₹" + totalPrice + "<br>Billing Info: " + billingInfo;
			try {
				MailUtility.sendOrderConfirmation("sricharanreddygatla@gmail.com", userName, orderDetails);
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            // Redirect to a success page after payment and email confirmation
            response.sendRedirect("orderSuccess.jsp");

        } else {
            // Payment failed, handle the error and forward to an error page
            request.setAttribute("error", "Payment failed. Please try again.");
            request.getRequestDispatcher("paymentError.jsp").forward(request, response);
        }
    }

    // Dummy method to simulate payment processing logic
    private boolean processPayment(String paymentMethod, String billingInfo) {
        // Here, you would implement the actual payment processing logic (e.g., Razorpay)
        return true; // Simulating successful payment for now
    }
}
