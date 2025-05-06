/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package michal.com.domain.repository;

import java.util.List;
import java.util.Optional;
import michal.com.domain.model.Movie;

/**
 *
 * @author zymci
 */
public interface IMovieRepository {
    Movie save(Movie movie);
    Movie findById(Long id);
    List<Movie> findAll();
    void deleteById(Long id);
    void delete(Movie movie);
    
    List<Movie> findByTitleContaining(String titleFragment);
    List<Movie> findByDirector(String director);
    List<Movie> findByActorName(String actorName);
    List<Movie> findByDescriptionContaining(String descriptionFragment);
}
