/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package michal.com.domain.repository;

import java.util.List;
import java.util.Optional;
import michal.com.domain.model.CinemaRoom;

/**
 *
 * @author zymci
 */
public interface ICinemaRoomRepository {
    CinemaRoom save(CinemaRoom room);
    CinemaRoom findById(Long id);
    List<CinemaRoom> findAll();
    void deleteById(Long id);
    void delete(CinemaRoom room);
    
    List<CinemaRoom> findByNameContaining(String nameFragment);
    List<CinemaRoom> findByCapacityGreaterThan(int minCapacity);
    List<CinemaRoom> findByCapacityLessThan(int maxCapacity);
}
