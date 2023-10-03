package ru.practicum.main.service.category.model;

import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Setter
@ToString
@Entity
@Table(name = "categories")
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
}
