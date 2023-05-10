package com.example.warehouse_management.models.warehouse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="column_positions")
public class ColumnPosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true,nullable = false)
    private String code;
    @Column(name = "name")
    private String name;
    @Column(name = "length", columnDefinition = "NUMERIC(5, 2) CHECK (length > 0)")
    private double length;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="shelfStorageId" )
    private ShelfStorage shelfStorage;
    @OneToMany(mappedBy = "columnPosition")
    private Set<BinPosition> binPositions;
}
