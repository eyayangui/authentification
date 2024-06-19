package com.sofrecom.authentificationms.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.sofrecom.authentificationms.dto.VehicleDTO;
import com.sofrecom.authentificationms.entity.*;
import com.sofrecom.authentificationms.mapper.VehicleMapper;
import com.sofrecom.authentificationms.repository.CollaboratorRepository;
import com.sofrecom.authentificationms.repository.CollaboratorVehicleRepository;
import com.sofrecom.authentificationms.repository.VehicleRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private CollaboratorRepository collaboratorRepository;

    @Autowired
    private CollaboratorVehicleRepository collaboratorVehicleRepository;

    @Autowired
    private VehicleMapper vehicleMapper ;

    private static final Logger log = LoggerFactory.getLogger(VehicleService.class);

    public Vehicle addVehicleForCollaborator(Integer collaboratorId, VehicleDTO vehicleDTO) {
        Optional<Collaborator> collaboratorOptional = collaboratorRepository.findById(collaboratorId);
        if (collaboratorOptional.isEmpty()) {
            throw new NoSuchElementException("Collaborator not found with ID: " + collaboratorId);
        }

        Collaborator collaborator = collaboratorOptional.get();

        Vehicle newVehicle = new Vehicle();
        BeanUtils.copyProperties(vehicleDTO, newVehicle);


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



    /*public List<VehicleDTO> getAllVehicleDTOsForCollaborator(Integer collaboratorId) {
        List<Vehicle> vehicles = vehicleRepository.findByCollaboratorId(collaboratorId);

        return vehicles.stream()
                .map(vehicleMapper::mapVehicleToDTO)
                .collect(Collectors.toList());
    }*/

    public void deleteVehicleById(Long vehicleId) {
        vehicleRepository.deleteById(vehicleId);
    }




    public List<Vehicle> getBrandsByTypeAndPrefix(VehicleType vehicleType, String brandPrefix) {
        return vehicleRepository.findByVehicleTypeAndBrandStartingWithIgnoreCase(vehicleType, brandPrefix);
    }

    public List<Vehicle> getModelsByBrandAndPrefix(VehicleType vehicleType, String brand, String modelPrefix) {
        return vehicleRepository.findByVehicleTypeAndBrandAndModelStartingWithIgnoreCase(vehicleType, brand, modelPrefix);
    }

    public CollaboratorVehicle assignVehicleToCollaborator(Integer collaboratorId, VehicleType vehicleType, String brand, String model) {
        Optional<Collaborator> collaborator = collaboratorRepository.findById(collaboratorId);
        if (!collaborator.isPresent()) {
            throw new RuntimeException("Collaborator not found");
        }

        Vehicle vehicle;
        if (vehicleType == VehicleType.CAR) {
            vehicle = vehicleRepository.findByVehicleTypeAndBrandAndModelStartingWithIgnoreCase(vehicleType, brand, model)
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Vehicle not found"));
        } else {
            vehicle = new Vehicle();
            vehicle.setVehicleType(vehicleType);
            vehicle.setBrand(brand);
            vehicle.setModel(model);
            vehicle = vehicleRepository.save(vehicle);
        }

        CollaboratorVehicle collaboratorVehicle = new CollaboratorVehicle();
        collaboratorVehicle.setCollaborator(collaborator.get());
        collaboratorVehicle.setVehicle(vehicle);
        return collaboratorVehicleRepository.save(collaboratorVehicle);
    }

    public List<VehicleDTO> getVehicleDTOsByCollaboratorId(Integer collaboratorId) {
        return collaboratorVehicleRepository.findVehicleDTOsByCollaboratorId(collaboratorId);
    }

    @Transactional
    public void deleteCollaboratorVehiclesByVehicleIdAndCollaboratorId(Long vehicleId, Long collaboratorId) {
        collaboratorVehicleRepository.deleteByVehicleIdAndCollaboratorId(vehicleId, collaboratorId);
    }


    @Transactional
    public CollaboratorVehicle updateVehicle(Integer idCollaborator, Long oldVehicleId, Long newVehicleId) {
        CollaboratorVehicle collaboratorVehicle = collaboratorVehicleRepository.findByCollaboratorIdAndVehicleId(idCollaborator, oldVehicleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "CollaboratorVehicle not found"));

        Vehicle newVehicle = vehicleRepository.findById(newVehicleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle not found"));

        collaboratorVehicle.setVehicle(newVehicle);
        return collaboratorVehicleRepository.save(collaboratorVehicle);
    }

}
