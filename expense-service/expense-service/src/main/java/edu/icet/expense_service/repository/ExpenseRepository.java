package edu.icet.expense_service.repository;

import edu.icet.expense_service.model.entity.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long> {

    // Find all expenses for a specific user
    List<ExpenseEntity> findByUserId(Long userId);

    // Find expense by ID only if it belongs to the user
    Optional<ExpenseEntity> findByExpenseIdAndUserId(Long expenseId, Long userId);

    // Find expenses by budget ID and user ID
    List<ExpenseEntity> findByBudgetIdAndUserId(Long budgetId, Long userId);

    // Find expenses by category ID and user ID
    List<ExpenseEntity> findByCategoryIdAndUserId(Long categoryId, Long userId);

    // Find expenses by date range and user ID
    List<ExpenseEntity> findByDateBetweenAndUserId(LocalDate startDate, LocalDate endDate, Long userId);

    // Check if expense exists and belongs to user
    boolean existsByExpenseIdAndUserId(Long expenseId, Long userId);

    // Delete expense only if it belongs to user
    void deleteByExpenseIdAndUserId(Long expenseId, Long userId);
}