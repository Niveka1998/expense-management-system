package edu.icet.budget_service.service;

import edu.icet.budget_service.model.dto.BudgetDTO;

import java.util.List;

public interface BudgetService {
    List<BudgetDTO> getAll();
    void addBudget(BudgetDTO budgetDTO);
    void updateBudget(BudgetDTO budgetDTO);
    boolean deleteBudget(Long id);
    BudgetDTO searchById(Long id);
}
