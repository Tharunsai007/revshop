package com.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.DAO.productDAOImp1;
import com.Entity.Product;

@WebServlet("/displayProducts")
public class DisplayProductsServlet extends HttpServlet {
    private productDAOImp1 productDAO;

    @Override
    public void init() throws ServletException {
        try {
            productDAO = new productDAOImp1();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Fetch all products from the DAO
        List<Product> products = productDAO.getAllProducts();
        
        // Set the products list as an attribute to the request
        request.setAttribute("products", products);
        
        // Forward the request to the JSP page for rendering the product list
        request.getRequestDispatcher("productdisplay.jsp").forward(request, response);
    }
}
