package org.example.model.dto;

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
}
