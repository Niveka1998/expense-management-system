package edu.icet.budget_service.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BudgetDTO {
    @JsonIgnore
    private Long budgetId;
    private Integer month;
    private Integer year;
    private Double amount;
    private String budgetTitle;
    @Override
    public String toString() {
        return "BudgetDTO{" +
                "budgetId=" + budgetId +
                ", month=" + month +
                ", year=" + year +
                ", amount=" + amount +
                '}';
    }

}
