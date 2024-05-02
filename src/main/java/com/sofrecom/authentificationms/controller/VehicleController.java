package com.sofrecom.authentificationms.controller;

import com.sofrecom.authentificationms.dto.VehicleDTO;
import com.sofrecom.authentificationms.entity.Vehicle;
import com.sofrecom.authentificationms.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @PostMapping("/{collaboratorId}")
    public ResponseEntity<Vehicle> addVehicleForCollaborator(
            @PathVariable Integer collaboratorId,
            @RequestBody VehicleDTO vehicleDTO) {
        Vehicle newVehicle = vehicleService.addVehicleForCollaborator(collaboratorId, vehicleDTO);
        return ResponseEntity.ok(newVehicle);
    }

    @PutMapping("/{vehicleId}")
    public ResponseEntity<Vehicle> updateVehicle(
            @PathVariable Long vehicleId,
            @RequestBody VehicleDTO vehicleDTO) {
        Vehicle updatedVehicle = vehicleService.updateVehicle(vehicleId, vehicleDTO);
        return ResponseEntity.ok(updatedVehicle);
    }

    @GetMapping("/collaborator/{collaboratorId}")
    public ResponseEntity<List<VehicleDTO>> getAllVehicleDTOsForCollaborator(
            @PathVariable Integer collaboratorId) {
        List<VehicleDTO> vehicleDTOs = vehicleService.getAllVehicleDTOsForCollaborator(collaboratorId);
        return ResponseEntity.ok(vehicleDTOs);
    }

    @DeleteMapping("/{vehicleId}")
    public ResponseEntity<Void> deleteVehicleById(
            @PathVariable Long vehicleId) {
        vehicleService.deleteVehicleById(vehicleId);
        return ResponseEntity.noContent().build();
    }

}
