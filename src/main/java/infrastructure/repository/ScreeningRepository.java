/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package infrastructure.repository;

import infrastructure.TransactionRetryHelper;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import michal.com.domain.model.Screening;
import michal.com.domain.repository.IScreeningRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author zymci
 */
public class ScreeningRepository extends BaseRepository implements IScreeningRepository {

    public ScreeningRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Screening save(Screening screening) {
        return TransactionRetryHelper.executeWithRetry(() -> executeInTransaction(session -> {
            session.saveOrUpdate(screening);
            return screening;
        }));
    }

    @Override
    public Screening findById(Long id) {
        Session session = sessionFactory.openSession();
        try {
            return (Screening) session.get(Screening.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<Screening> findAll() {
        Session session = sessionFactory.openSession();
        try {
            return session.createQuery("from Screening").list();
        } finally {
            session.close();
        }
    }

    @Override
    public void deleteById(Long id) {
        Screening screening = findById(id);
        if (screening != null) {
            delete(screening);
        }
    }

    @Override
    public void delete(Screening screening) {
        TransactionRetryHelper.executeWithRetry(() -> {
            executeInTransaction(session -> {
                session.delete(screening);
                return null;
            });
            return null;
        });
    }

    @Override
    public List<Screening> findByMovieId(Long movieId) {
        Session session = sessionFactory.openSession();
        try {
            return session.createQuery("from Screening where movie.id = :movieId").setParameter("movieId", movieId).list();
        } finally {
            session.close();
        }
    }

    @Override
    public List<Screening> findByRoomId(Long roomId) {
        Session session = sessionFactory.openSession();
        try {
            return session.createQuery("from Screening where room.id = :roomId").setParameter("roomId", roomId).list();
        } finally {
            session.close();
        }
    }

    @Override
    public List<Screening> findByDateBetween(Date startDate, Date endDate) {
        Session session = sessionFactory.openSession();
        try {
            return session.createQuery("from Screening where screeningDate between :startDate and :endDate").setParameter("startDate", startDate).setParameter("endDate", endDate).list();
        } finally {
            session.close();
        }
    }

    @Override
    public List<Screening> findByMovieAndDateBetween(Long movieId, Date startDate, Date endDate) {
        Session session = sessionFactory.openSession();
        try {
            return  session.createQuery("from Screening where movie.id = :movieId and screeningDate between :startDate and :endDate")
                    .setParameter("movieId", movieId).setParameter("startDate", startDate).setParameter("endDate", endDate).list();
        } finally {
            session.close();
        }
    }

    @Override
    public List<Screening> findByRoomAndDateBetween(Long roomId, Date startDate, Date endDate) {
        Session session = sessionFactory.openSession();
        try {
            return session.createQuery("from Screening where room.id = :roomId and screeningDate between :startDate and :endDate").setParameter("roomId", roomId).setParameter("startDate", startDate).setParameter("endDate", endDate).list();
        } finally {
            session.close();
        }
    }
}