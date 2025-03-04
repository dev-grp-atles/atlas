package tn.esprit.atlas.utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import tn.esprit.atlas.entities.Reservation;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class PdfGenerator {

    public static File generateReservationPdf(Reservation reservation) throws IOException {
        // Create a new PDF document
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        // Create a content stream for the page
        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        // Load the logo image from resources
        InputStream logoStream = PdfGenerator.class.getResourceAsStream("/tn/esprit/atlas/assets/ATLAS_LOGO.png");
        if (logoStream == null) {
            throw new IOException("Logo file not found in resources!");
        }
        PDImageXObject logo = PDImageXObject.createFromByteArray(document, logoStream.readAllBytes(), "ATLAS.LOGO");

        // Add the logo to the PDF
        float logoWidth = 100; // Adjust the width of the logo
        float logoHeight = 50; // Adjust the height of the logo
        float logoX = 50; // X position of the logo
        float logoY = page.getMediaBox().getHeight() - 70; // Y position of the logo
        contentStream.drawImage(logo, logoX, logoY, logoWidth, logoHeight);

        // Set font and font size for the header
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
        contentStream.beginText();
        contentStream.newLineAtOffset(50, logoY - 30); // Position below the logo
        contentStream.showText("Reservation Confirmation");
        contentStream.endText();

        // Set font and font size for the details
        contentStream.setFont(PDType1Font.HELVETICA, 12);
        float startY = logoY - 60; // Start position for the details

        // Write reservation details to the PDF
        contentStream.beginText();
        contentStream.newLineAtOffset(50, startY);
        contentStream.showText("Dear " + reservation.getPrenom() + " " + reservation.getNom() + ",");
        contentStream.newLineAtOffset(0, -20);
        contentStream.showText("Thank you for choosing Atlas for your reservation. Below are the details of your booking:");
        contentStream.newLineAtOffset(0, -30);

        // Reservation details
        contentStream.showText("Name: " + reservation.getPrenom() + " " + reservation.getNom());
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Email: " + reservation.getEmail());
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Room Type: " + reservation.getTypeChambre());
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Check-In Date: " + reservation.getDateArrivee());
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Check-Out Date: " + reservation.getDateDepart());
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Total Amount: " + reservation.getMontantTotal() + " DT");
        contentStream.endText();

        // Add a footer
        contentStream.setFont(PDType1Font.HELVETICA_OBLIQUE, 10);
        contentStream.beginText();
        contentStream.newLineAtOffset(50, 50); // Position at the bottom of the page
        contentStream.showText("Thank you for choosing Atlas. We look forward to serving you!");
        contentStream.endText();

        // Close the content stream
        contentStream.close();

        // Save the PDF to a temporary file
        File pdfFile = File.createTempFile("reservation_", ".pdf");
        document.save(pdfFile);
        document.close();

        return pdfFile;
    }
}