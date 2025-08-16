package org.example.model.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BudgetDTO {
    private Long budgetId;
    private Integer month;
    private Integer year;
    private Double amount;
}
