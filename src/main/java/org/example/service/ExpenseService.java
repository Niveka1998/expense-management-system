package org.example.service;

import org.example.model.dto.ExpenseDTO;

import java.util.List;

public interface ExpenseService {
    List<ExpenseDTO> getAll();
    void addExpense(ExpenseDTO expenseDTO);
    void updateExpense(ExpenseDTO expenseDTO);
    boolean deleteExpense(Long id);
    ExpenseDTO searchById(Long id);
}
