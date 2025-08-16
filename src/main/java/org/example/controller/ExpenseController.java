package org.example.controller;

import org.example.model.dto.CategoryDTO;
import org.example.model.dto.ExpenseDTO;
import org.example.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expense")
public class ExpenseController {

    @Autowired
    ExpenseService expenseService;

    @GetMapping("get-all-expenses")
    public List<ExpenseDTO> getAllExpenses(){
        return expenseService.getAll();
    }

    @PostMapping("/add-new-expense")
    public void addExpense(@RequestBody ExpenseDTO expenseDTO){
        expenseService.addExpense(expenseDTO);
    }

    @PutMapping("update-expense")
    public void updateExpense(@RequestBody ExpenseDTO expenseDTO){
        expenseService.updateExpense(expenseDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteExpense(@PathVariable Long id){
        expenseService.deleteExpense(id);
    }

    @GetMapping("/{id}")
    public ExpenseDTO searchExpense(@PathVariable Long id){
        return expenseService.searchById(id);
    }
}
