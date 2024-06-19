package com.sofrecom.authentificationms.repository;

import com.sofrecom.authentificationms.entity.Vehicle;
import com.sofrecom.authentificationms.entity.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle , Long> {

    /*@Query("SELECT v FROM Vehicle v WHERE v.collaborator.idCollaborator = :collaboratorId")
    List<Vehicle> findByCollaboratorId(Integer collaboratorId);
*/

    List<Vehicle> findByVehicleTypeAndBrandStartingWithIgnoreCase(VehicleType vehicleType, String brandPrefix);
    List<Vehicle> findByVehicleTypeAndBrandAndModelStartingWithIgnoreCase(VehicleType vehicleType, String brand, String modelPrefix);

}
