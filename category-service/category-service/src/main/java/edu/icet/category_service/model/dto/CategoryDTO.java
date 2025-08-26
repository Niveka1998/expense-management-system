package edu.icet.category_service.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    @JsonIgnore
    private Long categoryId;
    private String name;
    private String description;
}
