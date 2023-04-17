package com.example.warehouse_management.models.warehouse;

import com.example.warehouse_management.models.goods.Goods;
import com.example.warehouse_management.models.type.EStatusStorage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "bin_locations")
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BinLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(unique = true,nullable = false)
    private String code;
    @Column(columnDefinition = "text",length = 255)
    private String name;
    private double height;
    private double width;
    private double length;
    private double volume;
    private double remainingVolume;
    private int maxCapacity;
    private int currentCapacity;
    @Enumerated(EnumType.STRING)
    private EStatusStorage status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name ="columnLocationId" )
    private ColumnLocation columnLocation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name ="goodsId" )
    private Goods goods;

}
