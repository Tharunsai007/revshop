package com.servlet;

import com.Entity.Product;
import com.DAO.WishlistDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/WishlistServlet")
public class WishlistServlet extends HttpServlet {

    private WishlistDAOImpl wishlistService;

    @Override
    public void init() throws ServletException {
        try {
			wishlistService = new WishlistDAOImpl();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Assuming there's a session with the user ID
        Integer userId = (Integer) request.getSession().getAttribute("userId");

        if (userId != null) {
            // Get wishlist products for the logged-in user
            List<Product> wishlistProducts = null;
			try {
				wishlistProducts = wishlistService.getWishlistByUserId(userId);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            // Set the wishlist products as a request attribute and forward to JSP
            request.setAttribute("wishlistProducts", wishlistProducts);
            request.getRequestDispatcher("wishlist.jsp").forward(request, response);
        } else {
            // Redirect to login page if user is not logged in
            response.sendRedirect("login.jsp");
        }
    }
}

