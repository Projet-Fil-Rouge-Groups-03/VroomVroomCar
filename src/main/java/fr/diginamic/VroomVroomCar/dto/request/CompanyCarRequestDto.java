package fr.diginamic.VroomVroomCar.dto.request;

import fr.diginamic.VroomVroomCar.entity.Categorie;
import fr.diginamic.VroomVroomCar.entity.CompanyCarStatus;
import fr.diginamic.VroomVroomCar.entity.Motorisation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Cette classe représente un DTO (Data Transfer Object) pour les requêtes relatives à une voiture de service.
 * Elle est utilisée pour transférer les données de requête relatives à une voiture de service entre les couches de l'application.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO pour les requêtes de création/modification d'une voiture de service.")
public class CompanyCarRequestDto {

    // Champs hérités de Car

    @NotBlank(message = "La marque est obligatoire")
    @Schema(description = "La marque de la voiture", example = "Renault")
    private String marque;

    @NotBlank(message = "Le modèle est obligatoire")
    @Schema(description = "Le modèle de la voiture", example = "Clio")
    private String modele;

    @Min(value = 1, message = "Le nombre de places doit être au minimum de 1")
    @Max(value = 10, message = "Le nombre de places doit être au maximum de 10")
    @Schema(description = "Le nombre de places disponibles", example = "5")
    private int nbDePlaces;

    @Schema(description = "Taux de pollution en CO2/km", example = "110")
    private String pollution;

    @Schema(description = "Informations supplémentaires sur la voiture", example = "Climatisation automatique")
    private String infosSupp;

    @Schema(description = "ID de l'utilisateur responsable", example = "2")
    private Integer utilisateurId;

    @Schema(description = "Type de motorisation", example = "HYBRIDE")
    private Motorisation motorisation;

    @Schema(description = "Catégorie du véhicule", example = "BERLINE_TAILLE_M")
    private Categorie categorie;

    // Champs spécifiques à CompanyCar

    @NotBlank(message = "L'immatriculation est obligatoire")
    @Schema(description = "Immatriculation du véhicule", example = "AB-123-CD")
    private String immatriculation;

    @Schema(description = "URL de la photo du véhicule", example = "https://exemple.com/voiture.jpg")
    private String urlPhoto;

    @NotNull(message = "Le statut est obligatoire")
    @Schema(description = "Statut de la voiture de service", example = "EN_SERVICE")
    private CompanyCarStatus status;
}
