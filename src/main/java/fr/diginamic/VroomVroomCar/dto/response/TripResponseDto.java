package fr.diginamic.VroomVroomCar.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalTime;


/**
 * DTO représentant les données d'un trajet renvoyées au client.
 * Contient toutes les informations nécessaires à l'affichage d'un trajet.
 */

@Data
@NoArgsConstructor
public class TripResponseDto {

        private Integer id;

        private Date dateDebut;
        private Date dateFin;

        private LocalTime heureDepart;
        private LocalTime heureArrivee;

        private String lieuDepart;
        private String lieuArrivee;

        private String villeDepart;
        private String villeArrivee;

        private int nbPlacesRestantes;

        private Integer organisateurId;

        private Integer carId;
}
