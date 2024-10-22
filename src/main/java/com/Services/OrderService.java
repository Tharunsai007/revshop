package com.Services;

import java.math.BigDecimal;
//import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.DAO.CartDAO;
import com.DAO.DBConnectionManager;
import com.DAO.OrderDAOImpl;
import com.DAO.productDAOImp1;
import com.Entity.Order;
import com.Entity.cartdetails;

public class OrderService {
	private final OrderDAOImpl orderDAO;

    public OrderService(Connection connection) {
        this.orderDAO = new OrderDAOImpl(connection);
    }

    public int addOrder(Order order) throws SQLException {
        // Validate order details before adding
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        return orderDAO.addOrder(order);
    }

    public Order getOrderById(int orderId) {
        // Validate order ID
        if (orderId <= 0) {
            throw new IllegalArgumentException("Order ID must be positive");
        }
        return orderDAO.getOrderById(orderId);
    }

    public List<Order> getOrdersByBuyerId(int buyerId) {
        // Validate buyer ID
        if (buyerId <= 0) {
            throw new IllegalArgumentException("Buyer ID must be positive");
        }
        return orderDAO.getOrdersByBuyerId(buyerId);
    }

    public List<Order> getAllOrders() {
        try {
			return orderDAO.getAllOrders();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }

    public void updateOrder(Order order) {
        // Validate order details before updating
        if (order == null || order.getOrderId() <= 0) {
            throw new IllegalArgumentException("Invalid order details");
        }
        orderDAO.updateOrder(order);
    }

    public void deleteOrder(int orderId) {
        // Validate order ID
        if (orderId <= 0) {
            throw new IllegalArgumentException("Order ID must be positive");
        }
        orderDAO.deleteOrder(orderId);
    }
    }

