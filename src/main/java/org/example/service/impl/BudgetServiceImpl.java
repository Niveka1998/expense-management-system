package org.example.service.impl;

import org.example.model.dto.BudgetDTO;
import org.example.model.dto.CategoryDTO;
import org.example.model.entity.BudgetEntity;
import org.example.repository.BudgetRepository;
import org.example.service.BudgetService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
            budgetDTOS.add(mapper.map(budgetEntity, BudgetDTO.class));
        }
        return budgetDTOS;
    }

    @Override
    public void addBudget(BudgetDTO budgetDTO) {
        BudgetEntity budgetEntity = mapper.map(budgetDTO, BudgetEntity.class);
        budgetRepository.save(budgetEntity);
    }

    @Override
    public void updateBudget(BudgetDTO budgetDTO) {
        BudgetEntity budgetEntity = new BudgetEntity(budgetDTO.getBudgetId(), budgetDTO.getMonth(), budgetDTO.getYear(),budgetDTO.getAmount());
        budgetRepository.save(budgetEntity);
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
        return budgetRepository.findById(id)
                .map(budgetEntity -> mapper.map(budgetEntity, BudgetDTO.class))
                .orElse(null);
    }
}
