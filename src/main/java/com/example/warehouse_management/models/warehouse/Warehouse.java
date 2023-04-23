package com.example.warehouse_management.models.warehouse;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "warehouse")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Getter
@Setter
@NoArgsConstructor
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false)
    private String code;
    @Column(columnDefinition = "text",length = 255,unique = true)
    private String name;
    @Type(type = "jsonb")
    @Column(name = "location", columnDefinition = "jsonb")
    private Location location;
    //@Column(name = "length", columnDefinition = "FLOAT(8) CHECK (length > 0)")
    private double length;
    //@Column(name = "width", columnDefinition = "FLOAT(8) CHECK (width > 0)")
    private double width;
    //@Column(name = "height", columnDefinition = "FLOAT(8) CHECK (height > 0)")
    private double height;
    //@Column(name = "volume", columnDefinition = "FLOAT(8) CHECK (volume > 0)")
    private double volume;
    //@Column(name = "acreage", columnDefinition = "FLOAT(8) CHECK (acreage > 0)")
    private double acreage;
    @OneToMany(mappedBy = "warehouse")
    @JsonManagedReference
    private Set<ShelfStorage> shelfStorages;


}
