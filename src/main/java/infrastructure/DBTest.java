/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package infrastructure;

import michal.com.domain.model.Customer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 *
 * @author zymci
 */
public class DBTest {
    public static void main(String[] args) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction t = session.beginTransaction();

        Customer customer = Customer.createCustomer(
            "Michal", "Nowak", "michal.nowak@example.com", "123456789"
        );

        session.save(customer);
        t.commit();
        session.close();
        sessionFactory.close();

        System.out.println("Klient zapisany pomyślnie!");
    }
}