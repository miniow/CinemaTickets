/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package michal.com.domain.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import michal.com.domain.model.Reservation;

/**
 *
 * @author zymci
 */
public interface IReservationRepository {
    Reservation save(Reservation reservation);
    Reservation findById(Long id);
    List<Reservation> findAll();
    void deleteById(Long id);
    void delete(Reservation reservation);

    List<Reservation> findByCustomerId(Long customerId);
    List<Reservation> findByScreeningId(Long screeningId);
    List<Reservation> findByStatus(String status);
    List<Reservation> findByCustomerAndStatus(Long customerId, String status);
    List<Reservation> findByScreeningAndStatus(Long screeningId, String status);
    List<Reservation> findByDateBetween(Date startDate, Date endDate);
}
