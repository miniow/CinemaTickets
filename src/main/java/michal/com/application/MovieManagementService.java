/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package michal.com.application;

import infrastructure.repository.MovieRepository;
import java.awt.Image;

import java.util.List;
import michal.com.domain.model.Movie;

/**
 *
 * @author zymci
 */
public class MovieManagementService {
    private final MovieRepository movieRepository;
    
    public MovieManagementService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }
    public Long addNewMovie(String title, String director, List<String> actors, String description, Image thumbnail) {
        Movie movie = Movie.createMovie(title, director, actors, description, thumbnail);
        movieRepository.save(movie);
        return movie.getId();
    }
    
    public void updateMovieThumbnail(Long movieId, Image newThumbnail) {
        Movie movie = movieRepository.findById(movieId);
        if (movie == null) {
            throw new IllegalArgumentException("Movie not found");
        }
        Movie updatedMovie = Movie.createMovie(
            movie.getTitle(),
            movie.getDirector(),
            movie.getActors(),
            movie.getDescription(),
            newThumbnail
        );
        movieRepository.save(updatedMovie);
    }
    public Movie addMovie(Movie newMovie)
    {
       return movieRepository.save(newMovie);
    }
}
