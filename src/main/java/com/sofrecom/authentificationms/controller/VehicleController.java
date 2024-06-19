package com.sofrecom.authentificationms.controller;

import com.sofrecom.authentificationms.dto.VehicleDTO;
import com.sofrecom.authentificationms.entity.CollaboratorVehicle;
import com.sofrecom.authentificationms.entity.Vehicle;
import com.sofrecom.authentificationms.entity.VehicleType;
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

  /* @GetMapping("/collaborator/{collaboratorId}")
    public ResponseEntity<List<VehicleDTO>> getAllVehicleDTOsForCollaborator(
            @PathVariable Integer collaboratorId) {
        List<VehicleDTO> vehicleDTOs = vehicleService.getAllVehicleDTOsForCollaborator(collaboratorId);
        return ResponseEntity.ok(vehicleDTOs);
    }*/

    @DeleteMapping("/{vehicleId}")
    public ResponseEntity<Void> deleteVehicleById(
            @PathVariable Long vehicleId) {
        vehicleService.deleteVehicleById(vehicleId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/brands")
    public List<Vehicle> getBrandsByTypeAndPrefix(@RequestParam VehicleType vehicleType, @RequestParam String brandPrefix) {
        return vehicleService.getBrandsByTypeAndPrefix(vehicleType, brandPrefix);
    }

    @GetMapping("/models")
    public List<Vehicle> getModelsByBrandAndPrefix(@RequestParam VehicleType vehicleType, @RequestParam String brand, @RequestParam String modelPrefix) {
        return vehicleService.getModelsByBrandAndPrefix(vehicleType, brand, modelPrefix);
    }

    @PostMapping("/assign")
    public ResponseEntity<?> assignVehicleToCollaborator(
            @RequestParam Integer collaboratorId,
            @RequestParam VehicleType vehicleType,
            @RequestParam String brand,
            @RequestParam String model) {

        try {
            CollaboratorVehicle collaboratorVehicle = vehicleService.assignVehicleToCollaborator(collaboratorId, vehicleType, brand, model);
            return ResponseEntity.ok(collaboratorVehicle);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error assigning vehicle: " + e.getMessage());
        }
    }

    @GetMapping("/byCollaborator/{collaboratorId}")
    public ResponseEntity<List<VehicleDTO>> getVehicleDTOsByCollaboratorId(@PathVariable Integer collaboratorId) {
        List<VehicleDTO> vehicleDTOs = vehicleService.getVehicleDTOsByCollaboratorId(collaboratorId);
        if (vehicleDTOs.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(vehicleDTOs);
    }

    @DeleteMapping("/{vehicleId}/collaborator-vehicles/{collaboratorId}")
    public ResponseEntity<?> deleteCollaboratorVehiclesByVehicleIdAndCollaboratorId(@PathVariable Long vehicleId, @PathVariable Long collaboratorId) {
        vehicleService.deleteCollaboratorVehiclesByVehicleIdAndCollaboratorId(vehicleId, collaboratorId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update-vehicle")
    public ResponseEntity<CollaboratorVehicle> updateVehicle(
            @RequestParam Integer idCollaborator,
            @RequestParam Long oldVehicleId,
            @RequestParam Long newVehicleId) {

        CollaboratorVehicle updatedCollaboratorVehicle = vehicleService.updateVehicle(idCollaborator, oldVehicleId, newVehicleId);
        return ResponseEntity.ok(updatedCollaboratorVehicle);
    }

}
