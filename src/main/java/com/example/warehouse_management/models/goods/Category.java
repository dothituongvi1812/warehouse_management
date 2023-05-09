package com.example.warehouse_management.models.goods;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "category")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true,nullable = false)
    private String code;
    @Column(columnDefinition = "text",length = 255)
    private String name;
    private String description;

    @OneToMany(mappedBy = "category",
            orphanRemoval = true,
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    private List<Goods> goodsList;
}