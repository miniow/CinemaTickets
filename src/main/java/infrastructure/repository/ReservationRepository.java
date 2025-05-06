package infrastructure.repository;

import infrastructure.TransactionRetryHelper;
import java.util.Date;
import java.util.List;
import michal.com.domain.model.Reservation;
import michal.com.domain.model.ReservationStatus;
import michal.com.domain.repository.IReservationRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;



/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author zymci
 */
public class ReservationRepository extends BaseRepository implements IReservationRepository {

    public ReservationRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Reservation save(Reservation reservation) {
        return TransactionRetryHelper.executeWithRetry(() -> executeInTransaction(session -> {
            session.saveOrUpdate(reservation);
            return reservation;
        }));
    }

    @Override
    public Reservation findById(Long id) {
        Session session = sessionFactory.openSession();
        try {
            return (Reservation) session.get(Reservation.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<Reservation> findAll() {
        Session session = sessionFactory.openSession();
        try {
            return session.createQuery("from Reservation").list();
        } finally {
            session.close();
        }
    }

    @Override
    public void deleteById(Long id) {
        Reservation reservation = findById(id);
        if (reservation != null) {
            delete(reservation);
        }
    }

    @Override
    public void delete(Reservation reservation) {
        TransactionRetryHelper.executeWithRetry(() -> {
            executeInTransaction(session -> {
                session.delete(reservation);
                return null;
            });
            return null;
        });
    }

    @Override
    public List<Reservation> findByCustomerId(Long customerId) {
        Session session = sessionFactory.openSession();
        try {
            return session.createQuery("from Reservation where customer.id = :customerId").setParameter("customerId", customerId).list();
        } finally {
            session.close();
        }
    }

    @Override
    public List<Reservation> findByScreeningId(Long screeningId) {
        Session session = sessionFactory.openSession();
        try {
            return session.createQuery("from Reservation where screening.id = :screeningId").setParameter("screeningId", screeningId).list();
        } finally {
            session.close();
        }
    }

    @Override
    public List<Reservation> findByStatus(String status) {
        Session session = sessionFactory.openSession();
        try {
            return session.createQuery("from Reservation where status = :status").setParameter("status", ReservationStatus.valueOf(status)).list();
        } finally {
            session.close();
        }
    }

    @Override
    public List<Reservation> findByCustomerAndStatus(Long customerId, String status) {
        Session session = sessionFactory.openSession();
        try {
            return session.createQuery("from Reservation where customer.id = :customerId and status = :status")
                    .setParameter("customerId", customerId)
                    .setParameter("status", ReservationStatus.valueOf(status))
                    .list();
        } finally {
            session.close();
        }
    }

    @Override
    public List<Reservation> findByScreeningAndStatus(Long screeningId, String status) {
        Session session = sessionFactory.openSession();
        try {
            return session.createQuery("from Reservation where screening.id = :screeningId and status = :status")
                    .setParameter("screeningId", screeningId)
                    .setParameter("status", ReservationStatus.valueOf(status)).list();
        } finally {
            session.close();
        }
    }

    @Override
    public List<Reservation> findByDateBetween(Date startDate, Date endDate) {
        Session session = sessionFactory.openSession();
        try {
            return session.createQuery("from Reservation where screening.date between :startDate and :endDate")
                    .setParameter("startDate", startDate)
                    .setParameter("endDate", endDate)
                    .list();
        } finally {
            session.close();
        }
    }
}
