/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package michal.com.domain.repository;

import java.util.List;
import java.util.Optional;
import michal.com.domain.model.Customer;

/**
 *
 * @author zymci
 */
public interface ICustomerRepository {
    Customer save(Customer customer);
    Customer findById(Long id);
    List<Customer> findAll();
    void deleteById(Long id);
    void delete(Customer customer);

    Optional<Customer> findByEmail(String email);
    List<Customer> findByLastName(String lastName);
    List<Customer> findByPhoneNumber(String phoneNumber);
    List<Customer> findByFullNameContaining(String nameFragment);
}
