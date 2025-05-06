/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package michal.com.application;

import infrastructure.ConfirmationGenerator;
import infrastructure.repository.CustomerRepository;
import infrastructure.repository.ReservationRepository;
import infrastructure.repository.ScreeningRepository;
import infrastructure.repository.SeatRepository;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import michal.com.domain.model.Customer;
import michal.com.domain.model.Reservation;
import michal.com.domain.model.Screening;
import michal.com.domain.model.Seat;

/**
 *
 * @author zymci
 */
public class BookingService {
    private final ReservationRepository reservationRepository;
    private final ScreeningRepository screeningRepository;
    private final SeatRepository seatRepository;
    private final CustomerRepository customerRepository;
    private final ConfirmationGenerator pdfGenerator;
    public BookingService(ReservationRepository reservationRepository,
                        ScreeningRepository screeningRepository,
                        SeatRepository seatRepository,
                        CustomerRepository customerRepository,
                        ConfirmationGenerator pdfGenerator) {
        this.reservationRepository = reservationRepository;
        this.screeningRepository = screeningRepository;
        this.seatRepository = seatRepository;
        this.customerRepository = customerRepository;
        this.pdfGenerator = pdfGenerator;
    }
    public BookingResult makeReservation(Long screeningId, List<Long> seatIds, Long customerId) {
        Screening screening = screeningRepository.findById(screeningId);
        Customer customer = customerRepository.findById(customerId);
        
        if (screening == null || customer == null) {
            return BookingResult.failure("Invalid screening or customer");
        }
        
        List<Seat> seats = seatIds.stream()
            .map(seatRepository::findById)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
        
        if (seats.size() != seatIds.size()) {
            return BookingResult.failure("Some seats not found");
        }
        
        List<Seat> unavailableSeats = seats.stream()
            .filter(seat -> !seat.isAvailable())
            .collect(Collectors.toList());
        
        if (!unavailableSeats.isEmpty()) {
            return BookingResult.failure("Some seats are already reserved: " + 
                unavailableSeats.stream()
                    .map(Seat::getLabel)
                    .collect(Collectors.joining(", ")));
        }
        
        seats.forEach(Seat::reserve);
        Reservation reservation = Reservation.createReservation(screening, seats, customer);
        reservationRepository.save(reservation);
        
        byte[] pdfConfirmation = pdfGenerator.generateConfirmation(reservation);
        
        return BookingResult.success(reservation.getId(), pdfConfirmation);
    }
    
    public void cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId);
        if (reservation != null) {
            reservation.cancel();
            reservationRepository.save(reservation);
        }
    }
    public List<Reservation> getCustomerReservations(Long customerId) {
    Customer customer = customerRepository.findById(customerId);
    if (customer == null) {
        throw new IllegalArgumentException("Customer not found");
    }
    return reservationRepository.findByCustomerId(customer.getId());
}
    public Reservation getReservationById(Long reservationId) {
    return reservationRepository.findById(reservationId);
}
    public byte[] getReservationConfirmation(Long reservationId) {
    Reservation reservation = reservationRepository.findById(reservationId);
    if (reservation == null) {
        throw new IllegalArgumentException("Reservation not found.");
    }
    return pdfGenerator.generateConfirmation(reservation);
}
    
    public BookingResult modifyReservation(Long reservationId, List<Long> newSeatIds) {
        Reservation reservation = reservationRepository.findById(reservationId);
        if (reservation == null) {
            return BookingResult.failure("Reservation not found");
        }
        
        List<Seat> newSeats = newSeatIds.stream()
            .map(seatRepository::findById)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
        
        if (newSeats.size() != newSeatIds.size()) {
            return BookingResult.failure("Some new seats not found");
        }
        
        List<Seat> unavailableSeats = newSeats.stream()
            .filter(seat -> !seat.isAvailable())
            .collect(Collectors.toList());
        
        if (!unavailableSeats.isEmpty()) {
            return BookingResult.failure("Some new seats are already reserved: " + 
                unavailableSeats.stream()
                    .map(Seat::getLabel)
                    .collect(Collectors.joining(", ")));
        }
        
        reservation.modifySeats(newSeats);
        reservationRepository.save(reservation);
        
        byte[] pdfConfirmation = pdfGenerator.generateConfirmation(reservation);
        
        return BookingResult.success(reservation.getId(), pdfConfirmation);
    }
    
    public static class BookingResult {
        private final boolean success;
        private final String errorMessage;
        private final Long reservationId;
        private final byte[] pdfConfirmation;
        
        private BookingResult(boolean success, String errorMessage, Long reservationId, byte[] pdfConfirmation) {
            this.success = success;
            this.errorMessage = errorMessage;
            this.reservationId = reservationId;
            this.pdfConfirmation = pdfConfirmation;
        }
        
        public static BookingResult success(Long reservationId, byte[] pdfConfirmation) {
            return new BookingResult(true, null, reservationId, pdfConfirmation);
        }
        
        public static BookingResult failure(String errorMessage) {
            return new BookingResult(false, errorMessage, null, null);
        }
        public boolean isSucces(){
            return success;
        }
        public String getError(){
            return errorMessage;
        }
        public Long getReservationId(){
            return reservationId;
            
        }
        public byte[] getPdfConfirmation(){
            return pdfConfirmation;
        }
    }
}
