package org.example.controller;

import org.example.model.dto.BudgetDTO;
import org.example.service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/budget")
public class BudgetController {

    @Autowired
    BudgetService budgetService;

    @GetMapping("/get-all-budgets")
    public List<BudgetDTO> getAllBudgets(){
        return budgetService.getAll();
    }

    @PostMapping("/add-new-budget")
    public void addBudget(@RequestBody BudgetDTO budgetDTO){
        budgetService.addBudget(budgetDTO);
    }

    @PutMapping("/update-budget")
    public void updateBudget(@RequestBody BudgetDTO budgetDTO){
        budgetService.updateBudget(budgetDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteBudget(@PathVariable Long id){
        budgetService.deleteBudget(id);
    }

    @GetMapping("/{id}")
    public BudgetDTO searchBudget(@PathVariable Long id){
        return budgetService.searchById(id);
    }
}
