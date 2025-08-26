package edu.icet.budget_service.repository;

import edu.icet.budget_service.model.entity.BudgetEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetRepository extends JpaRepository<BudgetEntity, Long> {
}
