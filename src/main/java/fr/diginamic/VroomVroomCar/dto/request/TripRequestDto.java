package fr.diginamic.VroomVroomCar.dto.request;

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
public class TripRequestDto {

    private Integer id;

    @NotNull(message = "La date de début de trajet est obligatoire.")
    private Date dateDebut;
    @NotNull(message = "La date de fin de trajet est obligatoire.")
    private Date dateFin;

    @NotNull(message = "L'heure de départ est obligatoire.")
    private LocalTime heureDepart;
    // heureArrivee supprimée car calculée

    @NotBlank(message = "Le lieu de départ est obligatoire.")
    private String lieuDepart;
    @NotBlank(message = "Le lieu d'arrivée est obligatoire.")
    private String lieuArrivee;

    @NotBlank(message = "La ville de départ est obligatoire.")
    private String villeDepart;
    @NotBlank(message = "La ville d'arrivée est obligatoire.")
    private String villeArrivee;

    @Min(value = 0, message = "Le nombre de places restantes ne peut pas être en dessous de 0.")
    private int nbPlacesRestantes;

    @NotNull(message = "L'organisateur est obligatoire.")
    private Integer organisateurId;

    @NotNull(message = "Le véhicule est obligatoire.")
    private Integer carId;


}
