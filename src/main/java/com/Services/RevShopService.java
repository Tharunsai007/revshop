package com.Services;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.DAO.DBConnectionManager;
import com.DAO.LoginDAO;
import com.DAO.OrderDAOImpl;
import com.DAO.OrderItemDAOImpl;
import com.DAO.UserDAOImplementation1;
import com.DAO.productDAOImp1;
import com.Entity.Order;
import com.Entity.OrderItem;
import com.Entity.Product;
import com.Entity.User_Registration;

public class RevShopService {

    public static void main(String[] args) throws ClassNotFoundException {
    	 RevShopService service = new RevShopService();
         service.execute();
     }

     public void execute() throws ClassNotFoundException {
         try (Connection connection = DBConnectionManager.getConnection()) {
             // Create DAO instances with the connection
             UserDAOImplementation1 userDAO = new UserDAOImplementation1();
             productDAOImp1 productDAO = new productDAOImp1();
             OrderDAOImpl orderDAO = new OrderDAOImpl(connection);
             OrderItemDAOImpl orderItemsDAO = new OrderItemDAOImpl(connection);
             LoginDAO loginDAO = new LoginDAO();

             Scanner scanner = new Scanner(System.in);

             // 1. User Login
             System.out.println("Welcome to RevShop. Please login.");
             System.out.print("Enter email: ");
             String email = scanner.nextLine();
             System.out.print("Enter password: ");
             String password = scanner.nextLine();

             User_Registration user = loginDAO.validateBuyerLogin(email, password);
             if (user == null) {
                 System.out.println("Invalid credentials, login failed.");
                 return;
             }
             System.out.println("Login successful. Welcome, " + user.getFirstName());

             // 2. Display Products for Buyer to Select
             List<Product> products = productDAO.getAllProducts();
             System.out.println("Available Products:");
             for (Product product : products) {
                 System.out.println("Product ID: " + product.getProductId() + ", Name: " + product.getProductName() + ", Price: $" + product.getPrice());
             }

             // 3. Add Products to Cart
             List<OrderItem> cartItems = new ArrayList<>();
             String addMore;
             do {
                 System.out.print("Enter product ID to add to cart: ");
                 int productId = scanner.nextInt();
                 Product selectedProduct = productDAO.getProductById(productId);

                 if (selectedProduct != null) {
                     System.out.print("Enter quantity: ");
                     int quantity = scanner.nextInt();
                     OrderItem orderItem = new OrderItem();
                     orderItem.setProductId(productId);
                     orderItem.setQuantity(quantity);
                     orderItem.setPrice(selectedProduct.getPrice() * quantity);  // Calculate total price for this item
                     cartItems.add(orderItem);
                 } else {
                     System.out.println("Product not found!");
                 }

                 System.out.print("Would you like to add more products to your cart? (yes/no): ");
                 addMore = scanner.next();
             } while (addMore.equalsIgnoreCase("yes"));

             // 4. Checkout and Place Order
             if (!cartItems.isEmpty()) {
                 // Create the order without the total amount
                 Order order = new Order();
                 order.setUserId(user.getUserId());

                 System.out.print("Enter shipping address: ");
                 scanner.nextLine(); // Consume newline
                 String shippingAddress = scanner.nextLine(); // Get shipping address from user
                 order.setShippingAddress(shippingAddress);
                 
              // Calculate total order price manually from the cart items
                 BigDecimal totalOrderPrice = BigDecimal.ZERO;
                 for (OrderItem orderItem : cartItems) {
                     totalOrderPrice = totalOrderPrice.add(BigDecimal.valueOf(orderItem.getPrice()));
                 }
                 order.setTotalAmount(totalOrderPrice);
                 
                 order.setOrderStatus("Pending");// Set the initial status for the order
                 
              // Set the default payment status (e.g., "Unpaid")
                 order.setPaymentStatus("Unpaid");

                 // Save the order and get the generated order ID
                 int orderId = orderDAO.addOrder(order);
                 order.setOrderId(orderId);

                 // 5. Save Order Items
                // BigDecimal totalOrderPrice = BigDecimal.ZERO;
                 for (OrderItem orderItem : cartItems) {
                     orderItem.setOrderId(orderId);
                     orderItemsDAO.addOrderItem(orderItem);  // Save each order item

                     // Accumulate total price
                    // totalOrderPrice = totalOrderPrice.add(BigDecimal.valueOf(orderItem.getPrice()));
                 }

                 // 6. Update the order with the total amount
                 order.setTotalAmount(totalOrderPrice);
                 orderDAO.updateOrder(order);  // Update the order with the calculated total

                 System.out.println("Order placed successfully! Total Order Price: $" + totalOrderPrice);
             } else {
                 System.out.println("No products in the cart. Order was not placed.");
             }

         } catch (SQLException e) {
             System.err.println("Database error: " + e.getMessage());
         }
     }
}


