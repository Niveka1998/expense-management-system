package edu.icet.budget_service.controller;

import edu.icet.budget_service.model.dto.BudgetDTO;
import edu.icet.budget_service.service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/budget")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5174", "http://localhost:5173"})
public class BudgetController {

    @Autowired
    BudgetService budgetService;

    @GetMapping("/get-all-budgets")
    public List<BudgetDTO> getAllBudgetsForCurrentUser(){
        // Extract user ID from authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof edu.icet.budget_service.model.dto.UserDTO) {
            edu.icet.budget_service.model.dto.UserDTO userDTO = (edu.icet.budget_service.model.dto.UserDTO) authentication.getPrincipal();
            return budgetService.getAllForUser(userDTO.getUserId());
        }
        throw new RuntimeException("User not authenticated");
    }

    @PostMapping("/add-new-budget")
    public void addBudget(@RequestBody BudgetDTO budgetDTO){
        budgetService.addBudget(budgetDTO);
    }

    @PutMapping("/update-budget/{id}")
    public void updateBudget(@PathVariable Long id, @RequestBody BudgetDTO budgetDTO){
        budgetDTO.setBudgetId(id);
        budgetService.updateBudget(budgetDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteBudget(@PathVariable Long id){
        budgetService.deleteBudget(id);
    }

    @GetMapping("/{id}")
    public BudgetDTO searchBudget(@PathVariable Long id){
        return budgetService.getBudgetForCurrentUser(id); // Changed to user-specific method
    }
}