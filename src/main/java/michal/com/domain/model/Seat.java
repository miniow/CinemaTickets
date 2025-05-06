/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package michal.com.domain.model;

import infrastructure.BooleanToNumberConverter;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import michal.com.domain.DomainExceptions.SeatAlreadyReservedException;

/**
 *
 * @author zymci
 */
@Entity
@Table(name = "rsi_Seats")
public class Seat {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private int rowNumber;

    @Column(nullable = false)
    private int seatNumber;

    @Column(nullable = false)
    @Convert(converter = BooleanToNumberConverter.class)
    private boolean isAvailable;

    protected Seat() {} 

    private Seat(int rowNumber, int seatNumber, boolean isAvailable) {
        if (rowNumber < 0 || seatNumber < 0) {
            throw new IllegalArgumentException("Row and seat numbers must be non-negative.");
        }
        this.rowNumber = rowNumber;
        this.seatNumber = seatNumber;
        this.isAvailable = isAvailable;
    }

    public static Seat createSeat(int rowNumber, int seatNumber, boolean isAvailable) {
        return new Seat( rowNumber, seatNumber, isAvailable);
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void reserve() {
        if (!isAvailable) {
            throw new SeatAlreadyReservedException();
        }
        this.isAvailable = false;
    }

    public void cancelReservation() {
        this.isAvailable = true;
    }

    public Long getId() {
        return id;
    }

    public String getLabel() {
        return "R" + rowNumber + "S" + seatNumber;
    }
}