package com.Services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.DAO.DBConnectionManager;
import com.DAO.productDAOImp1;
import com.Entity.Product;

public class ProductService {

	private final productDAOImp1 productDAO;

    public ProductService(Connection connection) throws ClassNotFoundException, SQLException {
        this.productDAO = new productDAOImp1();
    }

    // Method to add a new product
    /*public void addProduct(Product product) {
        productDAO.addProduct(product);
		System.out.println("Product added successfully: " + product.getProductName());
    }*/

    // Method to get a product by its ID
    public Product getProductById(int productId) {
        return productDAO.getProductById(productId);
    }

    // Method to get all products
    public List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }

    // Method to update a product
    public void updateProduct(Product product) throws SQLException {
       // productDAO.updateProduct(product);
		System.out.println("Product updated successfully: " + product.getProductName());
    }

    // Method to delete a product by its ID
    public void deleteProduct(int productId) throws SQLException {
        productDAO.deleteProduct(productId);
		System.out.println("Product deleted successfully: ID " + productId);
    }
}
