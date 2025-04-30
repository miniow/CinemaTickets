/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package michal.com.domain.model;

import michal.com.domain.model.ReservationStatus;
import michal.com.domain.model.Screening;
import michal.com.domain.model.Seat;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author zymci
 */
@Entity
@Table(name = "rsi_Reservations")
public class Reservation {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "screening_id", nullable = false)
    private Screening screening;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "ReservationSeats",
        joinColumns = @JoinColumn(name = "reservation_id"),
        inverseJoinColumns = @JoinColumn(name = "seat_id"))
    private List<Seat> reservedSeats;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    protected Reservation() {}

    private Reservation(Screening screening, List<Seat> reservedSeats, Customer customer) {
        if (screening == null) throw new IllegalArgumentException("Screening cannot be null");
        if (reservedSeats == null || reservedSeats.isEmpty()) throw new IllegalArgumentException("Reserved seats cannot be null or empty");
        if (customer == null) throw new IllegalArgumentException("Customer cannot be null");

        this.screening = screening;
        this.reservedSeats = reservedSeats;
        this.customer = customer;
        this.status = ReservationStatus.CONFIRMED;
    }

    public static Reservation createReservation(Screening screening, List<Seat> reservedSeats, Customer customer) {
        return new Reservation(screening, reservedSeats, customer);
    }

    public Long getId() {
        return id;
    }

    public List<Seat> getReservedSeats() {
        return reservedSeats;
    }

    public Customer getCustomer() {
        return customer;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void cancel() {
        this.status = ReservationStatus.CANCELLED;
        reservedSeats.forEach(Seat::cancelReservation);
    }

    public void modifySeats(List<Seat> newSeats) {
        this.status = ReservationStatus.MODIFIED;
        this.reservedSeats.forEach(Seat::cancelReservation);
        newSeats.forEach(Seat::reserve);
        this.reservedSeats = newSeats;
    }
}
