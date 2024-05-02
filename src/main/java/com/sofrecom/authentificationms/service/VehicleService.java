package com.sofrecom.authentificationms.service;

import com.sofrecom.authentificationms.dto.VehicleDTO;
import com.sofrecom.authentificationms.entity.Collaborator;
import com.sofrecom.authentificationms.entity.Vehicle;
import com.sofrecom.authentificationms.mapper.VehicleMapper;
import com.sofrecom.authentificationms.repository.CollaboratorRepository;
import com.sofrecom.authentificationms.repository.VehicleRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private CollaboratorRepository collaboratorRepository;

    @Autowired
    private VehicleMapper vehicleMapper ;

    public Vehicle addVehicleForCollaborator(Integer collaboratorId, VehicleDTO vehicleDTO) {
        Optional<Collaborator> collaboratorOptional = collaboratorRepository.findById(collaboratorId);
        if (collaboratorOptional.isEmpty()) {
            throw new NoSuchElementException("Collaborator not found with ID: " + collaboratorId);
        }

        Collaborator collaborator = collaboratorOptional.get();

        Vehicle newVehicle = new Vehicle();
        BeanUtils.copyProperties(vehicleDTO, newVehicle);
        newVehicle.setCollaborator(collaborator);

        return vehicleRepository.save(newVehicle);
    }

    public Vehicle updateVehicle(Long vehicleId, VehicleDTO vehicleDTO) {
        Optional<Vehicle> vehicleOptional = vehicleRepository.findById(vehicleId);
        if (vehicleOptional.isEmpty()) {
            throw new NoSuchElementException("Vehicle not found with ID: " + vehicleId);
        }

        Vehicle existingVehicle = vehicleOptional.get();
        BeanUtils.copyProperties(vehicleDTO, existingVehicle, "idVehicule", "collaborator");

        return vehicleRepository.save(existingVehicle);
    }



    public List<VehicleDTO> getAllVehicleDTOsForCollaborator(Integer collaboratorId) {
        List<Vehicle> vehicles = vehicleRepository.findByCollaboratorId(collaboratorId);

        return vehicles.stream()
                .map(vehicleMapper::mapVehicleToDTO)
                .collect(Collectors.toList());
    }

    public void deleteVehicleById(Long vehicleId) {
        vehicleRepository.deleteById(vehicleId);
    }


}
