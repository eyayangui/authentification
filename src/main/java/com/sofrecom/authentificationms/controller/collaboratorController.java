package com.sofrecom.authentificationms.controller;

import com.sofrecom.authentificationms.dto.CollaboratorDTO;
import com.sofrecom.authentificationms.entity.Collaborator;
import com.sofrecom.authentificationms.entity.Role;
import com.sofrecom.authentificationms.mapper.CollaboratorMapper;
import com.sofrecom.authentificationms.service.CollaboratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/collaborators")
public class collaboratorController {

    @Autowired
    private CollaboratorService collaboratorService ;

    @Autowired
    private CollaboratorMapper collaboratorMapper;



    @GetMapping("/by-role")
    public List<CollaboratorDTO> getCollaboratorsByRole(@RequestParam Role role) {
        return collaboratorService.listCollaborators(role);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CollaboratorDTO> getCollaboratorById(@PathVariable Integer id) {
        Collaborator collaborator = collaboratorService.findCollaboratorById(id);

        if (collaborator != null) {
            CollaboratorDTO collaboratorDTO = collaboratorMapper.fromCollaborator(collaborator);
            return ResponseEntity.ok(collaboratorDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/collaborators-page")
    public Page<CollaboratorDTO> getCollaboratorsByPage(@RequestParam(defaultValue = "1") int page)
    {
        final int size = 6 ;
        return collaboratorService.getCollaboratorsByPage(page, size);
    }

    @GetMapping("/collaborators/{id}/visibility-kpi")
    public boolean getVisibilityKpi(@PathVariable Integer id) {
        Collaborator collaborator = collaboratorService.findById(id);
        return collaborator != null && collaborator.visibilityKpi();
    }

    @PutMapping("/collaborators/{id}")
    public CollaboratorDTO updateCollaborator(@PathVariable Integer id, @RequestBody CollaboratorDTO updatedCollaborator) {
        return collaboratorService.updateCollaborator(id, updatedCollaborator);
    }

    @PatchMapping("/attribute/{id}")
    public ResponseEntity<CollaboratorDTO> updateCollaboratorAttribute(@PathVariable Integer id, @RequestBody Map<String, String> updates) {
        String attributeName = updates.get("attributeName");
        String attributeValue = updates.get("attributeValue");
        return collaboratorService.updateCollaboratorAttribute(id, attributeName, attributeValue);
    }


    @GetMapping("/{id}/email")
    public String getCollaboratorEmailById(@PathVariable Integer id) {
        return collaboratorService.getCollaboratorEmailById(id)
                .orElse(null);
    }

    @PutMapping("/{id}/image")
    public ResponseEntity<Collaborator> updateCollaboratorImage(
            @PathVariable Integer id,
            @RequestParam("file") MultipartFile file) {
        System.out.println("updateCollaboratorImage called with id: " + id);
        try {
            Collaborator updatedCollaborator = collaboratorService.storeFile(id, file);
            System.out.println("Collaborator image updated successfully");
            return ResponseEntity.ok(updatedCollaborator);
        } catch (NoSuchElementException e) {
            System.out.println("Collaborator not found with id: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (RuntimeException e) {
            System.out.println("Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }



    @GetMapping("/{id}/display-image")
    public ResponseEntity<byte[]> getCollaboratorImage(@PathVariable Integer id) {
        try {
            Collaborator collaborator = collaboratorService.findCollaboratorById(id);

            if (collaborator.getData() == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "image/png");
            headers.set("Content-Disposition", "inline; filename=\"" + collaborator.getFileName() + "\"");

            return new ResponseEntity<>(collaborator.getData(), headers, HttpStatus.OK);
        } catch (NoSuchElementException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
