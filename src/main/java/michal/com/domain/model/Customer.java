/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package michal.com.domain.model;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author zymci
 */
@Entity
@Table(name = "rsi_Customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_seq")
    @SequenceGenerator(name = "customer_seq", sequenceName = "customer_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String phoneNumber;

    protected Customer() {}

    private Customer( String firstName, String lastName, String email, String phoneNumber) {
        if (firstName == null || firstName.trim().isEmpty()) throw new IllegalArgumentException("First name cannot be null or empty");
        if (lastName == null || lastName.trim().isEmpty()) throw new IllegalArgumentException("Last name cannot be null or empty");

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public static Customer createCustomer(String firstName, String lastName, String email, String phoneNumber) {
        return new Customer( firstName, lastName, email, phoneNumber);
    }
    
    public Long getId() {
        return id;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String getFullName() {
        return firstName + " " + lastName;
    }
}