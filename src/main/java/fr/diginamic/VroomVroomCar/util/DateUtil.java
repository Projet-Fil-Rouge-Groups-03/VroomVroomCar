package fr.diginamic.VroomVroomCar.util;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Utilitaire pour la gestion et le formatage des dates.
 * Cette classe fournit des méthodes pour formater les dates selon différents formats.
 */
public class DateUtil {

    /**
     * Formate une date en une chaîne de caractères selon le format français (jour/mois/année).
     *
     * @param date La date à formater.
     * @return Une chaîne de caractères représentant la date au format "dd/MM/yyyy".
     */
    public static String formatToFrench(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(formatter);
    }

    public static Date convertStringToDate(String dateStr){
        Date dateConverted = null ;
        if (dateStr != null && !dateStr.trim().isEmpty()) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate localDateFin = LocalDate.parse(dateStr, formatter);
                dateConverted = Date.valueOf(localDateFin);
            } catch (java.time.format.DateTimeParseException e) {
                System.err.println("Erreur de format de date pour dateFin : " + dateStr + " - " + e.getMessage());
                throw new IllegalArgumentException("Format de date invalide pour dateFin. Utilisez le format AAAA-MM-JJ.", e);
            }
        }
        return dateConverted;
    }
}
