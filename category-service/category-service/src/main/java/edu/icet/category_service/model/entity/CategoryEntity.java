package edu.icet.category_service.model.entity;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "category")
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long categoryId;
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;


    private Long userId;
    private String username;
}
