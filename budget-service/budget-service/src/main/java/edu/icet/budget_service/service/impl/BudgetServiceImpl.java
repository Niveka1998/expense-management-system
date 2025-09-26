package edu.icet.budget_service.service.impl;

import edu.icet.budget_service.model.dto.BudgetDTO;
import edu.icet.budget_service.model.entity.BudgetEntity;
import edu.icet.budget_service.repository.BudgetRepository;
import edu.icet.budget_service.service.BudgetService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BudgetServiceImpl implements BudgetService {

    @Autowired
    BudgetRepository budgetRepository;

    ModelMapper mapper = new ModelMapper();

    @Override
    public List<BudgetDTO> getAllForUser(Long userId) {
        List<BudgetEntity> budgetEntities = budgetRepository.findByUserId(userId);
        List<BudgetDTO> budgetDTOS = new ArrayList<>();

        for(BudgetEntity budgetEntity : budgetEntities){
            BudgetDTO budgetDTO = convertToDTO(budgetEntity);
            budgetDTOS.add(budgetDTO);
        }

        return budgetDTOS;
    }
    // Helper method to get current user ID
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof edu.icet.budget_service.model.dto.UserDTO) {
            edu.icet.budget_service.model.dto.UserDTO userDTO = (edu.icet.budget_service.model.dto.UserDTO) authentication.getPrincipal();
            return userDTO.getUserId();
        }
        throw new RuntimeException("User not authenticated");
    }

    // Get only budgets for the current user
    @Override
    public List<BudgetDTO> getAllForCurrentUser() {
        Long userId = getCurrentUserId();
        List<BudgetEntity> budgetEntities = budgetRepository.findByUserId(userId);
        List<BudgetDTO> budgetDTOS = new ArrayList<>();

        for(BudgetEntity budgetEntity : budgetEntities){
            BudgetDTO budgetDTO = convertToDTO(budgetEntity);
            budgetDTOS.add(budgetDTO);
        }

        System.out.println("Returning " + budgetDTOS.size() + " budgets for user ID: " + userId);
        return budgetDTOS;
    }

    // Keep this method only if you need admin functionality
    @Override
    public List<BudgetDTO> getAll() {
        // Consider removing this method or securing it with admin role check
        List<BudgetEntity> budgetEntities = budgetRepository.findAll();
        List<BudgetDTO> budgetDTOS = new ArrayList<>();

        for(BudgetEntity budgetEntity : budgetEntities){
            BudgetDTO budgetDTO = convertToDTO(budgetEntity);
            budgetDTOS.add(budgetDTO);
        }

        return budgetDTOS;
    }

    @Override
    public void addBudget(BudgetDTO budgetDTO) {
        BudgetEntity budgetEntity = new BudgetEntity();
        budgetEntity.setBudgetTitle(budgetDTO.getBudgetTitle());
        budgetEntity.setYear(budgetDTO.getYear());
        budgetEntity.setAmount(budgetDTO.getAmount());
        budgetEntity.setMonth(budgetDTO.getMonth());

        // Set user information from authentication
        Long userId = getCurrentUserId();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof edu.icet.budget_service.model.dto.UserDTO) {
            edu.icet.budget_service.model.dto.UserDTO userDTO = (edu.icet.budget_service.model.dto.UserDTO) authentication.getPrincipal();
            budgetEntity.setUserId(userId);
            budgetEntity.setUsername(userDTO.getUsername());
            System.out.println("Adding budget for user: " + userDTO.getUsername() + " (ID: " + userId + ")");
        }

        budgetRepository.save(budgetEntity);
    }

    @Override
    public void updateBudget(BudgetDTO budgetDTO) {
        Long userId = getCurrentUserId();
        Optional<BudgetEntity> existingEntity = budgetRepository.findByBudgetIdAndUserId(budgetDTO.getBudgetId(), userId);

        if(existingEntity.isPresent()){
            BudgetEntity budgetEntity = existingEntity.get();
            budgetEntity.setBudgetTitle(budgetDTO.getBudgetTitle());
            budgetEntity.setYear(budgetDTO.getYear());
            budgetEntity.setMonth(budgetDTO.getMonth());
            budgetEntity.setAmount(budgetDTO.getAmount());

            budgetRepository.save(budgetEntity);
        } else {
            throw new RuntimeException("Budget not found or access denied");
        }
    }

    @Override
    @Transactional
    public boolean deleteBudget(Long id) {
        Long userId = getCurrentUserId();
        if (budgetRepository.existsByBudgetIdAndUserId(id, userId)) {
            budgetRepository.deleteByBudgetIdAndUserId(id, userId);
            return true;
        }
        return false;
    }

    @Override
    public BudgetDTO searchById(Long id) {
        // Consider making this user-specific too
        Optional<BudgetEntity> optionalEntity = budgetRepository.findById(id);
        if(optionalEntity.isPresent()){
            BudgetEntity entity = optionalEntity.get();
            return convertToDTO(entity);
        }
        return null;
    }

    @Override
    public BudgetDTO getBudgetForCurrentUser(Long id) {
        Long userId = getCurrentUserId();
        Optional<BudgetEntity> optionalEntity = budgetRepository.findByBudgetIdAndUserId(id, userId);
        if(optionalEntity.isPresent()){
            BudgetEntity entity = optionalEntity.get();
            return convertToDTO(entity);
        }
        throw new RuntimeException("Budget not found or access denied");
    }

    // Helper method to convert entity to DTO
    private BudgetDTO convertToDTO(BudgetEntity budgetEntity) {
        BudgetDTO budgetDTO = new BudgetDTO();
        budgetDTO.setBudgetId(budgetEntity.getBudgetId());
        budgetDTO.setBudgetTitle(budgetEntity.getBudgetTitle());
        budgetDTO.setAmount(budgetEntity.getAmount());
        budgetDTO.setYear(budgetEntity.getYear());
        budgetDTO.setMonth(budgetEntity.getMonth());
        budgetDTO.setUserId(budgetEntity.getUserId());
        budgetDTO.setUsername(budgetEntity.getUsername());
        return budgetDTO;
    }
}