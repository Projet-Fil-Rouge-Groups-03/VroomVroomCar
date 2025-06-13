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
 * DTO représentant les données nécessaires d'un trajet.
 * Ces informations seront envoyés dans la base de donnée.
 */

@Data
@NoArgsConstructor
@Schema(description = "DTO de requête pour enregistrer les données dans l'API.")
public class TripRequestDto {

    private Integer id;

    @Schema(description = "Date de début du trajet", example = "2025-07-15")
    @NotNull(message = "La date de début de trajet est obligatoire.")
    private Date dateDebut;
    @Schema(description = "Date de fin du trajet", example = "2025-07-17")
    @NotNull(message = "La date de fin de trajet est obligatoire.")
    private Date dateFin;

    @Schema(description = "Heure de départ du trajet", example = "10:00:00")
    @NotNull(message = "L'heure de départ est obligatoire.")
    private LocalTime heureDepart;
    // heureArrivee supprimée car calculée

    @Schema(description = "Lieu de départ du trajet", example = "Gare Lille Flandres")
    @NotBlank(message = "Le lieu de départ est obligatoire.")
    private String lieuDepart;
    @Schema(description = "Lieu d'arrivée du trajet", example = "Gare du Nord")
    @NotBlank(message = "Le lieu d'arrivée est obligatoire.")
    private String lieuArrivee;

    @Schema(description = "Ville de départ du trajet", example = "Lille")
    @NotBlank(message = "La ville de départ est obligatoire.")
    private String villeDepart;
    @Schema(description = "Ville d'arrivée du trajet", example = "Paris")
    @NotBlank(message = "La ville d'arrivée est obligatoire.")
    private String villeArrivee;

    @Schema(description = "Nombre de places restantes du véhicule", example = "2")
    @Min(value = 0, message = "Le nombre de places restantes ne peut pas être en dessous de 0.")
    private int nbPlacesRestantes;

    @Schema(description = "Identifiant de l'organisateur", example = "42")
    @NotNull(message = "L'organisateur est obligatoire.")
    private Integer organisateurId;

    @Schema(description = "Identifiant du vehicule", example = "44")
    @NotNull(message = "Le véhicule est obligatoire.")
    private Integer carId;


}
