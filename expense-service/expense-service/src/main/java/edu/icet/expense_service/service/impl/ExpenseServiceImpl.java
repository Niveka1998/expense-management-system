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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    // Helper method to get current user ID
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDTO) {
            UserDTO userDTO = (UserDTO) authentication.getPrincipal();
            return userDTO.getUserId();
        }
        throw new RuntimeException("User not authenticated");
    }

    // Helper method to get current user info
    private UserDTO getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDTO) {
            return (UserDTO) authentication.getPrincipal();
        }
        throw new RuntimeException("User not authenticated");
    }

    @Override
    public List<ExpenseDTO> getAll() {
        Long userId = getCurrentUserId();
        System.out.println("=== DEBUG: Getting expenses for user ID: " + userId + " ===");

        List<ExpenseEntity> expenseEntities = expenseRepository.findByUserId(userId);
        System.out.println("Found " + expenseEntities.size() + " raw expenses from repository");

        List<ExpenseDTO> expenseDTOS = new ArrayList<>();
        for(ExpenseEntity expenseEntity : expenseEntities){
            System.out.println("Processing expense: " + expenseEntity.getTitle() +
                    " (ID: " + expenseEntity.getExpenseId() +
                    ", User ID: " + expenseEntity.getUserId() + ")");

            try {
                ExpenseDTO expenseDTO = convertToDTO(expenseEntity);
                expenseDTOS.add(expenseDTO);
                System.out.println("Converted to DTO: " + expenseDTO.getTitle());
            } catch (Exception e) {
                System.err.println("Error converting expense to DTO: " + e.getMessage());
                e.printStackTrace();
            }
        }

        System.out.println("Returning " + expenseDTOS.size() + " expenses for user ID: " + userId);
        return expenseDTOS;
    }

    // Add this helper method
    private ExpenseDTO convertToDTO(ExpenseEntity expenseEntity) {
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setExpenseId(expenseEntity.getExpenseId());
        expenseDTO.setTitle(expenseEntity.getTitle());
        expenseDTO.setAmount(expenseEntity.getAmount());
        expenseDTO.setDate(expenseEntity.getDate());
        expenseDTO.setNote(expenseEntity.getNote());
        expenseDTO.setBudgetId(expenseEntity.getBudgetId());
        expenseDTO.setBudgetTitle(expenseEntity.getBudgetTitle());
        expenseDTO.setCategoryId(expenseEntity.getCategoryId());
        expenseDTO.setName(expenseEntity.getName());
        expenseDTO.setUserId(expenseEntity.getUserId());
        expenseDTO.setUsername(expenseEntity.getUsername());
        return expenseDTO;
    }

    public ExpenseDTO addExpenseWithValidation(ExpenseDTO expenseDTO) {
        // Get current user and set user info
        UserDTO currentUser = getCurrentUser();
        expenseDTO.setUserId(currentUser.getUserId());
        expenseDTO.setUsername(currentUser.getUsername());

        // Validate and get budget information
        BudgetDTO budgetDTO = budgetClient.searchBudget(expenseDTO.getBudgetId());
        if(budgetDTO == null){
            throw new RuntimeException("Budget not found for Id: " + expenseDTO.getBudgetId());
        }

        // Verify that the budget belongs to the current user (if needed)
        // Add this check if your budget service supports user-specific budgets

        // Check if there's enough budget
        if (budgetDTO.getAmount() < expenseDTO.getAmount()) {
            throw new RuntimeException("Not enough budget. Available: " + budgetDTO.getAmount());
        }

        // Update budget amount
        budgetDTO.setAmount(budgetDTO.getAmount() - expenseDTO.getAmount());
        budgetClient.updateBudget(expenseDTO.getBudgetId(), budgetDTO);

        // Validate and get category information
        CategoryDTO category = categoryClient.searchCategory(expenseDTO.getCategoryId());
        if (category == null) {
            throw new RuntimeException("Category not found for Id: " + expenseDTO.getCategoryId());
        }

        // Set the budget title and category name in the expense DTO
        expenseDTO.setBudgetTitle(budgetDTO.getBudgetTitle());
        expenseDTO.setName(category.getName());

        System.out.println("=== DEBUG: Adding expense with validation ===");
        System.out.println("User ID: " + expenseDTO.getUserId());
        System.out.println("Username: " + expenseDTO.getUsername());
        System.out.println("Budget Title: " + expenseDTO.getBudgetTitle());
        System.out.println("Category Name: " + expenseDTO.getName());

        try {
            return addExpense(expenseDTO);
        } catch (Exception e) {
            System.err.println("Error adding expense: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to add expense", e);
        }
    }
    @Override
    public ExpenseDTO addExpense(ExpenseDTO expenseDTO) {
        // Set current user info if not already set
        if (expenseDTO.getUserId() == null) {
            UserDTO currentUser = getCurrentUser();
            expenseDTO.setUserId(currentUser.getUserId());
            expenseDTO.setUsername(currentUser.getUsername());
        }

        ExpenseEntity expenseEntity = new ExpenseEntity();
        expenseEntity.setTitle(expenseDTO.getTitle());
        expenseEntity.setAmount(expenseDTO.getAmount());
        expenseEntity.setDate(expenseDTO.getDate());
        expenseEntity.setNote(expenseDTO.getNote());

        expenseEntity.setBudgetId(expenseDTO.getBudgetId());
        expenseEntity.setBudgetTitle(expenseDTO.getBudgetTitle());

        expenseEntity.setCategoryId(expenseDTO.getCategoryId());
        expenseEntity.setName(expenseDTO.getName());

        expenseEntity.setUserId(expenseDTO.getUserId());
        expenseEntity.setUsername(expenseDTO.getUsername());

        expenseRepository.save(expenseEntity);

        // Set the generated ID back to the DTO
        expenseDTO.setExpenseId(expenseEntity.getExpenseId());
        return expenseDTO;
    }

    @Override
    public void updateExpense(ExpenseDTO expenseDTO) {
        Long userId = getCurrentUserId();

        // Verify that the expense belongs to the current user
        Optional<ExpenseEntity> existingExpense = expenseRepository.findById(expenseDTO.getExpenseId());
        if (existingExpense.isPresent()) {
            ExpenseEntity expense = existingExpense.get();
            if (!expense.getUserId().equals(userId)) {
                throw new RuntimeException("Expense does not belong to the current user");
            }

            // Update only allowed fields
            expense.setTitle(expenseDTO.getTitle());
            expense.setAmount(expenseDTO.getAmount());
            expense.setDate(expenseDTO.getDate());
            expense.setNote(expenseDTO.getNote());
            expense.setBudgetId(expenseDTO.getBudgetId());
            expense.setBudgetTitle(expenseDTO.getBudgetTitle());
            expense.setCategoryId(expenseDTO.getCategoryId());
            expense.setName(expenseDTO.getName());

            expenseRepository.save(expense);
        } else {
            throw new RuntimeException("Expense not found");
        }
    }

    @Override
    public boolean deleteExpense(Long id) {
        Long userId = getCurrentUserId();

        // Verify that the expense belongs to the current user
        Optional<ExpenseEntity> expense = expenseRepository.findById(id);
        if (expense.isPresent() && expense.get().getUserId().equals(userId)) {
            expenseRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public ExpenseDTO searchById(Long id) {
        Long userId = getCurrentUserId();

        return expenseRepository.findByExpenseIdAndUserId(id, userId)
                .map(expenseEntity -> mapper.map(expenseEntity, ExpenseDTO.class))
                .orElse(null);
    }

    // Additional user-specific methods
    public List<ExpenseDTO> getExpensesByBudgetId(Long budgetId) {
        Long userId = getCurrentUserId();
        List<ExpenseEntity> expenseEntities = expenseRepository.findByBudgetIdAndUserId(budgetId, userId);
        List<ExpenseDTO> expenseDTOS = new ArrayList<>();

        for(ExpenseEntity expenseEntity : expenseEntities){
            expenseDTOS.add(mapper.map(expenseEntity, ExpenseDTO.class));
        }

        return expenseDTOS;
    }

    public List<ExpenseDTO> getExpensesByCategoryId(Long categoryId) {
        Long userId = getCurrentUserId();
        List<ExpenseEntity> expenseEntities = expenseRepository.findByCategoryIdAndUserId(categoryId, userId);
        List<ExpenseDTO> expenseDTOS = new ArrayList<>();

        for(ExpenseEntity expenseEntity : expenseEntities){
            expenseDTOS.add(mapper.map(expenseEntity, ExpenseDTO.class));
        }

        return expenseDTOS;
    }
}