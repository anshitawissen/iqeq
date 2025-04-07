package com.iqeq.model;

import com.iqeq.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
    @Column(name = "userId", unique = true, nullable = false)
    private Long userId;

    @NotEmpty(message = "First name cannot be empty")
    @Size(max = 20, message = "First name should be less than {max} characters")
    private String firstName;

    @NotEmpty(message = "Last name cannot be empty")
    @Size(max = 20, message = "Last name should be less than {max} characters")
    private String lastName;

    @Size(max = 50, message = "Work email should be less than {max} characters")
    @Column(name = "workEmail", unique = true)
    private String workEmail;

    @Column(name = "displayName")
    private String displayName;

    @Column(name = "imageUrl")
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "isActiveDirectoryUser")
    private Boolean isActiveDirectoryUser;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return workEmail;
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