package edu.icet.budget_service.service.impl;

import edu.icet.budget_service.model.dto.BudgetDTO;
import edu.icet.budget_service.model.entity.BudgetEntity;
import edu.icet.budget_service.repository.BudgetRepository;
import edu.icet.budget_service.service.BudgetService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<BudgetDTO> getAll() {
        List<BudgetEntity> budgetEntities = budgetRepository.findAll();
        List<BudgetDTO> budgetDTOS = new ArrayList<>();
        for(BudgetEntity budgetEntity : budgetEntities){
            BudgetDTO budgetDTO = new BudgetDTO();
            budgetDTO.setBudgetId(budgetEntity.getBudgetId());
            budgetDTO.setBudgetTitle(budgetEntity.getBudgetTitle());
            budgetDTO.setAmount(budgetEntity.getAmount());
            budgetDTO.setYear(budgetDTO.getYear());
            budgetDTO.setMonth(budgetEntity.getMonth());

            budgetDTOS.add(budgetDTO);
        }System.out.println(budgetDTOS);
        return budgetDTOS;

    }

    @Override
    public void addBudget(BudgetDTO budgetDTO) {
        BudgetEntity budgetEntity = new BudgetEntity();
        budgetEntity.setBudgetTitle(budgetDTO.getBudgetTitle());
        budgetEntity.setYear(budgetDTO.getYear());
        budgetEntity.setAmount(budgetDTO.getAmount());
        budgetEntity.setMonth(budgetDTO.getMonth());
        budgetEntity.setUserId(budgetDTO.getUserId());
        budgetEntity.setUsername(budgetDTO.getUsername());
        budgetRepository.save(budgetEntity);
    }

    @Override
    public void updateBudget(BudgetDTO budgetDTO) {
        Optional<BudgetEntity> existingEntity = budgetRepository.findById(budgetDTO.getBudgetId());
        if(existingEntity.isPresent()){
            BudgetEntity budgetEntity = existingEntity.get();
            budgetEntity.setBudgetTitle(budgetDTO.getBudgetTitle());
            budgetEntity.setYear(budgetDTO.getYear());
            budgetEntity.setMonth(budgetDTO.getMonth());
            budgetEntity.setAmount(budgetDTO.getAmount());

            budgetRepository.save(budgetEntity);
        }

    }

    @Override
    public boolean deleteBudget(Long id) {
        if (budgetRepository.existsById(id)) {
            budgetRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public BudgetDTO searchById(Long id) {
        Optional<BudgetEntity> optionalEntity = budgetRepository.findById(id);
        if(optionalEntity.isPresent()){
            BudgetEntity entity = optionalEntity.get();
            BudgetDTO dto = new BudgetDTO();
            dto.setBudgetId(entity.getBudgetId());
            dto.setBudgetTitle(entity.getBudgetTitle());
            dto.setMonth(entity.getMonth());
            dto.setYear(entity.getYear());
            dto.setAmount(entity.getAmount());
            return dto;
        }
        return null;
    }
}
