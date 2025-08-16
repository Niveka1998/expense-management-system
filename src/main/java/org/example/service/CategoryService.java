package org.example.service;

import org.example.model.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> getAll();
    void addCategory(CategoryDTO categoryDTO);
    void updateCategory(CategoryDTO categoryDTO);
    boolean deleteCategory(Long id);
    CategoryDTO searchById(Long id);
}
