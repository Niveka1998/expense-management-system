package edu.icet.expense_service.controller;

import edu.icet.expense_service.model.dto.ExpenseDTO;
import edu.icet.expense_service.service.ExpenseService;
import edu.icet.expense_service.service.impl.ExpenseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/expense")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5174", "http://localhost:5173"})
public class ExpenseController {

    @Autowired
    ExpenseService expenseService;

    @Autowired
    ExpenseServiceImpl expenseServiceImpl; // For accessing the validation method


    @GetMapping("get-all-expenses")
    public List<ExpenseDTO> getAllExpenses(){

        return expenseService.getAll();
    }

    @PostMapping("/add-new-expense")
    public void addExpense(@RequestBody ExpenseDTO expenseDTO){

        expenseService.addExpense(expenseDTO);
        System.out.println(expenseDTO);
    }

    @PostMapping("/add-expense-with-validation")
    public ResponseEntity<ExpenseDTO> addExpenseWithValidation(@RequestBody ExpenseDTO expenseDTO){
        try {
            System.out.println("Received ExpenseDTO: " + expenseDTO);
            ExpenseDTO savedExpense = expenseServiceImpl.addExpenseWithValidation(expenseDTO);
            return ResponseEntity.ok(savedExpense);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
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
