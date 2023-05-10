package com.example.warehouse_management.models.warehouse;

import com.example.warehouse_management.models.goods.Goods;
import com.example.warehouse_management.models.type.EStatusStorage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "bin_positions")
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BinPosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(unique = true,nullable = false)
    private String code;
    @Column(name="name")
    private String name;
    @Column(name = "height", columnDefinition = "NUMERIC(5, 2) CHECK (height > 0)")
    private double height;
    @Column(name = "width", columnDefinition = "NUMERIC(5, 2) CHECK (width > 0)")
    private double width;
    @Column(name = "length", columnDefinition = "NUMERIC(5, 2) CHECK (length > 0)")
    private double length;
    @Column(name = "volume", columnDefinition = "NUMERIC(5, 2) CHECK (volume > 0)")
    private double volume;
    @Column(name = "remaining_volume", columnDefinition = "NUMERIC(5, 2) CHECK (remaining_volume >= 0 AND remaining_volume <= volume)")
    private double remainingVolume;
    @Column(name = "max_capacity", columnDefinition = "INT CHECK (max_capacity >= 0)")
    private int maxCapacity;
    @Column(name = "current_capacity", columnDefinition = "INT CHECK (current_capacity >= 0 AND current_capacity <= max_capacity)")
    private int currentCapacity;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "VARCHAR(60) CHECK (status IN ('FULL', 'AVAILABLE', 'EMPTY'))")
    private EStatusStorage status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name ="columnPositionId" ,nullable = false)
    private ColumnPosition columnPosition;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name ="goodsId" ,nullable = true)
    private Goods goods;

}
