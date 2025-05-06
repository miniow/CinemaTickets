/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package infrastructure;

import michal.com.domain.model.Reservation;

/**
 *
 * @author zymci
 */
public interface PdfGenerator {
    byte[] generateConfirmation(Reservation reservation);
}
