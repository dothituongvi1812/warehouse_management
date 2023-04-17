package com.example.warehouse_management.models.warehouse;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Table
@Entity(name = "shelve_storages")
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
    private int numberOfFloors;
    private double width;
    private double height;
    private double length;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouseId")
    private Warehouse warehouse;

    @OneToMany(mappedBy = "shelfStorage")
    private Set<ColumnLocation> columnLocations;


}
