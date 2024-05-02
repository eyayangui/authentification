package com.sofrecom.authentificationms.service;

import com.sofrecom.authentificationms.dto.CollaboratorDTO;
import com.sofrecom.authentificationms.entity.Collaborator;
import com.sofrecom.authentificationms.entity.Role;
import com.sofrecom.authentificationms.mapper.CollaboratorMapper;
import com.sofrecom.authentificationms.repository.CollaboratorRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class CollaboratorService {

    private CollaboratorRepository collaboratorRepository;
    private CollaboratorMapper collaboratorMapper;

    public List<CollaboratorDTO> listCollaborators(Role role) {
        List<Collaborator> collaborators = collaboratorRepository.findByRole(role);
        List<CollaboratorDTO> collaboratorDTOS = collaborators.stream()
                .map(user -> collaboratorMapper.fromCollaborator(user))
                .collect(Collectors.toList());
        return collaboratorDTOS;
    }

    public Page<CollaboratorDTO> getCollaboratorsByPage(int page, int size) {
        Page<Collaborator> collaborators = collaboratorRepository.findAll(PageRequest.of(page, size));
        return collaborators.map(collaboratorMapper::fromCollaborator);
    }

    public Collaborator findCollaboratorById(Integer id) {
        Optional<Collaborator> collaborator = collaboratorRepository.findById(id);
        return collaborator.orElse(null);
    }

    public Collaborator findById(Integer id) {
        return collaboratorRepository.findById(id).orElse(null);
    }

    public Collaborator updateCollaborator(Integer id, CollaboratorDTO updatedCollaboratorDto) {
        Collaborator existingCollaborator = collaboratorRepository.findById(id).orElse(null);
        if (existingCollaborator != null) {
            BeanUtils.copyProperties(updatedCollaboratorDto, existingCollaborator);
            return collaboratorRepository.save(existingCollaborator);
        }
        return null;
    }

    public Optional<String> getCollaboratorEmailById(Integer id) {
        return collaboratorRepository.findById(id)
                .map(Collaborator::getEmail);
    }

}
