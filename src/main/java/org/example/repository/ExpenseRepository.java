package org.example.repository;

import org.example.model.entity.CategoryEntity;
import org.example.model.entity.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long> {
}
