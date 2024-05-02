package com.sofrecom.authentificationms.service;

import com.sofrecom.authentificationms.dto.CollaboratorDTO;
import com.sofrecom.authentificationms.entity.Collaborator;
import com.sofrecom.authentificationms.entity.Role;
import com.sofrecom.authentificationms.mapper.CollaboratorMapper;
import com.sofrecom.authentificationms.repository.CollaboratorRepository;
import com.sofrecom.authentificationms.repository.VehicleRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CollaboratorServiceTest {

    @InjectMocks
    private CollaboratorService collaboratorService;
    @Mock
    private CollaboratorRepository collaboratorRepository;
    @Mock
    private CollaboratorMapper collaboratorMapper;

    @BeforeEach
    public void setUp() {
        collaboratorRepository = mock(CollaboratorRepository.class);
        collaboratorMapper = new CollaboratorMapper();
        collaboratorService = new CollaboratorService(collaboratorRepository, collaboratorMapper);
    }

    @Test
    public void testListCollaborators() {

        Role role = Role.USER;
        Collaborator collaborator1 = new Collaborator("John@gmail.com", role);
        Collaborator collaborator2 = new Collaborator("Alice@gmail.com", role);
        List<Collaborator> mockCollaborators = Arrays.asList(collaborator1, collaborator2);

        when(collaboratorRepository.findByRole(role)).thenReturn(mockCollaborators);

        List<CollaboratorDTO> result = collaboratorService.listCollaborators(role);

        assertEquals(2, result.size());
        assertEquals("John@gmail.com", result.get(0).getEmail());
        assertEquals("Alice@gmail.com", result.get(1).getEmail());

    }

    @Test
    public void testFindExistingCollaboratorById() {
        int id = 1;
        Collaborator mockCollaborator = new Collaborator("John", Role.ADMINISTRATOR);
        Optional<Collaborator> optionalCollaborator = Optional.of(mockCollaborator);
        when(collaboratorRepository.findById(id)).thenReturn(optionalCollaborator);
        Collaborator result = collaboratorService.findCollaboratorById(id);
        assertEquals(mockCollaborator, result);
    }

    @Test
    public void testFindNonExistingCollaboratorById() {
        int id = 999;
        Optional<Collaborator> optionalCollaborator = Optional.empty();
        when(collaboratorRepository.findById(id)).thenReturn(optionalCollaborator);
        Collaborator result = collaboratorService.findCollaboratorById(id);
        assertNull(result);
    }

    @Test
    public void testUpdateCollaborator() {
        int id = 1;
        Collaborator existingCollaborator = new Collaborator("John", Role.ADMINISTRATOR);
        CollaboratorDTO updatedCollaboratorDto = new CollaboratorDTO("Updated Name", Role.USER);
        Collaborator updatedCollaborator = new Collaborator("Updated Name", Role.USER);

        when(collaboratorRepository.findById(id)).thenReturn(Optional.of(existingCollaborator));
        when(collaboratorRepository.save(existingCollaborator)).thenReturn(updatedCollaborator);
        Collaborator result1 = collaboratorService.updateCollaborator(id, updatedCollaboratorDto);
        assertEquals(updatedCollaborator, result1);

        int nonExistingId = 999;
        when(collaboratorRepository.findById(nonExistingId)).thenReturn(Optional.empty());
        Collaborator result2 = collaboratorService.updateCollaborator(nonExistingId, updatedCollaboratorDto);
        assertNull(result2);
    }

}