/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package infrastructure.repository;

import infrastructure.TransactionRetryHelper;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import michal.com.domain.model.Movie;
import michal.com.domain.repository.IMovieRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author zymci
 */
public class MovieRepository extends BaseRepository implements IMovieRepository {

    private static final Logger LOGGER = Logger.getLogger(MovieRepository.class.getName());

    public MovieRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Movie save(Movie movie) {
        return TransactionRetryHelper.executeWithRetry(() -> executeInTransaction(session -> {
            session.saveOrUpdate(movie);
            return movie;
        }));
    }

    @Override
    public Movie findById(Long id) {
        Session session = sessionFactory.openSession();
        try {
            return (Movie) session.get(Movie.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<Movie> findAll() {
        Session session = sessionFactory.openSession();
        try {
            return session.createQuery("from Movie").list();
        } finally {
            session.close();
        }
    }

    @Override
    public void deleteById(Long id) {
        Movie movie = findById(id);
        if (movie != null) {
            delete(movie);
        }
    }

    @Override
    public void delete(Movie movie) {
        TransactionRetryHelper.executeWithRetry(() -> {
            executeInTransaction(session -> {
                session.delete(movie);
                return null;
            });
            return null;
        });
    }

    @Override
    public List<Movie> findByTitleContaining(String titleFragment) {
        Session session = sessionFactory.openSession();
        try {
            return session.createQuery("from Movie where lower(title) like :titleFragment")
                    .setParameter("titleFragment", "%" + titleFragment.toLowerCase() + "%")
                    .list();
        } finally {
            session.close();
        }
    }

    @Override
    public List<Movie> findByDirector(String director) {
        Session session = sessionFactory.openSession();
        try {
            return session.createQuery("from Movie where director = :director")
                    .setParameter("director", director)
                    .list();
        } finally {
            session.close();
        }
    }

    @Override
    public List<Movie> findByActorName(String actorName) {
        Session session = sessionFactory.openSession();
        try {
            return session.createQuery("from Movie where :actorName member of actors")
                    .setParameter("actorName", actorName)
                    .list();
        } finally {
            session.close();
        }
    }

    @Override
    public List<Movie> findByDescriptionContaining(String descriptionFragment) {
        Session session = sessionFactory.openSession();
        try {
            return session.createQuery("from Movie where lower(description) like :descriptionFragment")
                    .setParameter("descriptionFragment", "%" + descriptionFragment.toLowerCase() + "%")
                    .list();
        } finally {
            session.close();
        }
    }
}
