package com.atyeti.tradewebapp.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Transient
    private String confirmPassword;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String role="ROLE_USER";

    @Column(nullable = false)
    private double balance;

}

