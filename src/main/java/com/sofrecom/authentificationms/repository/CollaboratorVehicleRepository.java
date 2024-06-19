package com.sofrecom.authentificationms.repository;

import com.sofrecom.authentificationms.dto.VehicleDTO;
import com.sofrecom.authentificationms.entity.Collaborator;
import com.sofrecom.authentificationms.entity.CollaboratorVehicle;
import com.sofrecom.authentificationms.entity.Vehicle;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CollaboratorVehicleRepository extends JpaRepository<CollaboratorVehicle, Long> {

    @Query("SELECT new com.sofrecom.authentificationms.dto.VehicleDTO(v.idVehicule, v.brand, v.model, v.vehicleType) " +
            "FROM CollaboratorVehicle cv " +
            "JOIN cv.vehicle v " +
            "WHERE cv.collaborator.idCollaborator = :collaboratorId")
    List<VehicleDTO> findVehicleDTOsByCollaboratorId(@Param("collaboratorId") Integer collaboratorId);


    @Modifying
    @Transactional
    @Query("DELETE FROM CollaboratorVehicle cv WHERE cv.vehicle.idVehicule = :vehicleId AND cv.collaborator.idCollaborator = :collaboratorId")
    void deleteByVehicleIdAndCollaboratorId(@Param("vehicleId") Long vehicleId, @Param("collaboratorId") Long collaboratorId);

    Optional<CollaboratorVehicle> findByCollaboratorAndVehicle(Collaborator collaborator, Vehicle vehicle);

    @Query("SELECT cv FROM CollaboratorVehicle cv WHERE cv.collaborator.idCollaborator = :collaboratorId AND cv.vehicle.idVehicule = :vehicleId")
    Optional<CollaboratorVehicle> findByCollaboratorIdAndVehicleId(@Param("collaboratorId") Integer collaboratorId, @Param("vehicleId") Long vehicleId);

}
