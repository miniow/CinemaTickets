/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package michal.com.ws;

import java.awt.Image;
import java.util.List;
import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import michal.com.domain.model.Movie;
import michal.com.domain.model.Reservation;
import michal.com.domain.model.Screening;
import michal.com.domain.model.Seat;
import michal.com.domain.model.User;

/**
 *
 * @author zymci
 */
@WebService
@HandlerChain(file="handler-chain.xml")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
public interface ICinemaTicketsWS {

    @WebMethod
    User login(String username, String password);

    @WebMethod
    User register(String username, String password, String firstName, String lastName, String email, String phoneNumber);

    @WebMethod
    Movie addMovie( String title, String director, List<String> actors, String description, Image img);

    @WebMethod
    Screening addScreening(Long movieId, String screeningTime, int roomNumber);

    @WebMethod
    List<Movie> getAvailableMovies();

    @WebMethod
    List<Screening> getScreeningsForMovie(Long movieId);

    @WebMethod
    List<Seat> getAvailableSeats(Long screeningId);

    @WebMethod
    Reservation makeReservation( Long screeningId, long[] seatIds);
    
    @WebMethod
    List<Reservation>  getReservationForCustomer(Long CustomerId);

    @WebMethod 
    byte[] downloadTicket(Long reservationId);
    
    @WebMethod
    boolean cancelReservation(Long reservationId);
    
    @WebMethod
    Reservation modifyReservation(Long reservationId, long[] newSeatIds);
}
