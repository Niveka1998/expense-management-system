package edu.icet.expense_service.service;

import edu.icet.expense_service.model.dto.ExpenseDTO;

import java.util.List;

public interface ExpenseService {
    List<ExpenseDTO> getAll();
    ExpenseDTO addExpense(ExpenseDTO expenseDTO);
    void updateExpense(ExpenseDTO expenseDTO);
    boolean deleteExpense(Long id);
    ExpenseDTO searchById(Long id);
}
