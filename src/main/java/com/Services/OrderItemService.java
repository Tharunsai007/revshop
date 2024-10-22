package com.Services;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.DAO.DBConnectionManager;
import com.DAO.OrderItemDAOImpl;
import com.Entity.OrderItem;

public class OrderItemService {
	private final OrderItemDAOImpl orderItemDAO;

	
    public OrderItemService(Connection connection) {
        this.orderItemDAO = new OrderItemDAOImpl(connection);
    }

    public void addOrderItem(OrderItem orderItem) {
        // Validate the order item before adding
        if (orderItem == null) {
            throw new IllegalArgumentException("OrderItem cannot be null");
        }
        orderItemDAO.addOrderItem(orderItem);
    }

    public List<OrderItem> getOrderItemsByOrderId(int orderId) {
        // Validate the order ID
        if (orderId <= 0) {
            throw new IllegalArgumentException("Order ID must be positive");
        }
        return orderItemDAO.getOrderItemsByOrderId(orderId);
    }

    public BigDecimal getOrderTotal(int orderId) {
        // Validate the order ID
        if (orderId <= 0) {
            throw new IllegalArgumentException("Order ID must be positive");
        }
        return orderItemDAO.getOrderTotal(orderId);
    }
}

