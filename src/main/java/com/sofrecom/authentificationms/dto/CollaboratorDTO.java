package com.sofrecom.authentificationms.dto;

import com.sofrecom.authentificationms.entity.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollaboratorDTO {

    private Integer idCollaborator;
    private String firstName;
    private String lastName;
    private String email;
    private String cin;
    private String phoneNumber;
    private String gender;
    private String birthDate;
    private String adress;
    private Integer bonus;
    private Role role ;
    private String minibio;
    /*private String fileName;
    private byte[] data;*/

    public  CollaboratorDTO(String email , Role role) {
        this.email = email;
        this.role = role ;
    }
    /*public CollaboratorDTO(String fileName, byte[] data) {
        this.fileName = fileName;
        this.data = data;
    }*/
}
