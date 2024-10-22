package com.DAO;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.Entity.Product;

interface ProductDAO {
    void addProductWithImage(Product product,InputStream imageInputStream);
    List<Product> getProductsByCategory(int categoryId);
	Product getProductById(int productId);
    List<Product> getProductsBySellerId(int sellerId);
    List<Product> getAllProducts();
    public boolean updateProduct(int productId, String productName, String productDescription, double price, int quantityInStock, int categoryId) throws SQLException;
    void deleteProduct(int productId);
    public List<Product> getProductsBySellerIdAndCategory(int sellerId, int categoryId);
}

public class productDAOImp1 implements ProductDAO {
    private Connection connection;

    public productDAOImp1() throws SQLException, ClassNotFoundException {
        // Initialize the connection using DBConnectionManager
        this.connection = DBConnectionManager.getConnection();
        if (this.connection == null) {
            System.out.println("Failed to establish a database connection.");
        }
    }

    @Override
    public void addProductWithImage(Product product, InputStream imageInputStream) {
        String query = "INSERT INTO products (seller_id, product_name, product_description, price, discounted_price, quantity_in_stock, category_id, image_url) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, product.getSellerId());
            pstmt.setString(2, product.getProductName());
            pstmt.setString(3, product.getProductDescription());
            pstmt.setDouble(4, product.getPrice());
            pstmt.setObject(5, product.getDiscountedPrice());
            pstmt.setInt(6, product.getQuantityInStock());
            pstmt.setInt(7, product.getCategoryId());
            
            // Set the image stream (BLOB) if it is available
            if (imageInputStream != null) {
                pstmt.setBlob(8, imageInputStream);
            } else {
                pstmt.setNull(8, java.sql.Types.BLOB);
            }

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Product getProductById(int productId) {
        Product product = null;
        String query = "SELECT * FROM products WHERE product_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, productId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                product = new Product(rs.getInt("product_id"), rs.getInt("seller_id"),
                        rs.getString("product_name"), rs.getString("product_description"),
                        rs.getDouble("price"), rs.getObject("discounted_price", Double.class),
                        rs.getInt("quantity_in_stock"), rs.getInt("category_id"),
                        rs.getBytes("image_url"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }
    
    @Override
    public List<Product> getProductsByCategory(int categoryId) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products WHERE category_id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, categoryId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Product product = new Product(
                    rs.getInt("product_id"),
                    rs.getInt("seller_id"),
                    rs.getString("product_name"),
                    rs.getString("product_description"),
                    rs.getDouble("price"),
                    rs.getObject("discounted_price", Double.class),
                    rs.getInt("quantity_in_stock"),
                    rs.getInt("category_id"),
                    rs.getBytes("image_url")
                );
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return products;
    }


    @Override
    public List<Product> getProductsBySellerId(int sellerId) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products WHERE seller_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, sellerId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Product product = new Product(rs.getInt("product_id"), rs.getInt("seller_id"),
                        rs.getString("product_name"), rs.getString("product_description"),
                        rs.getDouble("price"), rs.getObject("discounted_price", Double.class),
                        rs.getInt("quantity_in_stock"), rs.getInt("category_id"),
                        rs.getBytes("image_url"));
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products";
        try (PreparedStatement pstmt = connection.prepareStatement(query);
        		ResultSet rs = pstmt.executeQuery();) {
            while (rs.next()) {
                Product product = new Product(rs.getInt("product_id"), rs.getInt("seller_id"),
                        rs.getString("product_name"), rs.getString("product_description"),
                        rs.getDouble("price"), rs.getObject("discounted_price", Double.class),
                        rs.getInt("quantity_in_stock"), rs.getInt("category_id"),
                        rs.getBytes("image_url"));
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public boolean updateProduct(int productId, String productName, String productDescription, double price, int quantityInStock, int categoryId) throws SQLException {
        String updateQuery = "UPDATE products SET product_name = ?, product_description = ?, price = ?, quantity_in_stock = ?, category_id = ? WHERE product_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(updateQuery)) {
            ps.setString(1, productName);
            ps.setString(2, productDescription);
            ps.setDouble(3, price);
            ps.setInt(4, quantityInStock);
            ps.setInt(5, categoryId);
            ps.setInt(6, productId);

            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throw the exception to handle it higher in the call chain
        }
    }


    @Override
    public void deleteProduct(int productId) {
        String query = "DELETE FROM products WHERE product_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, productId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public List<Product> getProductsBySellerIdAndCategory(int sellerId, int categoryId) {
        List<Product> products = new ArrayList<>();
        
        String query = "SELECT * FROM products WHERE seller_id = ? AND category_id = ?";
        
        try (
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, sellerId);
            preparedStatement.setInt(2, categoryId);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Product product = new Product();
                    product.setProductId(resultSet.getInt("product_id"));
                    product.setSellerId(resultSet.getInt("seller_id"));
                    product.setProductName(resultSet.getString("product_name"));
                    product.setProductDescription(resultSet.getString("product_description"));
                    product.setPrice(resultSet.getDouble("price"));
                    product.setDiscountedPrice(resultSet.getDouble("discounted_price"));
                    product.setQuantityInStock(resultSet.getInt("quantity_in_stock"));
                    product.setCategoryId(resultSet.getInt("category_id"));
                    product.setImageUrl(resultSet.getBytes("image_url"));
                    
                    products.add(product);
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return products;
    }

}
