package com.sofrecom.authentificationms.service;

import com.sofrecom.authentificationms.dto.CollaboratorDTO;
import com.sofrecom.authentificationms.entity.Collaborator;
import com.sofrecom.authentificationms.entity.Role;
import com.sofrecom.authentificationms.mapper.CollaboratorMapper;
import com.sofrecom.authentificationms.repository.CollaboratorRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class CollaboratorService {

    private final Path fileStorageLocation;
    private final CollaboratorRepository collaboratorRepository;

    @Autowired
    private CollaboratorMapper collaboratorMapper;

    private static final Logger log = LoggerFactory.getLogger(CollaboratorService.class);
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

    public CollaboratorDTO updateCollaborator(Integer id, CollaboratorDTO updatedCollaboratorDto) {
        Collaborator existingCollaborator = collaboratorRepository.findById(id).orElse(null);
        if (existingCollaborator != null) {
            BeanUtils.copyProperties(updatedCollaboratorDto, existingCollaborator);
            Collaborator savedCollaborator = collaboratorRepository.save(existingCollaborator);
            return convertToDTO(savedCollaborator);
        }
        return null;
    }




    private CollaboratorDTO convertToDTO(Collaborator collaborator) {
        CollaboratorDTO collaboratorDTO = new CollaboratorDTO();
        BeanUtils.copyProperties(collaborator, collaboratorDTO);
        return collaboratorDTO;
    }

    public Optional<String> getCollaboratorEmailById(Integer id) {
        return collaboratorRepository.findById(id)
                .map(Collaborator::getEmail);
    }

    @Autowired
    public CollaboratorService(@Value("${file.upload-dir}") String uploadDir, CollaboratorRepository collaboratorRepository) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        this.collaboratorRepository = collaboratorRepository;
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }



    public Collaborator storeFile(Integer collaboratorId, MultipartFile file) {
        String fileName = file.getOriginalFilename();
        System.out.println("storeFile called with collaboratorId: " + collaboratorId + ", fileName: " + fileName);

        try {
            if (fileName.contains("..")) {
                throw new RuntimeException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Save file to the file system
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File saved to file system at: " + targetLocation.toString());

            // Find the collaborator
            Optional<Collaborator> optionalCollaborator = collaboratorRepository.findById(collaboratorId);
            if (optionalCollaborator.isPresent()) {
                Collaborator collaborator = optionalCollaborator.get();
                // Update the collaborator with the new file
                collaborator.setFileName(fileName);
                collaborator.setData(file.getBytes());
                Collaborator savedCollaborator = collaboratorRepository.save(collaborator);
                System.out.println("Collaborator updated and saved with new file: " + savedCollaborator.getIdCollaborator());
                return savedCollaborator;
            } else {
                throw new NoSuchElementException("Collaborator not found with id " + collaboratorId);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
        } catch (Exception ex) {
            throw new RuntimeException("An unexpected error occurred while storing the file: " + ex.getMessage(), ex);
        }
    }

    public ResponseEntity<CollaboratorDTO> updateCollaboratorAttribute(Integer id, String attributeName, String attributeValue) {
        Collaborator existingCollaborator = collaboratorRepository.findById(id).orElse(null);
        if (existingCollaborator != null) {
            switch(attributeName) {
                case "phoneNumber":
                    existingCollaborator.setPhoneNumber(attributeValue);
                    break;
                case "birthDate":
                    existingCollaborator.setBirthDate(attributeValue);
                    break;
                case "address":
                    existingCollaborator.setAdress(attributeValue);
                    break;
                case "minibio":
                    existingCollaborator.setMinibio(attributeValue);
                    break;
                default:
                    break;
            }
            Collaborator savedCollaborator = collaboratorRepository.save(existingCollaborator);
            CollaboratorDTO collaboratorDTO = collaboratorMapper.fromCollaborator(savedCollaborator);
            return ResponseEntity.ok(collaboratorDTO);
        }
        return ResponseEntity.notFound().build();
    }

}
