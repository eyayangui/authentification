package com.sofrecom.authentificationms.dto;

import com.sofrecom.authentificationms.entity.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleDTO {
    private Long idVehicle ;
    private String brand;
    private String power;
    private Integer numberSeat;
    private String plateNumber;
    private VehicleType vehicleType;
}
