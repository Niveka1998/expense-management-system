package edu.icet.expense_service.service.impl;

import edu.icet.expense_service.client.BudgetClient;
import edu.icet.expense_service.client.CategoryClient;
import edu.icet.expense_service.client.UserClient;
import edu.icet.expense_service.model.dto.BudgetDTO;
import edu.icet.expense_service.model.dto.CategoryDTO;
import edu.icet.expense_service.model.dto.ExpenseDTO;
import edu.icet.expense_service.model.dto.UserDTO;
import edu.icet.expense_service.model.entity.ExpenseEntity;
import edu.icet.expense_service.repository.ExpenseRepository;
import edu.icet.expense_service.service.ExpenseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ExpenseServiceImpl implements ExpenseService {

    @Autowired
    ExpenseRepository expenseRepository;

    @Autowired
    private UserClient userClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private BudgetClient budgetClient;

    ModelMapper mapper = new ModelMapper();

    @Override
    public List<ExpenseDTO> getAll() {
        List<ExpenseEntity> expenseEntities = expenseRepository.findAll();
        List<ExpenseDTO> expenseDTOS = new ArrayList<>();
        for(ExpenseEntity expenseEntity : expenseEntities){
            expenseDTOS.add(mapper.map(expenseEntity,ExpenseDTO.class));
        }
        return expenseDTOS;
    }

    public ExpenseDTO addExpenseWithValidation(ExpenseDTO expenseDTO) {
        BudgetDTO budgetDTO = budgetClient.searchBudget(expenseDTO.getBudgetId());

        if(budgetDTO == null){
            throw new RuntimeException("Budget not found for Id: "+expenseDTO.getBudgetId());
        }
        if (budgetDTO.getAmount() < expenseDTO.getAmount()) {
            throw new RuntimeException("Not enough budget. Available: " + budgetDTO.getAmount());
        }
        budgetDTO.setAmount(budgetDTO.getAmount() - expenseDTO.getAmount());
        budgetClient.updateBudget(expenseDTO.getBudgetId(), budgetDTO);
        try {
            UserDTO user = userClient.getUserByEmail(expenseDTO.getEmail());
            System.out.println("User found: " + user.getUsername());

            CategoryDTO category = categoryClient.searchCategory(expenseDTO.getCategoryId());
            System.out.println("Category found: " + category.getName());

            return addExpense(expenseDTO);

        } catch (Exception e) {
            System.err.println("Error validating expense: " + e.getMessage());
            throw new RuntimeException("Expense validation failed", e);
        }
    }
    @Override
    public ExpenseDTO addExpense(ExpenseDTO expenseDTO) {
        ExpenseEntity expenseEntity = mapper.map(expenseDTO, ExpenseEntity.class);
        expenseRepository.save(expenseEntity);
        return expenseDTO;
    }

    @Override
    public void updateExpense(ExpenseDTO expenseDTO) {
        ExpenseEntity expenseEntity = mapper.map(expenseDTO, ExpenseEntity.class);
        expenseRepository.save(expenseEntity);
    }

    @Override
    public boolean deleteExpense(Long id) {
        if (expenseRepository.existsById(id)) {
            expenseRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public ExpenseDTO searchById(Long id) {
        return expenseRepository.findById(id)
                .map(expenseEntity -> mapper.map(expenseEntity, ExpenseDTO.class))
                .orElse(null);
    }
}

