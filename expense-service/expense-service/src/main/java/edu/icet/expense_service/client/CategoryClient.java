package edu.icet.expense_service.client;

import edu.icet.expense_service.model.dto.CategoryDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "category-service")
public interface CategoryClient {
    @GetMapping("/category/{id}")
    CategoryDTO searchCategory(@PathVariable Long id);
}