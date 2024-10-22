package com.DAO;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.Entity.OrderItem;

interface OrderItemDAO {
    void addOrderItem(OrderItem orderItem);
    List<OrderItem> getOrderItemsByOrderId(int orderId);
    BigDecimal getOrderTotal(int orderId);
}

public class OrderItemDAOImpl implements OrderItemDAO{

	 private Connection connection;

	    public OrderItemDAOImpl(Connection connection) {
	        this.connection = connection;
	    }

	    @Override
	    public void addOrderItem(OrderItem orderItem) {
	        String query = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
	        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	            pstmt.setInt(1, orderItem.getOrderId());
	            pstmt.setInt(2, orderItem.getProductId());
	            pstmt.setInt(3, orderItem.getQuantity());
	            pstmt.setDouble(4, orderItem.getPrice());
	            pstmt.executeUpdate();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    @Override
	    public List<OrderItem> getOrderItemsByOrderId(int orderId) {
	        List<OrderItem> orderItems = new ArrayList<>();
	        String query = "SELECT * FROM order_items WHERE order_id = ?";
	        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	            pstmt.setInt(1, orderId);
	            ResultSet rs = pstmt.executeQuery();
	            while (rs.next()) {
	                OrderItem orderItem = new OrderItem();
	                orderItem.setOrderItemId(rs.getInt("order_item_id"));
	                orderItem.setOrderId(rs.getInt("order_id"));
	                orderItem.setProductId(rs.getInt("product_id"));
	                orderItem.setQuantity(rs.getInt("quantity"));
	                orderItem.setPrice(rs.getDouble("price"));
	                orderItems.add(orderItem);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return orderItems;
	    }

	    @Override
	    public BigDecimal getOrderTotal(int orderId) {
	        BigDecimal total = new BigDecimal("0.0");
	        String query = "SELECT SUM(COALESCE(quantity, 0) * COALESCE(price, 0)) AS total_price FROM order_items WHERE order_id = ?";
	        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	            pstmt.setInt(1, orderId);
	            ResultSet rs = pstmt.executeQuery();
	            if (rs.next()) {
	                total = rs.getBigDecimal("total_price");
	                if (total == null) {  // Handle case where the sum might be null
	                    total = BigDecimal.ZERO;
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        System.out.println(total);
	        return total;
	    }


}
