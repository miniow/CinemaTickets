/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package infrastructure.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 *
 * @author zymci
 */
public abstract class BaseRepository {
    protected final SessionFactory sessionFactory;

    protected BaseRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected <T> T executeInTransaction(TransactionOperation<T> operation) throws Exception {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            
            T result = operation.execute(session);
            
            transaction.commit();
            return result;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new Exception("Transaction failed", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    protected void executeInTransaction(TransactionVoidOperation operation) throws Exception {
        executeInTransaction(session -> {
            operation.execute(session);
            return null;
        });
    }

    @FunctionalInterface
    protected interface TransactionOperation<T> {
        T execute(Session session);
    }

    @FunctionalInterface
    protected interface TransactionVoidOperation {
        void execute(Session session);
    }
}
