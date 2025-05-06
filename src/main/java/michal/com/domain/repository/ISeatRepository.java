/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package michal.com.domain.repository;

import java.util.List;
import java.util.Optional;
import michal.com.domain.model.Seat;

/**
 *
 * @author zymci
 */
public interface ISeatRepository {
    Seat save(Seat seat);
    Seat findById(Long id);
    List<Seat> findAll();
    void deleteById(Long id);
    void delete(Seat seat);
    
    List<Seat> findByRoomId(Long roomId);
    List<Seat> findByRoomIdAndAvailability(Long roomId, boolean available);
    List<Seat> findByReservationId(Long reservationId);
    List<Seat> findByScreeningId(Long screeningId);
    List<Seat> findByScreeningIdAndAvailability(Long screeningId, boolean available);
}
