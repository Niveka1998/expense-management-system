package edu.icet.budget_service.controller;

import edu.icet.budget_service.model.dto.BudgetDTO;
import edu.icet.budget_service.service.BudgetService;
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

    @PutMapping("/update-budget/{id}")
    public void updateBudget(@PathVariable Long id , @RequestBody BudgetDTO budgetDTO){
        budgetDTO.setBudgetId(id);
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
