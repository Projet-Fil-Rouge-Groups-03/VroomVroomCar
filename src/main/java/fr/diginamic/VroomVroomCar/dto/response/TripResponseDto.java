package fr.diginamic.VroomVroomCar.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Représente un trajet renvoyé par l'API.")
public class TripResponseDto {

        @Schema(description = "Identifiant unique du trajet", example = "1")
        private Integer id;

        @Schema(description = "Date de début du trajet", example = "2025-07-15")
        private Date dateDebut;
        @Schema(description = "Date de fin du trajet", example = "2025-07-17")
        private Date dateFin;

        @Schema(description = "Heure de départ du trajet", example = "10:00:00")
        private LocalTime heureDepart;
        @Schema(description = "Heure d'arrivée du trajet calculée", example = "13:00:00")
        private LocalTime heureArrivee;

        @Schema(description = "Lieu de départ du trajet", example = "Gare Lille Flandres")
        private String lieuDepart;
        @Schema(description = "Lieu d'arrivée du trajet", example = "Gare du Nord")
        private String lieuArrivee;

        @Schema(description = "Ville de départ du trajet", example = "Lille")
        private String villeDepart;
        @Schema(description = "Ville d'arrivée du trajet", example = "Paris")
        private String villeArrivee;

        @Schema(description = "Nombre de places restantes du véhicule", example = "2")
        private int nbPlacesRestantes;

        @Schema(description = "Identifiant de l'organisateur", example = "42")
        private Integer organisateurId;

        @Schema(description = "Identifiant du vehicule", example = "44")
        private Integer carId;
}
