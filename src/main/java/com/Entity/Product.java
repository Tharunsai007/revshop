package com.Entity;

public class Product {
	
	private int productId;
    private int sellerId;
    private String productName;
    private String productDescription;
    private double price;
    private Double discountedPrice;  // Nullable
    private int quantityInStock;
    private int categoryId;
    private byte[] imageUrl;
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public int getSellerId() {
		return sellerId;
	}
	public void setSellerId(int sellerId) {
		this.sellerId = sellerId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductDescription() {
		return productDescription;
	}
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public Double getDiscountedPrice() {
		return discountedPrice;
	}
	public void setDiscountedPrice(Double discountedPrice) {
		this.discountedPrice = discountedPrice;
	}
	public int getQuantityInStock() {
		return quantityInStock;
	}
	public void setQuantityInStock(int quantityInStock) {
		this.quantityInStock = quantityInStock;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public byte[] getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(byte[] imageUrl) {
		this.imageUrl = imageUrl;
	}
	public Product(int productId, int sellerId, String productName, String productDescription, double price,
			Double discountedPrice, int quantityInStock, int categoryId, byte[] imageUrl) {
		super();
		this.productId = productId;
		this.sellerId = sellerId;
		this.productName = productName;
		this.productDescription = productDescription;
		this.price = price;
		this.discountedPrice = discountedPrice;
		this.quantityInStock = quantityInStock;
		this.categoryId = categoryId;
		this.imageUrl = imageUrl;
	}
	public Product() {
		// TODO Auto-generated constructor stub
	}
    
    

}
