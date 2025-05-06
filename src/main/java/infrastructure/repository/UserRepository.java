/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package infrastructure.repository;

import michal.com.domain.model.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 *
 * @author zymci
 */
public class UserRepository {
    private final SessionFactory sessionFactory;

    public UserRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public User findByUsername(String username) {
        Session session = sessionFactory.openSession();
        try {
            String hql = "FROM User WHERE username = :username";
            return (User) session.createQuery(hql)
                               .setParameter("username", username)
                               .uniqueResult();
        } finally {
            session.close();
        }
    }

    public void save(User user) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.saveOrUpdate(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Failed to save user", e);
        } finally {
            session.close();
        }
    }

    public boolean existsByUsername(String username) {
        Session session = sessionFactory.openSession();
        try {
            String hql = "SELECT count(*) FROM User WHERE username = :username";
            Long count = (Long) session.createQuery(hql)
                                    .setParameter("username", username)
                                    .uniqueResult();
            return count != null && count > 0;
        } finally {
            session.close();
        }
    }

    public User findWithCustomer(String username) {
        Session session = sessionFactory.openSession();
        try {
            String hql = "FROM User u LEFT JOIN FETCH u.customer WHERE u.username = :username";
            return (User) session.createQuery(hql)
                               .setParameter("username", username)
                               .uniqueResult();
        } finally {
            session.close();
        }
    }
}
