/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package infrastructure;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import michal.com.domain.model.Reservation;
import michal.com.domain.model.Seat;


/**
 *
 * @author zymci
 */
public class ConfirmationGenerator implements PdfGenerator{

    @Override
    public byte[] generateConfirmation(Reservation reservation) {
       try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();

            // Nagłówek
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("Potwierdzenie rezerwacji", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Dane filmu
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Film: " + reservation.getScreening().getMovie().getTitle()));
            document.add(new Paragraph("Data i godzina: " + reservation.getScreening().getTime().toString()));

            // Miejsca
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Zarezerwowane miejsca:"));
            for (Seat seat : reservation.getReservedSeats()) {
                document.add(new Paragraph("- " + seat.getLabel()));
            }

            // Dane klienta
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Dane klienta:"));
            document.add(new Paragraph(reservation.getCustomer().getFullName()));
            document.add(new Paragraph(reservation.getCustomer().getEmail()));
            document.add(new Paragraph(reservation.getCustomer().getPhoneNumber()));

            document.close();
            return baos.toByteArray();

        } catch (DocumentException e) {
            throw new RuntimeException("Nie udało się wygenerować PDF", e);
        } catch (IOException ex) {
            Logger.getLogger(ConfirmationGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } 
       return null;
    }

}