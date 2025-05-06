/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package infrastructure.repository;

import infrastructure.TransactionRetryHelper;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import michal.com.domain.model.CinemaRoom;
import michal.com.domain.repository.ICinemaRoomRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author zymci
 */
public class CinemaRoomRepository extends BaseRepository implements ICinemaRoomRepository {

    private static final Logger LOGGER = Logger.getLogger(CinemaRoomRepository.class.getName());

    public CinemaRoomRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

   @Override
    public CinemaRoom save(CinemaRoom room) {
        return TransactionRetryHelper.executeWithRetry(() -> executeInTransaction(session -> {
            session.saveOrUpdate(room);
            return room;
        }));
    }

    @Override
    public CinemaRoom findById(Long id) {
        Session session = sessionFactory.openSession();
        try {
            return (CinemaRoom) session.get(CinemaRoom.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<CinemaRoom> findAll() {
        Session session = sessionFactory.openSession();
        try {
            return session.createQuery("from CinemaRoom").list();
        } finally {
            session.close();
        }
    }

    @Override
    public void deleteById(Long id) {
        CinemaRoom room = findById(id);
        if (room != null) {
            delete(room);
        }
    }

    @Override
    public void delete(CinemaRoom room) {
        TransactionRetryHelper.executeWithRetry(() -> {
            executeInTransaction(session -> {
                session.delete(room);
                return null;
            });
            return null;
        });
    }

    @Override
    public List<CinemaRoom> findByNameContaining(String nameFragment) {
        Session session = sessionFactory.openSession();
        try {
            return session.createQuery("from CinemaRoom where lower(name) like :fragment")
                    .setParameter("fragment", "%" + nameFragment.toLowerCase() + "%")
                    .list();
        } finally {
            session.close();
        }
    }

    @Override
    public List<CinemaRoom> findByCapacityGreaterThan(int minCapacity) {
        Session session = sessionFactory.openSession();
        try {
            return session.createQuery("select r from CinemaRoom r where size(r.seats) > :minCap")
                    .setParameter("minCap", minCapacity)
                    .list();
        } finally {
            session.close();
        }
    }

    @Override
    public List<CinemaRoom> findByCapacityLessThan(int maxCapacity) {
        Session session = sessionFactory.openSession();
        try {
            return session.createQuery("select r from CinemaRoom r where size(r.seats) < :maxCap")
                    .setParameter("maxCap", maxCapacity)
                    .list();
        } finally {
            session.close();
        }
    }
}