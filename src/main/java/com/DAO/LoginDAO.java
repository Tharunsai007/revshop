package com.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.Entity.User_Registration;

public class LoginDAO {
	
	public User_Registration validateBuyerLogin(String email, String password) throws SQLException, ClassNotFoundException {
		User_Registration user = null;
		Class.forName("com.mysql.cj.jdbc.Driver"); 
		Connection connection =DriverManager.getConnection("jdbc:mysql://localhost:3306/rev_shop","root","Tsai88868@");
        String query = "SELECT * FROM users WHERE email = ? AND password = ? AND role = 'buyer'";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                user = new User_Registration(rs.getInt("user_id"), rs.getString("email"), rs.getString("password"),
                        rs.getString("role"), rs.getString("first_name"), rs.getString("last_name"),
                        rs.getString("business_details"));
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
	public User_Registration validateSellerLogin(String email, String password) throws SQLException, ClassNotFoundException {
		User_Registration user = null;
		Class.forName("com.mysql.cj.jdbc.Driver"); 
		Connection connection =DriverManager.getConnection("jdbc:mysql://localhost:3306/rev_shop","root","Tsai88868@");
        String query = "SELECT * FROM users WHERE email = ? AND password = ? AND role = 'seller'";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                user = new User_Registration(rs.getInt("user_id"), rs.getString("email"), rs.getString("password"),
                        rs.getString("role"), rs.getString("first_name"), rs.getString("last_name"),
                        rs.getString("business_details"));
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

}
