package com.sofrecom.authentificationms.repository;

import com.sofrecom.authentificationms.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle , Long> {

    @Query("SELECT v FROM Vehicle v WHERE v.collaborator.idCollaborator = :collaboratorId")
    List<Vehicle> findByCollaboratorId(Integer collaboratorId);

}
