package fr.diginamic.VroomVroomCar.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Cette classe représente un DTO (Data Transfer Object) pour les requêtes relatives à une notification.
 * Elle est utilisée pour transférer les données de requête relatives à une notification entre les couches de l'application.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO pour les requêtes de création/modification d'une notification.")
public class NotificationRequestDto {

    @NotBlank(message = "Le contenu est obligatoire")
    @Schema(description = "Le contenu de la notification", example = "Mazier Didier a annulé sa participation au covoit du 06/07/25")
    private String contenu;

    @NotBlank(message = "Le nom est obligatoire")
    @Schema(description = "Le nom de la notification", example = "Annulation")
    private String nom;

    @Schema(description = "Date de la notification", example = "2025-07-15")
    @NotNull(message = "La date de la notification est obligatoire.")
    private Date date;

    @NotNull(message = "L'ID utilisateur est obligatoire")
    @Schema(description = "L'identifiant de l'utilisateur associé à cette notification", example = "1", required = true)
    private Integer userId;
}
