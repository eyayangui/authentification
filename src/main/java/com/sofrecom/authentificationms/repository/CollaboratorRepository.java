package com.sofrecom.authentificationms.repository;

import com.sofrecom.authentificationms.entity.Collaborator;
import com.sofrecom.authentificationms.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CollaboratorRepository extends JpaRepository<Collaborator , Integer> {

    Optional<Collaborator> findByEmail(String email);
    List<Collaborator> findByRole(Role role);
    Optional<Collaborator> findById(Integer id);

}
