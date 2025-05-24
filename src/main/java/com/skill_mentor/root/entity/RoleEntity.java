package com.skill_mentor.root.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
public class RoleEntity {

    @Id
    private Integer id; // manually assigned

    @Column(nullable = false, unique = true)
    private String role;

    public RoleEntity(Integer id, String role) {
        this.id = id;
        this.role = role;
    }
}
