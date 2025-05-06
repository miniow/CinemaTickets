/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package michal.com.domain.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import michal.com.domain.model.Screening;

/**
 *
 * @author zymci
 */
public interface IScreeningRepository {
    Screening save(Screening screening);
    Screening findById(Long id);
    List<Screening> findAll();
    void deleteById(Long id);
    void delete(Screening screening);
    
    // Specjalne zapytania
    List<Screening> findByMovieId(Long movieId);
    List<Screening> findByRoomId(Long roomId);
    List<Screening> findByDateBetween(Date startDate, Date endDate);
    List<Screening> findByMovieAndDateBetween(Long movieId, Date startDate, Date endDate);
    List<Screening> findByRoomAndDateBetween(Long roomId, Date startDate, Date endDate);
}
