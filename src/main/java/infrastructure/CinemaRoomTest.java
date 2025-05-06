/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package infrastructure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import michal.com.domain.model.CinemaRoom;
import michal.com.domain.model.Seat;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 *
 * @author zymci
 */
public class CinemaRoomTest {
    public static void main(String[] args) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        
        // Tworzenie sali Premium (mniejsza, kwadratowa 5x5 = 25 miejsc)
        List<Seat> premiumSeats = new ArrayList<>();
        int premiumRows = 5;
        int premiumSeatsPerRow = 5;
        
        for (int row = 1; row <= premiumRows; row++) {
            for (int seatNum = 1; seatNum <= premiumSeatsPerRow; seatNum++) {
                premiumSeats.add(Seat.createSeat(row, seatNum, true));
            }
        }
        
        CinemaRoom premiumRoom = CinemaRoom.createCinemaRoom("Sala Premium", premiumSeats);

        // Tworzenie sali Standard (większa, prostokątna 10x15 = 150 miejsc)
        List<Seat> standardSeats = new ArrayList<>();
        int standardRows = 10;
        int standardSeatsPerRow = 15;
        
        for (int row = 1; row <= standardRows; row++) {
            for (int seatNum = 1; seatNum <= standardSeatsPerRow; seatNum++) {
                standardSeats.add(Seat.createSeat(row, seatNum, true));
            }
        }
        
        CinemaRoom standardRoom = CinemaRoom.createCinemaRoom("Sala Standard", standardSeats);

        // Zapis do bazy danych
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        
        try {
            transaction = session.beginTransaction();
            
            // Zapisujemy miejsca i sale
            premiumSeats.forEach(session::save);
            session.save(premiumRoom);
            
            standardSeats.forEach(session::save);
            session.save(standardRoom);
            
            transaction.commit();
            
            System.out.println("Sale kinowe zapisane pomyślnie!");
            System.out.println("Sala Premium - ID: " + premiumRoom.getId() + 
                             ", Liczba miejsc: " + premiumRoom.getSeats().size());
            System.out.println("Sala Standard - ID: " + standardRoom.getId() + 
                             ", Liczba miejsc: " + standardRoom.getSeats().size());
            
            // Testowe odczytanie
            System.out.println("\nDane sali Premium:");
            CinemaRoom savedPremium = (CinemaRoom) session.get(CinemaRoom.class, premiumRoom.getId());
            printRoomInfo(savedPremium);
            
            System.out.println("\nDane sali Standard:");
            CinemaRoom savedStandard = (CinemaRoom) session.get(CinemaRoom.class, standardRoom.getId());
            printRoomInfo(savedStandard);
            
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Błąd podczas zapisu sal kinowych: " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close();
            sessionFactory.close();
        }
    }
    
    private static void printRoomInfo(CinemaRoom room) {
        System.out.println("Nazwa: " + room.getName());
        System.out.println("Liczba rzędów: " );
        System.out.println("Liczba miejsc w rzędzie: " );
        System.out.println("Łączna liczba miejsc: " + room.getSeats().size());
        
        // Przykładowe miejsca
        System.out.println("Przykładowe miejsca:");
        
            
    }
}