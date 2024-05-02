package com.sofrecom.authentificationms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
    private Integer bonus;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "collaborator", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Vehicle> vehicles;

    @OneToMany(mappedBy = "collaborator", cascade = CascadeType.REMOVE)
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
