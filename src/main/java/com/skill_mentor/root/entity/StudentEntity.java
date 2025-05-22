package com.skill_mentor.root.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "student")
public @Data class StudentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer studentId;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "phone_no")
    private String phoneNumber;
    @Column(name = "address")
    private String address;
    @Column(name = "age")
    private Integer age;

    public StudentEntity(Integer studentId, String firstName, String lastName, String email, String phoneNumber, String address, Integer age) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.age = age;
    }

    public StudentEntity() {

    }
}
