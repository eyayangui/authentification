package com.sofrecom.authentificationms.mapper;

import com.sofrecom.authentificationms.dto.CollaboratorDTO;
import com.sofrecom.authentificationms.entity.Collaborator;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class CollaboratorMapper {
    public CollaboratorDTO fromCollaborator(Collaborator collaborator){
        CollaboratorDTO collaboratorDTO = new CollaboratorDTO();
        BeanUtils.copyProperties(collaborator, collaboratorDTO);

        return collaboratorDTO;
    }
    public Collaborator fromCollaboratorDto(CollaboratorDTO collaboratorDTO){
        Collaborator collaborator = new Collaborator();
        BeanUtils.copyProperties(collaboratorDTO, collaborator);
        return collaborator;
    }
}
