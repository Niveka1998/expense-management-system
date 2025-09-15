package edu.icet.expense_service.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "expenses")
public class ExpenseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
}
