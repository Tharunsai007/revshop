package com.Services;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.DAO.UserDAOImplementation1;
import com.Entity.User_Registration;

public class Registration {
    public void Register() throws ClassNotFoundException, SQLException {
        
    	UserDAOImplementation1 o1=new UserDAOImplementation1();
    	Scanner sc=new Scanner(System.in);
    	String email=sc.next();
    	String password=sc.next();
    	String role=sc.next();
    	String f_name=sc.next();
    	String l_name=sc.next();
    	String details=sc.next();
    	
    	User_Registration newuser = new User_Registration(0,email,password,role,f_name,l_name,details);
    	o1.addUser(newuser);
    	
    	User_Registration user=o1.getUserById(1);
    	if(user!=null)
    	{
    		System.out.println(user.getEmail());
    	}
    	
    	user.setFirstName(role);
    	o1.updateUser(user);
    	
    	List<User_Registration> list=o1.getAllUsers();
    	for(User_Registration user1:list)
    	{
    		System.out.println(user1.getFirstName());
    	}
    	
    }
}
