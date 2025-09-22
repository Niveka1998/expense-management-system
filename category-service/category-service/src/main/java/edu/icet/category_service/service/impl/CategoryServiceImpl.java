package edu.icet.category_service.service.impl;

import edu.icet.category_service.model.dto.CategoryDTO;
import edu.icet.category_service.model.entity.CategoryEntity;
import edu.icet.category_service.repository.CategoryRepository;
import edu.icet.category_service.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    // Helper method to get current user ID
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof edu.icet.category_service.model.dto.UserDTO) {
            edu.icet.category_service.model.dto.UserDTO userDTO = (edu.icet.category_service.model.dto.UserDTO) authentication.getPrincipal();
            return userDTO.getUserId();
        }
        throw new RuntimeException("User not authenticated");
    }

    // Helper method to get current user info
    private edu.icet.category_service.model.dto.UserDTO getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof edu.icet.category_service.model.dto.UserDTO) {
            return (edu.icet.category_service.model.dto.UserDTO) authentication.getPrincipal();
        }
        throw new RuntimeException("User not authenticated");
    }

    @Override
    public List<CategoryDTO> getAll() {
        Long userId = getCurrentUserId();
        List<CategoryEntity> categoryEntities = categoryRepository.findByUserId(userId);
        List<CategoryDTO> categoryDTOS = new ArrayList<>();

        for(CategoryEntity categoryEntity : categoryEntities){
            CategoryDTO categoryDTO = convertToDTO(categoryEntity);
            categoryDTOS.add(categoryDTO);
        }

        System.out.println("Returning " + categoryDTOS.size() + " categories for user ID: " + userId);
        return categoryDTOS;
    }

    @Override
    public void addCategory(CategoryDTO categoryDTO) {
        // Get current user info
        edu.icet.category_service.model.dto.UserDTO currentUser = getCurrentUser();

        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName(categoryDTO.getName());
        categoryEntity.setDescription(categoryDTO.getDescription());

        // Set user information
        categoryEntity.setUserId(currentUser.getUserId());
        categoryEntity.setUsername(currentUser.getUsername());

        categoryRepository.save(categoryEntity);

        // Set the generated ID back to DTO
        categoryDTO.setCategoryId(categoryEntity.getCategoryId());
    }

    @Override
    public void updateCategory(CategoryDTO categoryDTO) {
        Long userId = getCurrentUserId();

        Optional<CategoryEntity> existingEntity = categoryRepository.findByCategoryIdAndUserId(categoryDTO.getCategoryId(), userId);

        if (existingEntity.isPresent()) {
            CategoryEntity categoryEntity = existingEntity.get();
            categoryEntity.setName(categoryDTO.getName());
            categoryEntity.setDescription(categoryDTO.getDescription());

            categoryRepository.save(categoryEntity);
        } else {
            throw new RuntimeException("Category not found or access denied");
        }
    }

    @Override
    public boolean deleteCategory(Long id) {
        Long userId = getCurrentUserId();
        if (categoryRepository.existsByCategoryIdAndUserId(id, userId)) {
            categoryRepository.deleteByCategoryIdAndUserId(id, userId);
            return true;
        }
        return false;
    }

    @Override
    public CategoryDTO searchById(Long id) {
        Long userId = getCurrentUserId();
        Optional<CategoryEntity> entityOptional = categoryRepository.findByCategoryIdAndUserId(id, userId);

        if (entityOptional.isPresent()) {
            CategoryEntity entity = entityOptional.get();
            return convertToDTO(entity);
        }

        return null;
    }

    // Add this method to your CategoryServiceImpl
    @Override
    public List<CategoryDTO> searchByName(String name) {
        Long userId = getCurrentUserId();
        List<CategoryEntity> categoryEntities = categoryRepository.findByNameContainingAndUserId(name, userId);
        List<CategoryDTO> categoryDTOS = new ArrayList<>();

        for(CategoryEntity categoryEntity : categoryEntities){
            categoryDTOS.add(convertToDTO(categoryEntity));
        }

        return categoryDTOS;
    }
    // Helper method to convert entity to DTO
    private CategoryDTO convertToDTO(CategoryEntity categoryEntity) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryId(categoryEntity.getCategoryId());
        categoryDTO.setName(categoryEntity.getName());
        categoryDTO.setDescription(categoryEntity.getDescription());
        categoryDTO.setUserId(categoryEntity.getUserId());
        categoryDTO.setUsername(categoryEntity.getUsername());
        return categoryDTO;
    }
}