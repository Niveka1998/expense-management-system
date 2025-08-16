package org.example.controller;

import org.example.model.dto.CategoryDTO;
import org.example.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping("get-all-categories")
    public List<CategoryDTO> getAllCategories(){
        return categoryService.getAll();
    }

    @PostMapping("/add-new-category")
    public void addCategory(@RequestBody CategoryDTO categoryDTO){
        categoryService.addCategory(categoryDTO);
    }

    @PutMapping("update-category")
    public void updateCategory(@RequestBody CategoryDTO categoryDTO){
        categoryService.updateCategory(categoryDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id){
        categoryService.deleteCategory(id);
    }

    @GetMapping("/{id}")
    public CategoryDTO searchCategory(@PathVariable Long id){
        return categoryService.searchById(id);
    }

}
