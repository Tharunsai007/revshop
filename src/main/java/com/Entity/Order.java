package com.Entity;

import java.math.BigDecimal;
import java.util.List;

import com.google.protobuf.Timestamp;

public class Order {
	
	private int orderId;
    private int userId;
    private BigDecimal totalAmount;
    private String shippingAddress;
    private String orderStatus;
    private String paymentStatus;
    private java.sql.Timestamp createdAt;
    private Timestamp updatedAt;
    
    private List<OrderItem> orderItems;
    
	public Order(int orderId, int userId, BigDecimal totalAmount, String orderStatus, String shippingAddress,
			String paymentStatus, java.sql.Timestamp createdAt, Timestamp updatedAt) {
		super();
		this.orderId = orderId;
		this.userId = userId;
		this.totalAmount = totalAmount;
		this.shippingAddress = shippingAddress;
		this.orderStatus = orderStatus;
		this.paymentStatus = paymentStatus;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
	public Order() {
		// TODO Auto-generated constructor stub
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalOrderPrice) {
		this.totalAmount = totalOrderPrice;
	}
	public String getShippingAddress() {
		return shippingAddress;
	}
	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public java.sql.Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(java.sql.Timestamp timestamp) {
		this.createdAt = timestamp;
	}
	public Timestamp getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	
	public List<OrderItem> getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}
	
	
	 public BigDecimal calculateTotalPrice() {
	        BigDecimal total = BigDecimal.ZERO;
	        for (OrderItem item : orderItems) {
	            total = total.add(BigDecimal.valueOf(item.getPrice()).multiply(new BigDecimal(item.getQuantity())));
	        }
	        return total;
	    }
	public void addOrderItem(OrderItem item) {
		// TODO Auto-generated method stub
		
	}
	
	
	
    
    

}
