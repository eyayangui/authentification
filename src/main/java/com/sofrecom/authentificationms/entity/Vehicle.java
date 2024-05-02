package com.sofrecom.authentificationms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVehicule ;
    private String brand ;
    private String power ;
    private Integer numberSeat ;
    @Column(unique = true)
    private String plateNumber;

    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType ;

    @ManyToOne
    @JoinColumn(name = "collaborator_id")
    @JsonIgnore
    private Collaborator collaborator ;

}
