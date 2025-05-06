/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package infrastructure.repository;

import infrastructure.TransactionRetryHelper;
import java.util.List;
import java.util.Optional;
import michal.com.domain.model.Seat;
import michal.com.domain.repository.ISeatRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author zymci
 */
public class SeatRepository extends BaseRepository implements ISeatRepository {

    public SeatRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Seat save(Seat seat) {
        return TransactionRetryHelper.executeWithRetry(() -> executeInTransaction(session -> {
            session.saveOrUpdate(seat);
            return seat;
        }));
    }

    @Override
    public Seat findById(Long id) {
        Session session = sessionFactory.openSession();
        try {
            return (Seat) session.get(Seat.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<Seat> findAll() {
        Session session = sessionFactory.openSession();
        try {
            return session.createQuery("from Seat").list();
        } finally {
            session.close();
        }
    }

    @Override
    public void deleteById(Long id) {
        Seat seat = findById(id);
        if (seat != null) {
            delete(seat);
        }
    }

    @Override
    public void delete(Seat seat) {
        TransactionRetryHelper.executeWithRetry(() -> {
            executeInTransaction(session -> {
                session.delete(seat);
                return null;
            });
            return null;
        });
    }
    
    @Override
    public List<Seat> findByRoomId(Long roomId) {
        Session session = sessionFactory.openSession();
        try {
            return session.createQuery("from Seat where cinemaRoom.id = :roomId")
                    .setParameter("roomId", roomId)
                    .list();
        } finally {
            session.close();
        }
    }

    @Override
    public List<Seat> findByRoomIdAndAvailability(Long roomId, boolean available) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Seat> findByReservationId(Long reservationId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Seat> findByScreeningId(Long screeningId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Seat> findByScreeningIdAndAvailability(Long screeningId, boolean available) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}

