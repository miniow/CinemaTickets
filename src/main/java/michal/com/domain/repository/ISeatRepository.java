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
public interface SeatRepository {
    Optional<Seat> findById(Long id);
    List<Seat> findAll();
    void save(Seat seat);
    void delete(Seat seat);
}
