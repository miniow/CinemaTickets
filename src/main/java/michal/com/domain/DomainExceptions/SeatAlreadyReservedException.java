/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package michal.com.domain.DomainExceptions;

/**
 *
 * @author zymci
 */
public class SeatAlreadyReservedException extends DomainException {
    public SeatAlreadyReservedException() {
        super("Seat is already reserved.");
    }
}