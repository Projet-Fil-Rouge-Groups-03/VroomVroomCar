package fr.diginamic.VroomVroomCar.dto;

import fr.diginamic.VroomVroomCar.entity.Trip;
import fr.diginamic.VroomVroomCar.util.DateUtil;
import lombok.Getter;
import java.util.Date;

@Getter

public class TripNotificationDetailsDto {
    private final Date dateDebut;
    private final String villeDepart;
    private final String villeArrivee;
    private final String organisateurFullName;

    // Constructeur pour mapper facilement depuis l'entité Trip
    public TripNotificationDetailsDto(Trip trip) {
        this.dateDebut = trip.getDateDebut();
        this.villeDepart = trip.getVilleDepart();
        this.villeArrivee = trip.getVilleArrivee();
        this.organisateurFullName = trip.getOrganisateur().getPrenom() + " " + trip.getOrganisateur().getNom();
    }

    // Méthode utilitaire pour formater la date joliment dans la notif
    public String getFormattedDate() {
        if (this.dateDebut == null) {
            return "date inconnue";
        }
        // Conversion de java.util.Date vers java.time.LocalDate
        java.time.LocalDate localDate = new java.sql.Date(this.dateDebut.getTime()).toLocalDate();
        return DateUtil.formatToFrench(localDate);
    }
}
