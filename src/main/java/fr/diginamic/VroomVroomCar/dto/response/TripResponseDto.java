package fr.diginamic.VroomVroomCar.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalTime;


/**
 * DTO représentant les données d'un trajet renvoyées au client.
 *
 * Il est principalement utilisé en réponse des appels API REST liés aux trajets.
 */
@Data
@NoArgsConstructor
@Schema(description = "Objet renvoyé par l'API contenant les détails d'un trajet.")
public class TripResponseDto {

        /**
         * Identifiant unique du trajet.
         */
        @Schema(description = "Identifiant unique du trajet", example = "1")
        private Integer id;

        /**
         * Date de début du trajet
         */
        @Schema(description = "Date de début du trajet", example = "2025-07-15")
        private Date dateDebut;
        /**
         * Date de fin du trajet
         */
        @Schema(description = "Date de fin du trajet", example = "2025-07-17")
        private Date dateFin;

        /**
         * Heure de départ prévue
         */
        @Schema(description = "Heure de départ du trajet", example = "10:00:00")
        private LocalTime heureDepart;
        /**
         * Heure d'arrivée estimée, calculée automatiquement
         */
        @Schema(description = "Heure estimée d'arrivée", example = "13:00:00")
        private LocalTime heureArrivee;

        /**
         * Lieu de départ
         */
        @Schema(description = "Lieu précis de départ", example = "Gare Lille Flandres")
        private String lieuDepart;
        /**
         * Lieu d'arrivée
         */
        @Schema(description = "Lieu précis d'arrivée", example = "Gare du Nord")
        private String lieuArrivee;

        /**
         * Ville de départ.
         */
        @Schema(description = "Ville de départ", example = "Lille")
        private String villeDepart;
        /**
         * Ville d'arrivée.
         */
        @Schema(description = "Ville d'arrivée", example = "Paris")
        private String villeArrivee;

        /**
         * Nombre de places encore disponibles pour ce trajet.
         */
        @Schema(description = "Nombre de places restantes dans le véhicule", example = "2")
        private int nbPlacesRestantes;

        /**
         * Identifiant de l'organisateur du trajet.
         */
        @Schema(description = "Identifiant de l'organisateur du trajet", example = "42")
        private Integer organisateurId;

        /**
         * Identifiant du véhicule utilisé pour ce trajet.
         */
        @Schema(description = "Identifiant du véhicule associé", example = "44")
        private Integer carId;
}

