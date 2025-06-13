package fr.diginamic.VroomVroomCar.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

/**
 * DTO représentant les données nécessaires d'une réservation.
 * Ces informations seront envoyées dans la base de donnée.
 */

@Data
@NoArgsConstructor
@Schema(description = "DTO de requête pour enregistrer les données dans l'API.")
public class ReservationRequestDto {

    private Integer id;

    @Schema(description = "Date de début de la réservation", example = "2025-07-15")
    @NotNull(message = "La date de début de la réservation est obligatoire.")
    private Date dateDebut;
    @Schema(description = "Date de fin de la réservation", example = "2025-07-15")
    @NotNull(message = "La date de fin de la réservation est obligatoire.")
    private Date dateFin;

    @Schema(description = "Identifiant de l'utilisateur qui réserve le véhicule", example = "2")
    @NotNull(message = "L'identifiant de l'utilisateur qui réserve est obligatoire.")
    private Integer userId;

    @Schema(description = "Identifiant du véhicule réservé", example = "1")
    @NotNull(message = "L'identifiant du véhicule réservé est obligatoire.")
    private Integer carId;
}
