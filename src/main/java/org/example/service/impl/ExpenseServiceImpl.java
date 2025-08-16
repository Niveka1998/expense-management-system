package org.example.service.impl;

import org.example.model.dto.CategoryDTO;
import org.example.model.dto.ExpenseDTO;
import org.example.model.entity.ExpenseEntity;
import org.example.repository.ExpenseRepository;
import org.example.service.ExpenseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ExpenseServiceImpl implements ExpenseService {

    @Autowired
    ExpenseRepository expenseRepository;

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

    @Override
    public void addExpense(ExpenseDTO expenseDTO) {
        ExpenseEntity expenseEntity = mapper.map(expenseDTO, ExpenseEntity.class);
        expenseRepository.save(expenseEntity);
    }

    @Override
    public void updateExpense(ExpenseDTO expenseDTO) {
        ExpenseEntity expenseEntity = new ExpenseEntity(expenseDTO.getExpenseId(),expenseDTO.getTitle(),expenseDTO.getAmount(),expenseDTO.getDate(),expenseDTO.getNote());
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

