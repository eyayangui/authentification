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

    private static VehicleDTO mockVehicleDto(Long id , String brand, String power, Integer nbSeat, String plateNumber, VehicleType vehicleType) {
        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setIdVehicle(id);
        vehicleDTO.setBrand(brand);
        vehicleDTO.setPower(power);
        vehicleDTO.setNumberSeat(nbSeat);
        vehicleDTO.setPlateNumber(plateNumber);
        vehicleDTO.setVehicleType(vehicleType);
        return vehicleDTO ;
    }

    private static Vehicle mockVehicle(Long id , String brand, String power, Integer nbSeat, String plateNumber, VehicleType vehicleType) {
        Vehicle vehicle = new Vehicle();
        vehicle.setIdVehicule(id);
        vehicle.setBrand(brand);
        vehicle.setPower(power);
        vehicle.setNumberSeat(nbSeat);
        vehicle.setPlateNumber(plateNumber);
        vehicle.setVehicleType(vehicleType);
        return vehicle ;
    }

    @Test
    void testAddVehicleForCollaborator() {
        Integer collaboratorId = 1;
        VehicleDTO vehicleDTO = mockVehicleDto(1L,"Toyota", "4C", 5 , "ABC123", VehicleType.CAR );

        Collaborator collaborator = new Collaborator();
        collaborator.setIdCollaborator(collaboratorId);

        Mockito.when(collaboratorRepository.findById(collaboratorId)).thenReturn(Optional.of(collaborator));

        Vehicle savedVehicle = new Vehicle();
        BeanUtils.copyProperties(vehicleDTO, savedVehicle);
        savedVehicle.setCollaborator(collaborator);

        Mockito.when(vehicleRepository.save(any(Vehicle.class))).thenReturn(savedVehicle);

        Vehicle result = vehicleService.addVehicleForCollaborator(collaboratorId, vehicleDTO);

        assertNotNull(result);
        assertEquals(vehicleDTO.getBrand(), result.getBrand());
        assertEquals(vehicleDTO.getPower(), result.getPower());
        assertEquals(vehicleDTO.getNumberSeat(), result.getNumberSeat());
        assertEquals(vehicleDTO.getPlateNumber(), result.getPlateNumber());
        assertEquals(vehicleDTO.getVehicleType(), result.getVehicleType());
        assertEquals(collaboratorId, result.getCollaborator().getIdCollaborator());

        verify(collaboratorRepository, times(1)).findById(collaboratorId);
        verify(vehicleRepository, times(1)).save(any(Vehicle.class));
    }

    @Test
    void testAddVehicleForCollaboratorWhenCollaboratorNotFound() {
        Integer collaboratorId = 1;
        VehicleDTO vehicleDTO = mockVehicleDto(1L , "Toyota", "4C", 5 , "ABC123", VehicleType.CAR );

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
        VehicleDTO vehicleDTO = mockVehicleDto(1L , "Toyota", "200hp", 5 , "ABC123", VehicleType.CAR );

        Vehicle existingVehicle = mockVehicle(1L , "Honda", "150hp", 4 , "XYZ789", VehicleType.CAR );


        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(existingVehicle));
        when(vehicleRepository.save(any(Vehicle.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Vehicle updatedVehicle = vehicleService.updateVehicle(vehicleId, vehicleDTO);

        assertEquals(vehicleDTO.getBrand(), updatedVehicle.getBrand());
        assertEquals(vehicleDTO.getPower(), updatedVehicle.getPower());
        assertEquals(vehicleDTO.getNumberSeat(), updatedVehicle.getNumberSeat());
        assertEquals(vehicleDTO.getPlateNumber(), updatedVehicle.getPlateNumber());
        assertEquals(vehicleDTO.getVehicleType(), updatedVehicle.getVehicleType());

        verify(vehicleRepository, times(1)).findById(vehicleId);
        verify(vehicleRepository, times(1)).save(existingVehicle);
    }

    @Test
    void testUpdateVehicleWhenVehicleNotFound() {
        Long vehicleId = 1L;
        VehicleDTO vehicleDTO = mockVehicleDto(1L , "Toyota", "4C", 5 , "ABC123", VehicleType.CAR );

        Mockito.when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> vehicleService.updateVehicle(vehicleId, vehicleDTO));

        assertEquals("Vehicle not found with ID: " + vehicleId, exception.getMessage());

        verify(vehicleRepository, times(1)).findById(vehicleId);
        verify(vehicleRepository, never()).save(any(Vehicle.class));
    }

    @Test
    void testGetAllVehicleDTOsForCollaborator() {
        Integer collaboratorId = 1;

        Vehicle vehicle1 = mockVehicle(1L , "Toyota", "4C", 5 , "ABC123", VehicleType.CAR );
        Vehicle vehicle2 = mockVehicle(2L , "Honda", "120hp", 4 , "XYZ456", VehicleType.CAR );

        when(vehicleRepository.findByCollaboratorId(collaboratorId)).thenReturn(Arrays.asList(vehicle1, vehicle2));

        VehicleDTO vehicleDTO1 = mockVehicleDto(1L , "Toyota", "4C", 5 , "ABC123", VehicleType.CAR );
        VehicleDTO vehicleDTO2 = mockVehicleDto(2L , "Honda", "120hp", 5 , "ABC123", VehicleType.CAR );

        when(vehicleMapper.mapVehicleToDTO(vehicle1)).thenReturn(vehicleDTO1);
        when(vehicleMapper.mapVehicleToDTO(vehicle2)).thenReturn(vehicleDTO2);

        List<VehicleDTO> result = vehicleService.getAllVehicleDTOsForCollaborator(collaboratorId);

        assertEquals(2, result.size());
        assertEquals(vehicleDTO1, result.get(0));
        assertEquals(vehicleDTO2, result.get(1));

        verify(vehicleRepository).findByCollaboratorId(collaboratorId);
    }

    @Test
    void testDeleteVehicleById() {
        Long vehicleId = 1L;
        vehicleService.deleteVehicleById(vehicleId);
        verify(vehicleRepository).deleteById(vehicleId);
    }



}