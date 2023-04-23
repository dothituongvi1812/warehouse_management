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
   // @Column(name = "number_Of_floors", columnDefinition = "INT(4) CHECK (number_Of_floors >= 0)")
    private int numberOfFloors;
   // @Column(name = "width", columnDefinition = "FLOAT(8) CHECK (width > 0)")
    private double width;
   // @Column(name = "height", columnDefinition = "FLOAT(8) CHECK (height > 0)")
    private double height;
   // @Column(name = "length", columnDefinition = "FLOAT(8) CHECK (length > 0)")
    private double length;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouseId")
    private Warehouse warehouse;

    @OneToMany(mappedBy = "shelfStorage")
    private Set<ColumnLocation> columnLocations;


}
