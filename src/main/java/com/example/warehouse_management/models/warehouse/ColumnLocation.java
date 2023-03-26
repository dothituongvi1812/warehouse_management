package com.example.warehouse_management.models.warehouse;

import com.example.warehouse_management.models.type.EStatusStorage;
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
@Table(name="column_locations")
public class ColumnLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String code;
    private String name;
    private double length;
    @Enumerated(EnumType.STRING)
    private EStatusStorage status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="shelveStorageId" )
    private ShelveStorage shelveStorage;
    @OneToMany(mappedBy = "columnLocation")
    private Set<RowLocation> rowLocations;
}
