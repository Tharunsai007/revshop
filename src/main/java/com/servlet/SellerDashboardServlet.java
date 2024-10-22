package com.servlet;

import com.DAO.productDAOImp1;
import com.Entity.Product;
import Dbconnection.DBConnectionManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/sellerDashboard")
public class SellerDashboardServlet extends HttpServlet {

    private Connection connection;

    @Override
    public void init() throws ServletException {
        try {
            connection = DBConnectionManager.getConnection();
        } catch (ClassNotFoundException | SQLException e) {
            throw new ServletException("Unable to initialize DB connection", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer sellerId = (Integer) session.getAttribute("sellerId");

        if (sellerId == null) {
            response.sendRedirect("sellerlogin.jsp"); // Redirect to login if no sellerId is found
            return;
        }

        // Retrieve products by sellerId
        List<Product> products = null;
        try {
            productDAOImp1 productDAO = new productDAOImp1();
            products = productDAO.getProductsBySellerId(sellerId);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            request.setAttribute("error", "Unable to retrieve products.");
        }

        // Set products as a request attribute to pass them to JSP
        request.setAttribute("products", products);
        request.getRequestDispatcher("sellerDashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            int productId = Integer.parseInt(request.getParameter("productId"));
            deleteProduct(productId);
        } else if ("update".equals(action)) {
            int productId = Integer.parseInt(request.getParameter("productId"));
            String productName = request.getParameter("productName");
            String productDescription = request.getParameter("productDescription");
            double price = Double.parseDouble(request.getParameter("price"));
            int quantityInStock = Integer.parseInt(request.getParameter("quantityInStock"));
            int categoryId = Integer.parseInt(request.getParameter("categoryId"));

            updateProduct(productId, productName, productDescription, price, quantityInStock, categoryId);
        }

        response.sendRedirect("sellerDashboard");
    }

    private void deleteProduct(int productId) {
        try {
            productDAOImp1 productDAO = new productDAOImp1();
            productDAO.deleteProduct(productId);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void updateProduct(int productId, String productName, String productDescription, double price, int quantityInStock, int categoryId) {
        try {
            productDAOImp1 productDAO = new productDAOImp1();
            productDAO.updateProduct(productId, productName, productDescription, price, quantityInStock, categoryId);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
