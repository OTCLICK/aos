package org.example.antiplagiarism.entity;

import jakarta.persistence.*;
import org.example.antiplagiarism.entity.enums.UserRole;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "users")
public class User extends BaseEntity {

    private String email;

    private String passwordHash;

    private UserRole role;

    private String fullName;

    protected User() {}

    public User(String email, String passwordHash, UserRole role, String fullName) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.fullName = fullName;
    }

    @Column(name = "email", nullable = false, unique = true)
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Column(name = "password", nullable = false)
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false)
    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }

    @Column(name = "full_name", nullable = false)
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

}
