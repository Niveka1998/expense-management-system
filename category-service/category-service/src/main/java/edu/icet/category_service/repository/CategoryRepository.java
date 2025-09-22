package edu.icet.category_service.repository;

import edu.icet.category_service.model.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    // Find all categories for a specific user
    List<CategoryEntity> findByUserId(Long userId);

    // Find category by ID only if it belongs to the user
    Optional<CategoryEntity> findByCategoryIdAndUserId(Long categoryId, Long userId);

    // Check if category exists and belongs to user
    boolean existsByCategoryIdAndUserId(Long categoryId, Long userId);

    // Delete category only if it belongs to user
    void deleteByCategoryIdAndUserId(Long categoryId, Long userId);

    // Find categories by name for a specific user (for search functionality)
    List<CategoryEntity> findByNameContainingAndUserId(String name, Long userId);
}