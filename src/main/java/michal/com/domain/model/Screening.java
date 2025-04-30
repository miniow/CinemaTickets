/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package michal.com.domain.model;

import michal.com.domain.model.Seat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author zymci
 */
@Entity
@Table(name = "rsi_Screenings")
public class Screening {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date time;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private CinemaRoom room;

    protected Screening() {}

    private Screening(Movie movie, Date time, CinemaRoom room) {
        if (movie == null) throw new IllegalArgumentException("Movie cannot be null");
        if (time == null) throw new IllegalArgumentException("Time cannot be null");
        if (room == null) throw new IllegalArgumentException("Room cannot be null");

        this.movie = movie;
        this.time = time;
        this.room = room;
    }

    public static Screening createScreening(Movie movie, Date time, CinemaRoom room) {
        return new Screening(movie, time, room);
    }

    public List<Seat> getAvailableSeats() {
        return room.getSeats().stream().filter(Seat::isAvailable).collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public String getScreeningInfo() {
        return movie.getTitle() + " - " + time;
    }

    public Movie getMovie() {
        return movie;
    }

    public Date getTime() {
        return time;
    }

    public CinemaRoom getRoom() {
        return room;
    }
}