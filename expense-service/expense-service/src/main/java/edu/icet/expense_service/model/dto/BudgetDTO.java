package edu.icet.expense_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BudgetDTO {
    private Long budgetId;
    private Integer month;
    private Integer year;
    private Double amount;
    private String budgetTitle;
}