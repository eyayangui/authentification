package com.sofrecom.authentificationms.service;

import com.sofrecom.authentificationms.dto.VehicleDTO;
import com.sofrecom.authentificationms.entity.Collaborator;
import com.sofrecom.authentificationms.entity.Vehicle;
import com.sofrecom.authentificationms.entity.VehicleType;
import com.sofrecom.authentificationms.mapper.VehicleMapper;
import com.sofrecom.authentificationms.repository.CollaboratorRepository;
import com.sofrecom.authentificationms.repository.VehicleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {
    @InjectMocks
    private VehicleService vehicleService;
    @Mock
    private VehicleRepository vehicleRepository;
    @Mock
    private CollaboratorRepository collaboratorRepository;
    @Mock
    private VehicleMapper vehicleMapper;

    private static VehicleDTO mockVehicleDto(Long id , String brand, String model) {
        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setIdVehicule(id);
        vehicleDTO.setBrand(brand);
        vehicleDTO.setModel(model);
        return vehicleDTO ;
    }

    private static Vehicle mockVehicle(Long id , String brand, String model) {
        Vehicle vehicle = new Vehicle();
        vehicle.setIdVehicule(id);
        vehicle.setBrand(brand);
        vehicle.setModel(model);
        return vehicle ;
    }

    @Test
    void testAddVehicleForCollaborator() {
        Integer collaboratorId = 1;
        VehicleDTO vehicleDTO = mockVehicleDto(1L,"Toyota", "4C");

        Collaborator collaborator = new Collaborator();
        collaborator.setIdCollaborator(collaboratorId);

        Mockito.when(collaboratorRepository.findById(collaboratorId)).thenReturn(Optional.of(collaborator));

        Vehicle savedVehicle = new Vehicle();
        BeanUtils.copyProperties(vehicleDTO, savedVehicle);


        Mockito.when(vehicleRepository.save(any(Vehicle.class))).thenReturn(savedVehicle);

        Vehicle result = vehicleService.addVehicleForCollaborator(collaboratorId, vehicleDTO);

        assertNotNull(result);
        assertEquals(vehicleDTO.getBrand(), result.getBrand());
        assertEquals(vehicleDTO.getModel(), result.getModel());

        verify(collaboratorRepository, times(1)).findById(collaboratorId);
        verify(vehicleRepository, times(1)).save(any(Vehicle.class));
    }

    @Test
    void testAddVehicleForCollaboratorWhenCollaboratorNotFound() {
        Integer collaboratorId = 1;
        VehicleDTO vehicleDTO = mockVehicleDto(1L , "Toyota", "4C");

        Mockito.when(collaboratorRepository.findById(collaboratorId)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> vehicleService.addVehicleForCollaborator(collaboratorId, vehicleDTO));

        assertEquals("Collaborator not found with ID: " + collaboratorId, exception.getMessage());

        verify(collaboratorRepository, times(1)).findById(collaboratorId);
        verify(vehicleRepository, never()).save(any(Vehicle.class));
    }


    @Test
    public void testUpdateVehicle_Success() {
        Long vehicleId = 1L;
        VehicleDTO vehicleDTO = mockVehicleDto(1L , "Toyota", "200hp");

        Vehicle existingVehicle = mockVehicle(1L , "Honda", "150hp");


        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(existingVehicle));
        when(vehicleRepository.save(any(Vehicle.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Vehicle updatedVehicle = vehicleService.updateVehicle(vehicleId, vehicleDTO);

        assertEquals(vehicleDTO.getBrand(), updatedVehicle.getBrand());
        assertEquals(vehicleDTO.getModel(), updatedVehicle.getModel());


        verify(vehicleRepository, times(1)).findById(vehicleId);
        verify(vehicleRepository, times(1)).save(existingVehicle);
    }

    @Test
    void testUpdateVehicleWhenVehicleNotFound() {
        Long vehicleId = 1L;
        VehicleDTO vehicleDTO = mockVehicleDto(1L , "Toyota", "4C");

        Mockito.when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> vehicleService.updateVehicle(vehicleId, vehicleDTO));

        assertEquals("Vehicle not found with ID: " + vehicleId, exception.getMessage());

        verify(vehicleRepository, times(1)).findById(vehicleId);
        verify(vehicleRepository, never()).save(any(Vehicle.class));
    }


    @Test
    void testDeleteVehicleById() {
        Long vehicleId = 1L;
        vehicleService.deleteVehicleById(vehicleId);
        verify(vehicleRepository).deleteById(vehicleId);
    }



}