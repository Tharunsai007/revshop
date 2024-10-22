package com.Services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.DAO.CartDAO;
import com.DAO.DBConnectionManager;
import com.Entity.cartdetails;

public class cartService {

	private final CartDAO cartDAO;

    public cartService(Connection connection) {
        this.cartDAO = new CartDAO(connection);
    }

    // Method to add an item to the cart
    public void addItemToCart(cartdetails cart) {
        try {
            cartDAO.addToCart(cart);
            System.out.println("Item added to cart successfully: Product ID " + cart.getProductId());
        } catch (Exception e) {
            System.err.println("Error adding item to cart: " + e.getMessage());
        }
    }

    // Method to get a cart item by its ID
    public cartdetails getCartItemById(int cartId) throws SQLException {
        cartdetails cartItem = null;
        cartItem = cartDAO.getCartItemById(cartId);
		if (cartItem != null) {
		    System.out.println("Cart item retrieved: " + cartItem);
		} else {
		    System.out.println("Cart item not found with ID: " + cartId);
		}
        return cartItem;
    }

    // Method to get all items in the cart for a user
    public List<cartdetails> getCartItems(int buyerId) throws SQLException {
        List<cartdetails> cartItems = null;
        cartItems = cartDAO.getCartItemsByBuyerId(buyerId);
		if (cartItems.isEmpty()) {
		    System.out.println("No items found in cart for buyer ID: " + buyerId);
		} else {
		    System.out.println("Cart items retrieved successfully for buyer ID: " + buyerId);
		}
        return cartItems;
    }

    // Method to update an item in the cart
    public void updateCartItem(cartdetails cart) {
        try {
            cartDAO.updateCartItem(cart);
            System.out.println("Cart item updated successfully: Cart ID " + cart.getCartId());
        } catch (Exception e) {
            System.err.println("Error updating cart item: " + e.getMessage());
        }
    }

    // Method to remove an item from the cart
    public void removeItemFromCart(int cartId) {
        try {
            cartDAO.removeFromCart(cartId);
            System.out.println("Cart item removed successfully: Cart ID " + cartId);
        } catch (Exception e) {
            System.err.println("Error removing cart item: " + e.getMessage());
        }
    }

    // Method to clear the cart for a user
    public void clearCart(int buyerId) {
        try {
            cartDAO.clearCartByBuyerId(buyerId);
            System.out.println("Cart cleared successfully for buyer ID: " + buyerId);
        } catch (Exception e) {
            System.err.println("Error clearing cart: " + e.getMessage());
        }
    }
}
