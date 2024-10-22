package com.Services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.DAO.CategoryDAOImpl;
import com.DAO.DBConnectionManager;
import com.Entity.Category;

public class CategoryService {

	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		
		Connection connection = DBConnectionManager.getConnection();
        CategoryDAOImpl categoryDAO = new CategoryDAOImpl(connection);

        // Adding a new category
        Category newCategory = new Category(0, "groceries");
        categoryDAO.addCategory(newCategory);
        System.out.println("Category added successfully!");

        // Fetching a category by ID
        Category fetchedCategory = categoryDAO.getCategoryById(1);
        System.out.println("Category: " + fetchedCategory.getCategoryName());

        // Updating a category
       fetchedCategory.setCategoryName("toys");
        categoryDAO.updateCategory(fetchedCategory);
        System.out.println("Category updated successfully!");

        // Fetching all categories
        List<Category> allCategories = categoryDAO.getAllCategories();
        for (Category category : allCategories) {
            System.out.println(category.getCategoryName());
        }

        // Deleting a category
       // categoryDAO.deleteCategory(2);
       // System.out.println("Category deleted successfully!");

        DBConnectionManager.closeConnection(connection);

	}

}
