package com.example.warehouse_management.models.warehouse;

import com.example.warehouse_management.models.type.EStatusStorage;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Table
@Entity(name = "shelve_storages")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShelveStorage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String code;
    private String name;
    private int numberOfFloors;
    private double width;
    private double height;
    private double length;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouseId")
    private Warehouse warehouse;

    @OneToMany(mappedBy = "shelveStorage")
    private Set<ColumnLocation> columnLocations;


}
