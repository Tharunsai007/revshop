package com.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.Entity.User_Registration;

public class UserDAOImplementation1 {
	    
		public void addUser(User_Registration user) throws ClassNotFoundException, SQLException
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection connection =DriverManager.getConnection("jdbc:mysql://localhost:3306/rev_shop","root","Tsai88868@");
			String query = "INSERT INTO users (user_id,email, password, role, first_name, last_name, business_details) VALUES (?, ?, ?, ?, ?, ?,?)";
			try 
			{
				PreparedStatement pstmt = connection.prepareStatement(query);
				pstmt.setInt(1, user.getUserId());
	            pstmt.setString(2, user.getEmail());
	            pstmt.setString(3, user.getPassword());
	            pstmt.setString(4, user.getRole());
	            pstmt.setString(5, user.getFirstName());
	            pstmt.setString(6, user.getLastName());
	            pstmt.setString(7, user.getBusinessDetails());
	            pstmt.executeUpdate();
	            connection.close();
	         } 
			catch (SQLException e) 
			{
	            e.printStackTrace();
	        }
		}
		
		public User_Registration getUserById(int userId) throws SQLException
		{
			User_Registration user = null;
			Connection connection =DriverManager.getConnection("jdbc:mysql://localhost:3306/rev_shop","root","Tsai88868@");
	        String query = "SELECT * FROM users WHERE user_id = ?";
	        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	            pstmt.setInt(1, userId);
	            ResultSet rs = pstmt.executeQuery();
	            if (rs.next()) {
	                user = new User_Registration(rs.getInt("user_id"), rs.getString("email"), rs.getString("password"),
	                        rs.getString("role"), rs.getString("first_name"), rs.getString("last_name"),
	                        rs.getString("business_details"));
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return user;
		}
		
		public User_Registration getUserByEmail(String email) throws SQLException, ClassNotFoundException
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			User_Registration user = null;
			Connection connection =DriverManager.getConnection("jdbc:mysql://localhost:3306/rev_shop","root","Tsai88868@");
	        String query = "SELECT * FROM users WHERE email = ?";
	        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	            pstmt.setString(1, email);
	            ResultSet rs = pstmt.executeQuery();
	            if (rs.next()) {
	                user = new User_Registration(rs.getInt("user_id"), rs.getString("email"), rs.getString("password"),
	                        rs.getString("role"), rs.getString("first_name"), rs.getString("last_name"),
	                        rs.getString("business_details"));
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return user;
		}
		
		
		public List<User_Registration> getAllUsers() throws SQLException {
	        List<User_Registration> users = new ArrayList<>();
	        Connection connection =DriverManager.getConnection("jdbc:mysql://localhost:3306/rev_shop","root","Tsai88868@");
	        String query = "SELECT * FROM users";
	        try
	        {
	        	Statement stmt = connection.createStatement();
	             ResultSet rs = stmt.executeQuery(query);
	            while (rs.next()) 
	            {
	            	User_Registration user = new User_Registration(rs.getInt("user_id"), rs.getString("email"), rs.getString("password"),
	                        rs.getString("role"), rs.getString("first_name"), rs.getString("last_name"),
	                        rs.getString("business_details"));
	                users.add(user);
	            }
	        } 
	        catch (SQLException e)
	        {
	            e.printStackTrace();
	        }
	        return users;
	    }
		
		public void updateUser(User_Registration user) throws SQLException {
	        String query = "UPDATE users SET email = ?, password = ?, role = ?, first_name = ?, last_name = ?, business_details = ? WHERE user_id = ?";
	        Connection connection =DriverManager.getConnection("jdbc:mysql://localhost:3306/rev_shop","root","Tsai88868@");
	        try 
	        {
	        PreparedStatement pstmt = connection.prepareStatement(query);
	            pstmt.setString(1, user.getEmail());
	            pstmt.setString(2, user.getPassword());
	            pstmt.setString(3, user.getRole());
	            pstmt.setString(4, user.getFirstName());
	            pstmt.setString(5, user.getLastName());
	            pstmt.setString(6, user.getBusinessDetails());
	            pstmt.setInt(7, user.getUserId());
	            pstmt.executeUpdate();
	        } 
	        catch (SQLException e) 
	        {
	            e.printStackTrace();
	        }
		}
	        
	        public void DeleteUser(int userId) throws SQLException
	        {
	        	Connection connection1 =DriverManager.getConnection("jdbc:mysql://localhost:3306/rev_shop","root","Tsai88868@");
	            String query1 = "DELETE FROM users WHERE user_id = ?";
	            try 
	            {
	            	PreparedStatement pstmt = connection1.prepareStatement(query1);
	                pstmt.setInt(1, userId);
	                pstmt.executeUpdate();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
		
	}


