package edu.icet.expense_service.client;

import edu.icet.expense_service.model.dto.BudgetDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "budget-service")
public interface BudgetClient {
    @GetMapping("/budget/{id}")
    BudgetDTO searchBudget(@PathVariable Long id);

    @PutMapping("/budget/update-budget/{id}")
    void updateBudget(@PathVariable Long id, @RequestBody BudgetDTO budgetDTO);
}