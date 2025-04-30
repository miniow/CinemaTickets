/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package michal.com.domain.model;

import michal.com.domain.model.Seat;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author zymci
 */
@Entity
@Table(name = "rsi_CinemaRooms")
public class CinemaRoom {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "cinema_room_id")
    private List<Seat> seats;

    protected CinemaRoom() {}

    private CinemaRoom(String name, List<Seat> seats) {
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Name cannot be null or empty");
        if (seats == null || seats.isEmpty()) throw new IllegalArgumentException("Seats cannot be null or empty");

        this.name = name;
        this.seats = seats;
    }

    public static CinemaRoom createCinemaRoom(String name, List<Seat> seats) {
        return new CinemaRoom(name, seats);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Seat> getSeats() {
        return seats;
    }
}