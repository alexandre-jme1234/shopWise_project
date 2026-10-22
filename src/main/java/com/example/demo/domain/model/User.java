package com.example.demo.domain.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "`User`")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, length = 100)
    public String name;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "phone", length = 20)
    public String phone;

    @Column(name = "password_hash", nullable = false)
    private String password_hash;

    // Rempli par la base (DEFAULT CURRENT_TIMESTAMP)
    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime created_at;

    public User() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public abstract String getPassword();

    public String getPhone() {
        return phone;
    }

    public abstract void setPassword(String password);

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword_hash() {
        return password_hash;
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }
}
