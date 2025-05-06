/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package infrastructure.repository;

import infrastructure.TransactionRetryHelper;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import michal.com.domain.model.Customer;
import michal.com.domain.repository.ICustomerRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author zymci
 */
public class CustomerRepository extends BaseRepository implements ICustomerRepository {

    private static final Logger LOGGER = Logger.getLogger(CustomerRepository.class.getName());

    public CustomerRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Customer save(Customer customer) {
        return TransactionRetryHelper.executeWithRetry(() -> executeInTransaction(session -> {
            session.saveOrUpdate(customer);
            return customer;
        }));
    }

    @Override
    public Customer findById(Long id) {
        Session session = sessionFactory.openSession();
        try {
            return (Customer) session.get(Customer.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<Customer> findAll() {
        Session session = sessionFactory.openSession();
        try {
            return session.createQuery("from Customer").list();
        } finally {
            session.close();
        }
    }

    @Override
    public void deleteById(Long id) {
        Customer customer = findById(id);
        if (customer != null) {
            delete(customer);
        }
    }

    @Override
    public void delete(Customer customer) {
        TransactionRetryHelper.executeWithRetry(() -> {
            executeInTransaction(session -> {
                session.delete(customer);
                return null;
            });
            return null;
        });
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        Session session = sessionFactory.openSession();
        try {
            Customer customer = (Customer) session.createQuery("from Customer where email = :email")
                    .setParameter("email", email)
                    .uniqueResult();
            return Optional.ofNullable(customer);
        } finally {
            session.close();
        }
    }

    @Override
    public List<Customer> findByLastName(String lastName) {
        Session session = sessionFactory.openSession();
        try {
            return session.createQuery("from Customer where lastName = :lastName")
                    .setParameter("lastName", lastName)
                    .list();
        } finally {
            session.close();
        }
    }

    @Override
    public List<Customer> findByPhoneNumber(String phoneNumber) {
        Session session = sessionFactory.openSession();
        try {
            return session.createQuery("from Customer where phoneNumber = :phoneNumber")
                    .setParameter("phoneNumber", phoneNumber)
                    .list();
        } finally {
            session.close();
        }
    }

    @Override
    public List<Customer> findByFullNameContaining(String nameFragment) {
        Session session = sessionFactory.openSession();
        try {
            return session.createQuery("from Customer where lower(firstName) like :nameFragment or lower(lastName) like :nameFragment")
                    .setParameter("nameFragment", "%" + nameFragment.toLowerCase() + "%")
                    .list();
        } finally {
            session.close();
        }
    }
}