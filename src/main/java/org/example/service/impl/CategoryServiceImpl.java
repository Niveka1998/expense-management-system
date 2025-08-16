package org.example.service.impl;

import org.example.model.dto.CategoryDTO;
import org.example.model.entity.CategoryEntity;
import org.example.repository.CategoryRepository;
import org.example.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    ModelMapper mapper = new ModelMapper();

    @Override
    public List<CategoryDTO> getAll() {
        List<CategoryEntity> categoryEntities = categoryRepository.findAll();
        List<CategoryDTO> categoryDTOS = new ArrayList<>();
        for(CategoryEntity categoryEntity : categoryEntities){
            categoryDTOS.add(mapper.map(categoryEntity, CategoryDTO.class));
        }
        return categoryDTOS;
    }

    @Override
    public void addCategory(CategoryDTO categoryDTO) {
        CategoryEntity categoryEntity = mapper.map(categoryDTO, CategoryEntity.class);
        categoryRepository.save(categoryEntity);
    }

    @Override
    public void updateCategory(CategoryDTO categoryDTO) {
        CategoryEntity categoryEntity = new CategoryEntity(categoryDTO.getCategoryId(), categoryDTO.getName(), categoryDTO.getDescription());
        categoryRepository.save(categoryEntity);
    }

    @Override
    public boolean deleteCategory(Long id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public CategoryDTO searchById(Long id) {
        return categoryRepository.findById(id)
                .map(categoryEntity -> mapper.map(categoryEntity, CategoryDTO.class))
                .orElse(null);
    }
}
