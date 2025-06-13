package fr.diginamic.VroomVroomCar.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalTime;

/**
 * DTO utilisé pour la création ou la mise à jour d'un trajet.
 * Contient l'ensemble des informations nécessaires à l'enregistrement d'un trajet
 * dans la base de données.
 */
@Data
@NoArgsConstructor
@Schema(description = "Représente les données nécessaires pour créer ou mettre à jour un trajet.")
public class TripRequestDto {

    /**
     * Identifiant du trajet.
     */
    @Schema(description = "Identifiant du trajet (optionnel, utilisé pour la mise à jour)", example = "101")
    private Integer id;

    /**
     * Date de début du trajet.
     */
    @Schema(description = "Date de début du trajet", example = "2025-07-15")
    @NotNull(message = "La date de début de trajet est obligatoire.")
    private Date dateDebut;
    /**
     * Date de fin du trajet.
     */
    @Schema(description = "Date de fin du trajet", example = "2025-07-17")
    @NotNull(message = "La date de fin de trajet est obligatoire.")
    private Date dateFin;

    /**
     * Heure de départ prévue du trajet.
     */
    @Schema(description = "Heure de départ du trajet", example = "10:00:00")
    @NotNull(message = "L'heure de départ est obligatoire.")
    private LocalTime heureDepart;
    // heureArrivee supprimée, car elle est calculée dynamiquement dans le service.

    /**
     * Lieu de départ du trajet.
     */
    @Schema(description = "Lieu précis de départ (ex: nom de gare ou adresse)", example = "Gare Lille Flandres")
    @NotBlank(message = "Le lieu de départ est obligatoire.")
    private String lieuDepart;
    /**
     * Lieu d'arrivée du trajet.
     */
    @Schema(description = "Lieu précis d'arrivée", example = "Gare du Nord")
    @NotBlank(message = "Le lieu d'arrivée est obligatoire.")
    private String lieuArrivee;

    /**
     * Ville de départ du trajet.
     */
    @Schema(description = "Ville de départ", example = "Lille")
    @NotBlank(message = "La ville de départ est obligatoire.")
    private String villeDepart;
    /**
     * Ville d'arrivée du trajet.
     */
    @Schema(description = "Ville d'arrivée", example = "Paris")
    @NotBlank(message = "La ville d'arrivée est obligatoire.")
    private String villeArrivee;

    /**
     * Nombre de places encore disponibles dans le véhicule au moment de la création du trajet.
     * Cette valeur est calculée automatiquement selon les réservations.
     */
    @Schema(description = "Nombre de places restantes disponibles", example = "2")
    @Min(value = 0, message = "Le nombre de places restantes ne peut pas être en dessous de 0.")
    private int nbPlacesRestantes;

    /**
     * Identifiant de l'utilisateur organisateur du trajet.
     */
    @Schema(description = "Identifiant de l'organisateur du trajet", example = "42")
    @NotNull(message = "L'organisateur est obligatoire.")
    private Integer organisateurId;

    /**
     * Identifiant du véhicule associé au trajet.
     */
    @Schema(description = "Identifiant du véhicule utilisé pour le trajet", example = "44")
    @NotNull(message = "Le véhicule est obligatoire.")
    private Integer carId;
}

