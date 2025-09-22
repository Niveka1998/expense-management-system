package edu.icet.expense_service.controller;

import edu.icet.expense_service.model.dto.ExpenseDTO;
import edu.icet.expense_service.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expense")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5174", "http://localhost:5173"})
public class ExpenseController {

    @Autowired
    ExpenseService expenseService;

    @GetMapping("/get-all-expenses")
    public List<ExpenseDTO> getAllExpenses(){
        return expenseService.getAll(); // This now returns only current user's expenses
    }

    @PostMapping("/add-new-expense")
    public ResponseEntity<ExpenseDTO> addExpense(@RequestBody ExpenseDTO expenseDTO){
        try {
            ExpenseDTO savedExpense = expenseService.addExpense(expenseDTO);
            return ResponseEntity.ok(savedExpense);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/add-expense-with-validation")
    public ResponseEntity<ExpenseDTO> addExpenseWithValidation(@RequestBody ExpenseDTO expenseDTO){
        try {
            // This method will be implemented in the service layer with validation
            ExpenseDTO savedExpense = expenseService.addExpense(expenseDTO);
            return ResponseEntity.ok(savedExpense);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/update-expense")
    public ResponseEntity<Void> updateExpense(@RequestBody ExpenseDTO expenseDTO){
        try {
            expenseService.updateExpense(expenseDTO);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id){
        try {
            boolean deleted = expenseService.deleteExpense(id);
            return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseDTO> getExpenseById(@PathVariable Long id){
        try {
            ExpenseDTO expense = expenseService.searchById(id);
            return expense != null ? ResponseEntity.ok(expense) : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Additional user-specific endpoints
    @GetMapping("/budget/{budgetId}")
    public List<ExpenseDTO> getExpensesByBudgetId(@PathVariable Long budgetId){
        return expenseService.getExpensesByBudgetId(budgetId);
    }

    @GetMapping("/category/{categoryId}")
    public List<ExpenseDTO> getExpensesByCategoryId(@PathVariable Long categoryId){
        return expenseService.getExpensesByCategoryId(categoryId);
    }


}