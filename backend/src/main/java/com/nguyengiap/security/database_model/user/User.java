package com.nguyengiap.security.database_model.user;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
public class User implements UserDetails {
    @Getter
    @Id
    private String account;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String address;
    private String createDate;
    private String phoneNumber;
    @Setter
    @Getter
    private long fund;
    @Enumerated(EnumType.STRING)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return account;
    }

    @Override
    public String getPassword() {
        return password;
    }
}
