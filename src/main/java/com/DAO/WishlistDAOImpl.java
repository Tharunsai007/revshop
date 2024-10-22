package com.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.Entity.Product;

interface WishlistDAO {
    void addToWishlist(int productId, int userId);
    public void removeFromWishlist(Integer userId, int productId) throws SQLException;
    public List<Product> getWishlistByUserId(int userId) throws ClassNotFoundException;
}

public class WishlistDAOImpl implements WishlistDAO {

    private Connection connection;

    public WishlistDAOImpl() throws SQLException, ClassNotFoundException {
        // Initialize the connection using DBConnectionManager
        this.connection = DBConnectionManager.getConnection();
        if (this.connection == null) {
            System.out.println("Failed to establish a database connection.");
        }
    }

    @Override
    public void addToWishlist(int productId, int userId) {
        String sql = "INSERT INTO wishlist (user_id, product_id) VALUES (?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, productId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Optionally, you can throw a custom exception or handle it as per your application's requirement
        }
    }

    @Override

    public void removeFromWishlist(Integer userId, int productId) throws SQLException {
        String sql = "DELETE FROM wishlist WHERE user_id = ? AND product_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setInt(2, productId);
            statement.executeUpdate();
        }
    }

    @Override
    public List<Product> getWishlistByUserId(int userId) throws ClassNotFoundException {
        List<Product> wishlist = new ArrayList<>();
        String query = "SELECT p.product_id, p.product_name, p.product_description, p.price, p.discounted_price, p.quantity_in_stock, p.image_url "
                     + "FROM wishlist w "
                     + "JOIN products p ON w.product_id = p.product_id "
                     + "WHERE w.user_id = ?";
        
        try (Connection con = DBConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setProductName(rs.getString("product_name"));
                product.setProductDescription(rs.getString("product_description"));
                product.setPrice(rs.getDouble("price"));
                product.setDiscountedPrice(rs.getDouble("discounted_price"));
                product.setQuantityInStock(rs.getInt("quantity_in_stock"));
                product.setImageUrl(rs.getBytes("image_url"));
                
                wishlist.add(product);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return wishlist;
    }

}
