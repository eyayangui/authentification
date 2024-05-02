package com.sofrecom.authentificationms.controller;

import com.sofrecom.authentificationms.dto.CollaboratorDTO;
import com.sofrecom.authentificationms.entity.Collaborator;
import com.sofrecom.authentificationms.entity.Role;
import com.sofrecom.authentificationms.mapper.CollaboratorMapper;
import com.sofrecom.authentificationms.service.CollaboratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public Collaborator updateCollaborator(@PathVariable Integer id, @RequestBody CollaboratorDTO updatedCollaborator) {
        return collaboratorService.updateCollaborator(id, updatedCollaborator);
    }

    @GetMapping("/{id}/email")
    public String getCollaboratorEmailById(@PathVariable Integer id) {
        return collaboratorService.getCollaboratorEmailById(id)
                .orElse(null); // or any default value you want to return
    }

}
