package com.DAO;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.Entity.Order;
import com.Entity.OrderItem;
import com.Entity.Product;
import com.Entity.cartdetails;

interface OrderDAO {
    int addOrder(Order order) throws SQLException;
    Order getOrderById(int orderId);
    List<Order> getOrdersByBuyerId(int buyerId);
    List<Order> getAllOrders() throws SQLException;
    void updateOrder(Order order);
    void deleteOrder(int orderId);
    public int getLatestOrderIdByUser(Integer userId) throws SQLException;
    public void updateOrderStatus(String orderId, String status) throws SQLException;
}

public class OrderDAOImpl implements OrderDAO {
    private Connection connection;

    public OrderDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public int addOrder(Order order) throws SQLException {
    	String sql = "INSERT INTO orders (user_id, total_amount, shipping_address, order_status, payment_status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, order.getUserId());
            statement.setBigDecimal(2, order.getTotalAmount());
            statement.setString(3, order.getShippingAddress());
            statement.setString(4, order.getOrderStatus());
            statement.setString(5, order.getPaymentStatus());

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1); // Return the generated order ID
                    }
                }
            }
            return 0; // No order created
        }
    }
    

    @Override
    public Order getOrderById(int orderId) {
        Order order = null;
        String query = "SELECT * FROM orders WHERE order_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, orderId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                order = new Order(rs.getInt("order_id"), rs.getInt("user_id"),
                        rs.getBigDecimal("total_amount"), rs.getString("shipping_address") ,rs.getString("order_status"),
                        rs.getString("payment_status"),null,null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return order;
    }

    @Override
    public List<Order> getOrdersByBuyerId(int buyerId) {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM orders WHERE buyer_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, buyerId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Order order = new Order(rs.getInt("order_id"), rs.getInt("user_id"),
                        rs.getBigDecimal("total_amount"), rs.getString("shipping_address") ,rs.getString("order_status"),
                        rs.getString("payment_status"),null,null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public List<Order> getAllOrders() throws SQLException {
    	List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM orders"; // Ensure this matches your actual table name
        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("order_id")); // Update to match your actual column names
                order.setUserId(rs.getInt("user_id"));
                order.setOrderStatus(rs.getString("order_status")); // Ensure "status" is the correct column name
                orders.add(order);
            }
        }
        
        // Debug log
        System.out.println("Retrieved Orders: " + orders.size());
        
        return orders;
    }

    @Override
    public void updateOrder(Order order) {
        String query = "UPDATE orders SET user_id = ?, total_amount = ?, order_status = ?, shipping_address = ?, payment_status = ? WHERE order_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, order.getUserId());
            pstmt.setBigDecimal(2, order.getTotalAmount());
            pstmt.setString(3, order.getOrderStatus());
            pstmt.setString(4, order.getShippingAddress());
            pstmt.setString(5, order.getPaymentStatus());
            pstmt.setInt(6, order.getOrderId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteOrder(int orderId) {
        String query = "DELETE FROM orders WHERE order_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, orderId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


	@Override
	public int getLatestOrderIdByUser(Integer userId) throws SQLException {
		String sql = "SELECT order_id FROM orders WHERE user_id = ? ORDER BY order_id DESC LIMIT 1";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("order_id"); // Return the latest inserted order_id
                } else {
                    throw new SQLException("No order found for user: " + userId);
                }
            }
        }
	}

	public void updateOrderStatus(String orderId, String status) throws SQLException {
        String sql = "UPDATE orders SET order_status = ? WHERE order_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, Integer.parseInt(orderId));
            pstmt.executeUpdate();
        }
    }
	
	public List<Order> getOrdersByUserId(Integer userId) {
	    List<Order> orders = new ArrayList<>();
	    Map<Integer, Order> orderMap = new HashMap<>(); // To track existing orders by order_id
	    
	    String sql = "SELECT o.order_id, o.created_at, o.order_status, " +
	                 "oi.product_id, p.product_name, oi.quantity, oi.price " +
	                 "FROM orders o " +
	                 "JOIN order_items oi ON o.order_id = oi.order_id " +
	                 "JOIN products p ON oi.product_id = p.product_id " + // Join with products table
	                 "WHERE o.user_id = ?"; // Assuming you have a user_id column in your orders table

	    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
	        stmt.setInt(1, userId);
	        ResultSet rs = stmt.executeQuery();
	        
	        while (rs.next()) {
	            int orderId = rs.getInt("order_id");

	            // Check if the order already exists in the map
	            Order order = orderMap.get(orderId);
	            if (order == null) {
	                // If order doesn't exist yet, create it and add to the map
	                order = new Order();
	                order.setOrderId(orderId);
	                order.setCreatedAt(rs.getTimestamp("created_at")); // Make sure column names match
	                order.setOrderStatus(rs.getString("order_status"));
	                
	                orders.add(order); // Add the new order to the list
	                orderMap.put(orderId, order); // Add to map to avoid duplicates
	            }

	            // Create OrderItem object
	            OrderItem item = new OrderItem();
	            Product product = new Product();
	            
	            item.setProductId(rs.getInt("product_id")); // Product ID
	            product.setProductName(rs.getString("product_name")); // Product Name from products table
	            item.setProduct(product); // Associate product with the item
	            item.setQuantity(rs.getInt("quantity"));
	            item.setPrice(rs.getDouble("price"));

	            // Add the order item to the existing order
	            order.addOrderItem(item); // Make sure addOrderItem() is implemented in Order class
	        }
	    } catch (SQLException e) {
	        e.printStackTrace(); // Handle exceptions
	    }

	    return orders;
	}
	
	public String getUserEmailByOrderId(Integer orderId) throws SQLException {
	    String email = null;
	    String query = "SELECT u.email FROM users u JOIN orders o ON u.user_id = o.user_id WHERE o.order_id = ?";
	    
	    try (PreparedStatement stmt = connection.prepareStatement(query)) {
	        stmt.setInt(1, orderId);
	        ResultSet rs = stmt.executeQuery();
	        
	        if (rs.next()) {
	            email = rs.getString("email");
	        }
	    }
	    
	    return email;
	}




    
   
}


