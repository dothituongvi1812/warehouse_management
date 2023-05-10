package com.example.warehouse_management.models.warehouse;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Table
@Entity(name = "shelf_storages")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShelfStorage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true,nullable = false)
    private String code;
    private String name;
    @Column(name = "number_Of_floors", columnDefinition = "INT CHECK (number_Of_floors >= 0)")
    private int numberOfFloors;
    @Column(name = "width", columnDefinition = "NUMERIC(5, 2) CHECK (width > 0)")
    private double width;
    @Column(name = "height", columnDefinition = "NUMERIC(5, 2) CHECK (height > 0)")
    private double height;
    @Column(name = "length", columnDefinition = "NUMERIC(5, 2) CHECK (length > 0)")
    private double length;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouseId")
    private Warehouse warehouse;

    @OneToMany(mappedBy = "shelfStorage")
    private Set<ColumnPosition> columnPositions;


}
