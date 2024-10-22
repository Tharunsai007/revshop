package com.Services;

import java.sql.SQLException;

import com.DAO.LoginDAO;
import com.Entity.User_Registration;

public class LoginValidationService {

	public void validate() throws ClassNotFoundException {
		
		try
		{
			LoginDAO o1 = new LoginDAO();
            String email = "tsai@gmail.com";
            String password = "tsai";

            User_Registration isLoginSuccessful = o1.validateBuyerLogin(email, password);

            if (isLoginSuccessful!=null) {
                System.out.println("Login successful!");
            } else {
                System.out.println("Invalid email or password.");
            }
		}
		catch (SQLException e) 
		{
            e.printStackTrace();
        }

	}

}
