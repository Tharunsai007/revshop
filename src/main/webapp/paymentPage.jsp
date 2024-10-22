<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <title>RevShop - Payment</title>
    <style>
        /* Basic styling */
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f4f4f4;
        }

        .container {
            width: 50%;
            margin: 50px auto;
            background-color: #fff;
            padding: 20px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        h3 {
            text-align: center;
        }

        label {
            display: block;
            margin: 15px 0 10px;
        }

        input[type="radio"] {
            margin-right: 10px;
        }

        .payment-methods {
            margin-top: 20px;
        }

        .submit-btn {
            display: block;
            width: 100%;
            padding: 10px;
            background-color: #007bff;
            color: white;
            border: none;
            cursor: pointer;
            margin-top: 20px;
        }

        .submit-btn:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
    <div class="container">
        <h3>Payment Confirmation</h3>

        <form action="PaymentProcessingServlet" method="POST" id="paymentForm">
            <!-- Hidden fields to pass order-related and user-related information -->
            <input type="hidden" name="billingInfo" value="<%= session.getAttribute("billingInfo") %>">
            <input type="hidden" name="totalPrice" value="<%= session.getAttribute("totalPrice") %>">
            <input type="hidden" name="userEmail" value="<%= session.getAttribute("userEmail") %>">
            <input type="hidden" name="userName" value="<%= session.getAttribute("userName") %>">
            <%System.out.println(session.getAttribute("totalPrice")); %>

            <!-- Display total price to user -->
            <h4>Total Amount: $<%= session.getAttribute("totalPrice") != null ? String.format("%.2f", session.getAttribute("totalPrice")) : "0.00" %></h4>

            <!-- Payment Methods -->
            <div class="payment-methods">
                <h3>Select Payment Method</h3>
                <label>
                    <input type="radio" name="paymentMethod" value="credit_card" required> Credit Card
                </label>
                <label>
                    <input type="radio" name="paymentMethod" value="debit_card" required> Debit Card
                </label>
                <label>
                    <input type="radio" name="paymentMethod" value="upi" required> UPI (Unified Payments Interface)
                </label>
            </div>

            <!-- Payment details will be fetched dynamically based on payment method -->
            <div id="paymentDetails"></div>

            <!-- Submit button to confirm payment -->
            <input type="submit" value="Confirm Payment" class="submit-btn">
        </form>
    </div>

    <script>
        // Optional: Show payment details dynamically based on selected payment method
        function showPaymentDetails() {
            var selectedPaymentMethod = document.querySelector('input[name="paymentMethod"]:checked').value;
            var paymentDetailsDiv = document.getElementById("paymentDetails");
            paymentDetailsDiv.innerHTML = '';

            if (selectedPaymentMethod === 'credit_card' || selectedPaymentMethod === 'debit_card') {
                paymentDetailsDiv.innerHTML = `
                    <h4>Enter ${selectedPaymentMethod.replace('_', ' ').toUpperCase()} Details</h4>
                    <label>Card Number: <input type="text" name="cardNumber" pattern="\\d{16}" required title="Please enter a valid 16-digit card number"></label>
                    <label>Expiry Date (MM/YY): <input type="text" name="expiryDate" pattern="(0[1-9]|1[0-2])/[0-9]{2}" required title="Format: MM/YY"></label>
                    <label>CVV: <input type="text" name="cvv" pattern="\\d{3}" required title="Please enter a valid 3-digit CVV"></label>
                `;
            } else if (selectedPaymentMethod === 'upi') {
                paymentDetailsDiv.innerHTML = `
                    <h4>Enter UPI ID</h4>
                    <label>UPI ID: <input type="text" name="upiId" required></label>
                `;
            }
        }

        // Add an event listener to payment method radio buttons to show payment details
        var paymentMethods = document.querySelectorAll('input[name="paymentMethod"]');
        paymentMethods.forEach(function (radio) {
            radio.addEventListener('change', showPaymentDetails);
        });
    </script>
</body>
</html>
