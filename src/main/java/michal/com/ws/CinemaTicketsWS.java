/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package michal.com.ws;

import infrastructure.ConfirmationGenerator;
import infrastructure.HibernateUtil;
import infrastructure.repository.CinemaRoomRepository;
import infrastructure.repository.CustomerRepository;
import infrastructure.repository.MovieRepository;
import infrastructure.repository.ReservationRepository;
import infrastructure.repository.ScreeningRepository;
import infrastructure.repository.SeatRepository;
import infrastructure.repository.UserRepository;
import java.awt.Image;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.soap.MTOM;
import michal.com.application.AuthenticationService;
import michal.com.application.BookingService;
import michal.com.application.MovieManagementService;
import michal.com.application.MovieQueryService;
import michal.com.application.MovieQueryService.MovieWithScreenings;
import michal.com.application.ScreeningManagementService;
import michal.com.domain.model.Customer;
import michal.com.domain.model.Movie;
import michal.com.domain.model.Reservation;
import michal.com.domain.model.Screening;
import michal.com.domain.model.Seat;
import michal.com.domain.model.User;
import org.hibernate.SessionFactory;

/**
 *
 * @author zymci
 */
@MTOM(enabled = true, threshold = 1024)
@HandlerChain(file="handler-chain.xml")
@WebService(endpointInterface = "michal.com.ws.ICinemaTicketsWS")
public class CinemaTicketsWS implements ICinemaTicketsWS {

    private final AuthenticationService authService;
    private final BookingService bookingService;
    private final MovieQueryService movieQueryService;
    private final MovieManagementService movieManagementService;
    private final ScreeningManagementService screeningManagementService;
    private final ConfirmationGenerator pdfGenerator;

    public CinemaTicketsWS() throws SQLException {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        MovieRepository movieRepo = new MovieRepository(sessionFactory);
        ScreeningRepository screeningRepo = new ScreeningRepository(sessionFactory);
        SeatRepository seatRepo = new SeatRepository(sessionFactory);
        ReservationRepository reservationRepo = new ReservationRepository(sessionFactory);
        CustomerRepository customerRepo = new CustomerRepository(sessionFactory);
        CinemaRoomRepository roomRepo = new CinemaRoomRepository(sessionFactory);
        UserRepository userRepo = new UserRepository(sessionFactory);
        this.authService = new AuthenticationService(userRepo);
        this.pdfGenerator = new ConfirmationGenerator();
        this.bookingService = new BookingService(reservationRepo, screeningRepo, seatRepo, customerRepo, pdfGenerator);
        this.movieQueryService = new MovieQueryService(movieRepo, screeningRepo);
        this.movieManagementService = new MovieManagementService(movieRepo);
        this.screeningManagementService = new ScreeningManagementService(screeningRepo, movieRepo, roomRepo);
    }

     @Override
    public User login(String username, String password) {
        if (authService.validateCredentials(username, password)) {
            return authService.getUserByUsername(username);
        }
        throw new SecurityException("Invalid credentials");
    }

    @Override
    public User register(String username, String password, String firstName, 
                       String lastName, String email, String phoneNumber) {
        Customer customer =  Customer.createCustomer(firstName, lastName, email, phoneNumber);
        
        authService.registerUser(username, password, customer);
        return authService.getUserByUsername(username);
    }

    @Override
    public Movie addMovie(String title, String director, List<String> actors, String description, Image img) {
        Movie movie = Movie.createMovie(title, director, actors, description, img);
        return movieManagementService.addMovie(movie);
    }

    @Override
    public Screening addScreening(Long movieId, String screeningTime, int roomNumber) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date = format.parse(screeningTime);
            return screeningManagementService.scheduleNewScreening(movieId, (long)roomNumber, date);
        } catch (ParseException e) {
            throw new RuntimeException("Invalid date format. Use yyyy-MM-dd HH:mm", e);
        }
    }

    @Override
    public List<Movie> getAvailableMovies() {
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 7);
        Date weekLater = cal.getTime();
        
        return movieQueryService.getCurrentMoviesWithScreenings(now, weekLater)
                .stream()
                .map(MovieWithScreenings::getMovie)
                .collect(Collectors.toList());
    }

    @Override
    public List<Screening> getScreeningsForMovie(Long movieId) {
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 7);
        Date weekLater = cal.getTime();
        
        return movieQueryService.getScreeningsForMovie(movieId, now, weekLater);
    }
@Override
public Reservation modifyReservation(Long reservationId, long[] newSeatIds) {
    List<Long> newSeats = Arrays.stream(newSeatIds).boxed().collect(Collectors.toList());
    BookingService.BookingResult result = bookingService.modifyReservation(reservationId, newSeats);
    if (!result.isSucces()) {
        throw new RuntimeException(result.getError());
    }
    return bookingService.getReservationById(result.getReservationId());
}
    @Override
    public List<Seat> getAvailableSeats(Long screeningId) {
        return movieQueryService.getAvailableSeatsForScreening(screeningId);
    }

    @Override
    public Reservation makeReservation(Long screeningId, long[] seatIds) {
        Long customerId = 1L;
        List<Long> seats = Arrays.stream(seatIds).boxed().collect(Collectors.toList());
        
        BookingService.BookingResult result = bookingService.makeReservation(
            screeningId, seats, customerId);
        
        if (!result.isSucces()) {
            throw new RuntimeException(result.getError());
        }
        
       return bookingService.getReservationById(result.getReservationId());
    }

    @Override
    public List<Reservation> getReservationForCustomer(Long customerId) {
        return bookingService.getCustomerReservations(customerId);
    }

    @Override
    public byte[] downloadTicket(Long reservationId) {
        return bookingService.getReservationConfirmation(reservationId);
    }

    @Override
    public boolean cancelReservation(Long reservationId) {
        bookingService.cancelReservation(reservationId);
        return true;
    }
}