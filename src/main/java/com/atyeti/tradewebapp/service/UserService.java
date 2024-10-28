package com.atyeti.tradewebapp.service;

import com.atyeti.tradewebapp.model.User;
import com.atyeti.tradewebapp.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

    public User registerUser(User user) {

        if (userRepository.findByUsername(user.getUsername()).isPresent() ||
                userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Username or email already in use");
        }
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        user.setBalance(10000);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(new User());
    }

}
