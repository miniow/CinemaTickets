/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package michal.com.application;

import infrastructure.repository.MovieRepository;
import infrastructure.repository.ScreeningRepository;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import michal.com.domain.model.Movie;
import michal.com.domain.model.Screening;
import michal.com.domain.model.Seat;

/**
 *
 * @author zymci
 */
public class MovieQueryService {
     private final MovieRepository movieRepository;
    private final ScreeningRepository screeningRepository;
    
    public MovieQueryService(MovieRepository movieRepository, ScreeningRepository screeningRepository) {
        this.movieRepository = movieRepository;
        this.screeningRepository = screeningRepository;
    }
     public List<MovieWithScreenings> getCurrentMoviesWithScreenings(Date from, Date to) {
    List<Movie> movies = movieRepository.findAll();
    List<Screening> screenings = screeningRepository.findByDateBetween(from, to);

    return movies.stream()
        .map(movie -> {
            List<Screening> movieScreenings = screenings.stream()
                .filter(s -> s.getMovie().getId().equals(movie.getId()))
                .collect(Collectors.toList());
            return new MovieWithScreenings(movie, movieScreenings);
        })
        .filter(mws -> !mws.getScreenings().isEmpty())
        .collect(Collectors.toList());
}
    public List<Screening> getScreeningsForMovie(Long movieId, Date from, Date to) {
    return screeningRepository.findByMovieAndDateBetween(movieId, from, to);
}
    public List<Seat> getAvailableSeatsForScreening(Long screeningId) {
        Screening screening = screeningRepository.findById(screeningId);
        if (screening == null) {
            return Collections.emptyList();
        }
        return screening.getAvailableSeats();
    }
    
    public static class MovieWithScreenings {
        private final Movie movie;
        private final List<Screening> screenings;
        
        public MovieWithScreenings(Movie movie, List<Screening> screenings) {
            this.movie = movie;
            this.screenings = screenings;
        }
        
        public Movie getMovie(){
            return movie;
        }
        public List<Screening> getScreenings(){
            return screenings;
        }
    }
}
