package fr.diginamic.VroomVroomCar.dto.request;

import fr.diginamic.VroomVroomCar.entity.Categorie;
import fr.diginamic.VroomVroomCar.entity.Motorisation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Cette classe représente un DTO (Data Transfer Object) pour les requêtes relatives à une voiture.
 * Elle est utilisée pour transférer les données de requête relatives à une voiture entre les couches de l'application.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO pour les requêtes relatives à une voiture.")
public class CarRequestDto {

    @Schema(description = "La marque de la voiture", example = "Renault", required = true)
    @NotBlank(message = "La marque est obligatoire")
    private String marque;

    @Schema(description = "Le modèle de la voiture", example = "Clio", required = true)
    @NotBlank(message = "Le modèle est obligatoire")
    private String modele;

    @Schema(description = "Le nombre de places de la voiture", example = "5", required = true)
    @Min(value = 1, message = "Le nombre de places doit être au minimum de 1")
    @Max(value = 10, message = "Le nombre de places doit être au maximum de 10")
    private int nbDePlaces;

    @Schema(description = "Le niveau de pollution de la voiture", example = "120 g/km")
    @Size(max = 30, message = "La pollution ne peut pas dépasser 30 caractères")
    private String pollution;

    @Schema(description = "Informations supplémentaires sur la voiture", example = "Climatisation automatique")
    private String infosSupp;

    @Schema(description = "L'identifiant de l'utilisateur associé à cette voiture", example = "1", required = true)
    @NotNull(message = "L'utilisateur est obligatoire")
    private Integer utilisateurId;

    @Schema(description = "La motorisation de la voiture", required = true)
    @NotNull(message = "La motorisation est obligatoire")
    private Motorisation motorisation;

    @Schema(description = "La catégorie de la voiture")
    private Categorie categorie;
}