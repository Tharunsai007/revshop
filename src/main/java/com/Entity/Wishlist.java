package com.Entity;

import com.google.protobuf.Timestamp;

public class Wishlist {

	private int wishlistId;
    private int userId;
    private int productId;
    private Timestamp addedDate;
	public Wishlist(int wishlistId, int userId, int productId, Timestamp addedDate) {
		super();
		this.wishlistId = wishlistId;
		this.userId = userId;
		this.productId = productId;
		this.addedDate = addedDate;
	}
	public int getWishlistId() {
		return wishlistId;
	}
	public void setWishlistId(int wishlistId) {
		this.wishlistId = wishlistId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public Timestamp getAddedDate() {
		return addedDate;
	}
	public void setAddedDate(Timestamp addedDate) {
		this.addedDate = addedDate;
	}
    
    
}
