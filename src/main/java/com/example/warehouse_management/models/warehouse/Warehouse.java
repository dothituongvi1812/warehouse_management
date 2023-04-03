package com.example.warehouse_management.models.warehouse;

import com.example.warehouse_management.models.type.EStatusStorage;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.Date;
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

    @Column(unique = true)
    private String code;
    @Column(columnDefinition = "text",length = 255)
    private String name;
    @Enumerated(EnumType.STRING)
    private EStatusStorage status;
    @Type(type = "jsonb")
    @Column(name = "location", columnDefinition = "jsonb")
    private Location location;
    private double length;
    private double width;
    private double height;
    private double volume;
    private double acreage;
    @OneToMany(mappedBy = "warehouse")
    @JsonManagedReference
    private Set<ShelveStorage> shelveStorages;


}
