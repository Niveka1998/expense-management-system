package edu.icet.budget_service.service;

import edu.icet.budget_service.model.dto.BudgetDTO;

import java.util.List;

public interface BudgetService {

    // Get all budgets for the current user
    List<BudgetDTO> getAllForCurrentUser();

    // Get all budgets (admin only - or remove this if not needed)
    List<BudgetDTO> getAll();
    List<BudgetDTO> getAllForUser(Long userId);

    void addBudget(BudgetDTO budgetDTO);
    void updateBudget(BudgetDTO budgetDTO);
    boolean deleteBudget(Long id);
    BudgetDTO searchById(Long id);

    // New method to get budget only if it belongs to current user
    BudgetDTO getBudgetForCurrentUser(Long id);
}