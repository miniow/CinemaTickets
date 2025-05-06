/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package michal.com.application;

import infrastructure.repository.UserRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import michal.com.domain.model.Customer;
import michal.com.domain.model.User;

import michal.com.ws.Database;

/**
 *
 * @author zymci
 */
public class AuthenticationService {
    private final UserRepository userRepository;

    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean validateCredentials(String username, String password) {
        User user = userRepository.findByUsername(username);
        return user != null && user.getPassword().equals(password);
    }

    public boolean isAdmin(String username) {
        User user = userRepository.findByUsername(username);
        return user != null && user.isAdmin();
    }

    public void registerUser(String username, String password, Customer customer) {
        System.out.println("[REGISTER_USER] Registering user: " + username);

        User existingUser = userRepository.findByUsername(username);
        if (existingUser != null) {
            throw new RuntimeException("User with this username already exists.");
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setCustomer(customer);
        newUser.setIsAdmin(false);

        userRepository.save(newUser);

        System.out.println("[REGISTER_USER] User registered successfully: " + username);
    }

    public Long getCustomerId(String username) {
        User user = userRepository.findByUsername(username);
        return user != null && user.getCustomer() != null ? user.getCustomer().getId() : null;
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}