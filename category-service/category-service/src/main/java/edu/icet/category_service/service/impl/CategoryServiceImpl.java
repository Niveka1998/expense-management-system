package edu.icet.category_service.service.impl;

import edu.icet.category_service.model.dto.CategoryDTO;
import edu.icet.category_service.model.entity.CategoryEntity;
import edu.icet.category_service.repository.CategoryRepository;
import edu.icet.category_service.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    //ModelMapper mapper = new ModelMapper();

    @Override
    public List<CategoryDTO> getAll() {
        List<CategoryEntity> categoryEntities = categoryRepository.findAll();
        List<CategoryDTO> categoryDTOS = new ArrayList<>();
        for(CategoryEntity categoryEntity : categoryEntities){
            CategoryDTO categoryDTO = new CategoryDTO();
            categoryDTO.setCategoryId(categoryEntity.getCategoryId());
            categoryDTO.setName(categoryEntity.getName());
            categoryDTO.setDescription(categoryEntity.getDescription()); //getting the fields from saved entities
            categoryDTOS.add(categoryDTO);
        }
        return categoryDTOS;
    }

    @Override
    public void addCategory(CategoryDTO categoryDTO) {
        CategoryEntity categoryEntity = new CategoryEntity();
        //categoryEntity.setCategoryId(categoryDTO.getCategoryId());
        categoryEntity.setName(categoryDTO.getName());
        categoryEntity.setDescription(categoryDTO.getDescription());
        categoryRepository.save(categoryEntity);
    }

    @Override
    public void updateCategory(CategoryDTO categoryDTO) {
        Optional<CategoryEntity> existingEntity = categoryRepository.findById(categoryDTO.getCategoryId());

        if (existingEntity.isPresent()) {
            CategoryEntity categoryEntity = existingEntity.get();
            categoryEntity.setName(categoryDTO.getName());
            categoryEntity.setDescription(categoryDTO.getDescription());
            categoryRepository.save(categoryEntity);
        }
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
        Optional<CategoryEntity> entityOptional = categoryRepository.findById(id);

        if (entityOptional.isPresent()) {
            CategoryEntity entity = entityOptional.get();
            CategoryDTO dto = new CategoryDTO();
            dto.setCategoryId(entity.getCategoryId());
            dto.setName(entity.getName());
            dto.setDescription(entity.getDescription());
            return dto;
        }

        return null;
    }
}
