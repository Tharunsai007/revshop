package com.Entity;

public class User_Registration {
	
	private int userId;
	private String email;
	private String password;
	private String role;
	private String firstName;
	private String lastName;
	private String businessDetails;
    
    public User_Registration()
    {
    	
    }
    
	public User_Registration(int userId, String email, String password, String role, String firstName, String lastName,
			String businessDetails) {
		super();
		this.userId = userId;
		this.email = email;
		this.password = password;
		this.role = role;
		this.firstName = firstName;
		this.lastName = lastName;
		this.businessDetails = businessDetails;
	}
	
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getBusinessDetails() {
		return businessDetails;
	}
	public void setBusinessDetails(String businessDetails) {
		this.businessDetails = businessDetails;
	}
    

}
