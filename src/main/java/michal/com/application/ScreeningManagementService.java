/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package michal.com.application;

import infrastructure.repository.CinemaRoomRepository;
import infrastructure.repository.MovieRepository;
import infrastructure.repository.ScreeningRepository;
import java.util.Date;
import michal.com.domain.model.CinemaRoom;
import michal.com.domain.model.Movie;
import michal.com.domain.model.Screening;

/**
 *
 * @author zymci
 */
public class ScreeningManagementService {
    private final ScreeningRepository screeningRepository;
    private final MovieRepository movieRepository;
    private final CinemaRoomRepository cinemaRoomRepository;
    
    public ScreeningManagementService(ScreeningRepository screeningRepository, 
                                   MovieRepository movieRepository,
                                   CinemaRoomRepository cinemaRoomRepository) {
        this.screeningRepository = screeningRepository;
        this.movieRepository = movieRepository;
        this.cinemaRoomRepository = cinemaRoomRepository;
    }
    
    public Screening scheduleNewScreening(Long movieId, Long roomId, Date screeningTime) {
        Movie movie = movieRepository.findById(movieId);
        CinemaRoom room = cinemaRoomRepository.findById(roomId);
        
        if (movie == null || room == null) {
            throw new IllegalArgumentException("Movie or room not found");
        }
        
        Screening screening = Screening.createScreening(movie, screeningTime, room);
        screeningRepository.save(screening);
        return screening;
    }
    
    public void cancelScreening(Long screeningId) {
        Screening screening = screeningRepository.findById(screeningId);
        if (screening != null) {
            screeningRepository.delete(screening);
        }
    }
}
