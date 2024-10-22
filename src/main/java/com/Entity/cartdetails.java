package com.Entity;

import com.google.protobuf.Timestamp;

public class cartdetails {
	
	private int cartId;
    private int buyerId;
    private int productId;
    private int quantity;
    private Timestamp addedAt;
	public cartdetails(int cartId, int buyerId, int productId, int quantity,Timestamp addedAt) {
		super();
		this.cartId = cartId;
		this.buyerId = buyerId;
		this.productId = productId;
		this.quantity = quantity;
		this.addedAt = addedAt;
	}
	public int getCartId() {
		return cartId;
	}
	public void setCartId(int cartId) {
		this.cartId = cartId;
	}
	public int getBuyerId() {
		return buyerId;
	}
	public void setBuyerId(int buyerId) {
		this.buyerId = buyerId;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public Timestamp getAddedAt() {
		return addedAt;
	}
	public void setAddedAt(Timestamp addedAt) {
		this.addedAt = addedAt;
	}
	
    
    
	
	

}
