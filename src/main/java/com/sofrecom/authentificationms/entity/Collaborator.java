package com.sofrecom.authentificationms.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Collaborator implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCollaborator;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    private String password;
    @Column(unique = true)
    private String cin;
    @Column(unique = true)
    private String phoneNumber;
    private String gender;
    private String birthDate;
    private String adress;
    private String minibio;
    private Integer bonus;
    @Column(name = "file_name")
    private String fileName;

    @Lob
    @Column(name = "data_image", columnDefinition = "LONGBLOB")
    private byte[] data;

    public Collaborator(String fileName, byte[] data) {
        this.fileName = fileName;
        this.data = data;
    }

    @Enumerated(EnumType.STRING)
    private Role role;

    /*@OneToMany(mappedBy = "collaborator", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Vehicle> vehicles;*/
    @OneToMany(mappedBy = "collaborator", cascade = CascadeType.ALL)
    @JsonBackReference
    private Set<CollaboratorVehicle> vehicles;

    @OneToMany(mappedBy = "collaborator", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Token> tokens;

    public boolean visibilityKpi() {
        return this.role == Role.ADMINISTRATOR;
    }

    public  Collaborator(String email , Role role) {
        this.email = email;
        this.role = role ;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
