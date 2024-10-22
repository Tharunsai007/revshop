package com.DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.Entity.cartdetails;

interface cartinterface {
    void addToCart(cartdetails cart) throws SQLException;
    cartdetails getCartItemById(int cartId);
    public cartdetails getCartItem(int userId, int productId) throws SQLException;
    List<cartdetails> getCartItemsByBuyerId(int buyerId);
    void updateCartItem(cartdetails cart);
    public void updateCartQuantity(int cartId, int newQuantity) throws SQLException;
    public boolean removeFromCart(int cartId);
    void clearCartByBuyerId(int buyerId);
}

public class CartDAO implements cartinterface{
	private Connection connection;

    public CartDAO(Connection connection) {
        this.connection = connection;
    }

    public void addToCart(cartdetails cartItem) throws SQLException {
        String selectQuery = "SELECT quantity FROM cart WHERE user_id = ? AND product_id = ?";
        String updateQuery = "UPDATE cart SET quantity = ? WHERE user_id = ? AND product_id = ?";
        String insertQuery = "INSERT INTO cart (user_id, product_id, quantity) VALUES (?, ?, ?)";

        try (PreparedStatement selectStmt = connection.prepareStatement(selectQuery)) {
            // Check if product exists in the cart
            selectStmt.setInt(1, cartItem.getBuyerId());
            selectStmt.setInt(2, cartItem.getProductId());
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                // Product exists, update quantity
                int existingQuantity = rs.getInt("quantity");
                int newQuantity = existingQuantity + cartItem.getQuantity();
                System.out.println("Updating product "+cartItem.getProductId()+ "with new quantity: " + newQuantity);

                try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                    updateStmt.setInt(1, newQuantity);
                    updateStmt.setInt(2, cartItem.getBuyerId());
                    updateStmt.setInt(3, cartItem.getProductId());
                    updateStmt.executeUpdate();
                }
            } else {
                // Product doesn't exist, insert new cart item
                System.out.println("Inserting new product to cart");
                try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                    insertStmt.setInt(1, cartItem.getBuyerId());
                    insertStmt.setInt(2, cartItem.getProductId());
                    insertStmt.setInt(3, cartItem.getQuantity());
                    insertStmt.executeUpdate();
                }
            }
        }
    }



	@Override
	public cartdetails getCartItemById(int cartId) {
		
		cartdetails cart=null;
		String query="select * from cart where cart_id=?";
		try(PreparedStatement pstmt=connection.prepareStatement(query))
		{
			pstmt.setInt(1,cartId);
			ResultSet rs=pstmt.executeQuery();
			if(rs.next())
			{
				 cart = new cartdetails(rs.getInt("cart_id"),rs.getInt("user_id"),
						 rs.getInt("product_id"), rs.getInt("quantity"), null);
			}
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return cart;
	}
	
	 public cartdetails getCartItem(int userId, int productId) throws SQLException {
	        String query = "SELECT * FROM cart WHERE user_id = ? AND product_id = ?";
	        cartdetails cartItem = null;

	        try (PreparedStatement ps = connection.prepareStatement(query)) {
	            ps.setInt(1, userId);
	            ps.setInt(2, productId);

	            try (ResultSet rs = ps.executeQuery()) {
	                if (rs.next()) {
	                    cartItem = new cartdetails(
	                        rs.getInt("cart_id"),
	                        rs.getInt("product_id"),
	                        rs.getInt("user_id"),
	                        rs.getInt("quantity"),
	                        null
	                    );
	                }
	            }
	        }

	        return cartItem;
	    }

	@Override
	public List<cartdetails> getCartItemsByBuyerId(int buyerId) {
		List<cartdetails> list=new ArrayList<cartdetails>();
		String query="select * from cart where user_id=?";
		try(PreparedStatement pstmt=connection.prepareStatement(query))
		{
			cartdetails cart=null;
			pstmt.setInt(1,buyerId);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next())
			{
				cart = new cartdetails(rs.getInt("cart_id"),rs.getInt("user_id"),
						 rs.getInt("product_id"), rs.getInt("quantity"),null);
				list.add(cart);
			}
			
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public void updateCartItem(cartdetails cart) {
		String query="update cart set quantity=? where cart_id=? ";
		try(PreparedStatement pstmt=connection.prepareStatement(query))
		{
			pstmt.setInt(1,cart.getQuantity());
			pstmt.setInt(2, cart.getCartId());
			pstmt.executeUpdate();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
	}
	
	public void updateCartQuantity(int cartId, int newQuantity) throws SQLException {
        String query = "UPDATE cart SET quantity = ? WHERE cart_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, newQuantity);
            ps.setInt(2, cartId);
            ps.executeUpdate();
        }
    }


	@Override
	public void clearCartByBuyerId(int userId) {
		String query = "DELETE FROM cart WHERE user_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
	}

	public boolean removeFromCart(int cartId) {
		String query = "DELETE FROM cart WHERE cart_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, cartId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
		return false;
	}


}
