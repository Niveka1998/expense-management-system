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
        return categoryService.getAll();
    }

    @PostMapping("/add-new-category")
    public void addCategory(@RequestBody CategoryDTO categoryDTO){
        categoryService.addCategory(categoryDTO);
    }

    @PutMapping("/update-category/{id}")
    public void updateCategory(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO){
        categoryDTO.setCategoryId(id);
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
