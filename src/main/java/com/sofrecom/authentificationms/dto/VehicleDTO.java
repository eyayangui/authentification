package com.sofrecom.authentificationms.dto;

import com.sofrecom.authentificationms.entity.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleDTO {
    private Long idVehicule ;
    private String brand;
    private String model;
    private VehicleType vehicleType;
}
