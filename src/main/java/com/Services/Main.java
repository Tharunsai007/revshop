package com.Services;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
		Registration r1=new Registration();
		LoginValidationService l1=new LoginValidationService();
		Scanner sc=new Scanner(System.in);
		System.out.println("Type 1 for login AND Type 2 for registration");
		int login=sc.nextInt();
		if(login==2)
		{
			r1.Register();
		}
		else
		{
			l1.validate();
		}

	}

}
