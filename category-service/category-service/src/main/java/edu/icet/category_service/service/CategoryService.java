package edu.icet.category_service.service;

import edu.icet.category_service.model.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {

    // Get all categories for the current user
    List<CategoryDTO> getAll();

    void addCategory(CategoryDTO categoryDTO);
    void updateCategory(CategoryDTO categoryDTO);
    boolean deleteCategory(Long id);
    CategoryDTO searchById(Long id);

    // Additional methods for search functionality
    List<CategoryDTO> searchByName(String name);
}