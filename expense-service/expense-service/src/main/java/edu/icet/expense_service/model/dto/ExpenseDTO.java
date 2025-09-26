package edu.icet.expense_service.model.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseDTO {
    private Long expenseId;
    private String title;
    private Double amount;
    private LocalDate date;
    private String note;

    private Long budgetId;
    private String budgetTitle;

    private Long categoryId;
    private String name;

    private Long userId;
    private String username;

    @Override
    public String toString() {
        return "ExpenseDTO{" +
                "expenseId=" + expenseId +
                ", title='" + title + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                ", note='" + note + '\'' +
                '}';
    }
}
