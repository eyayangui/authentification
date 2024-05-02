package com.sofrecom.authentificationms.mapper;

import com.sofrecom.authentificationms.dto.VehicleDTO;
import com.sofrecom.authentificationms.entity.Vehicle;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class VehicleMapper {
    public VehicleDTO mapVehicleToDTO(Vehicle vehicle) {
        VehicleDTO vehicleDTO = new VehicleDTO();
        BeanUtils.copyProperties(vehicle, vehicleDTO);
        return vehicleDTO;
    }
}
