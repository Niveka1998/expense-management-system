package edu.icet.category_service.controller;

import edu.icet.category_service.model.dto.CategoryDTO;
import edu.icet.category_service.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5174", "http://localhost:5173"})
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping("/get-all-categories")
    public List<CategoryDTO> getAllCategories(){
        return categoryService.getAll(); // This now returns only current user's categories
    }

    @PostMapping("/add-new-category")
    public CategoryDTO addCategory(@RequestBody CategoryDTO categoryDTO){
        categoryService.addCategory(categoryDTO);
        return categoryDTO; // Return the created category with ID
    }

    @PutMapping("/update-category/{id}")
    public void updateCategory(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO){
        categoryDTO.setCategoryId(id);
        categoryService.updateCategory(categoryDTO);
    }

    @DeleteMapping("/{id}")
    public boolean deleteCategory(@PathVariable Long id){
        return categoryService.deleteCategory(id);
    }

    @GetMapping("/{id}")
    public CategoryDTO getCategoryById(@PathVariable Long id){
        return categoryService.searchById(id);
    }

    // Add search endpoint
    @GetMapping("/search")
    public List<CategoryDTO> searchCategories(@RequestParam String name) {
        return categoryService.searchByName(name);
    }
}