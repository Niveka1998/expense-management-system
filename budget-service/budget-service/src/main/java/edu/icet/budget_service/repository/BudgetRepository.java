package edu.icet.budget_service.repository;

import edu.icet.budget_service.model.entity.BudgetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<BudgetEntity, Long> {

    // Find all budgets for a specific user
    List<BudgetEntity> findByUserId(Long userId);

    // Find a specific budget by ID only if it belongs to the user
    Optional<BudgetEntity> findByBudgetIdAndUserId(Long budgetId, Long userId);

    // Check if a budget exists and belongs to a specific user
    boolean existsByBudgetIdAndUserId(Long budgetId, Long userId);

    // Delete a budget only if it belongs to the user
    void deleteByBudgetIdAndUserId(Long budgetId, Long userId);
}