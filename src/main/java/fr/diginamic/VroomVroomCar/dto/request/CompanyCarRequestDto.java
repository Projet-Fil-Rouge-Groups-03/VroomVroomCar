package fr.diginamic.VroomVroomCar.dto.request;

import fr.diginamic.VroomVroomCar.entity.Categorie;
import fr.diginamic.VroomVroomCar.entity.CompanyCarStatus;
import fr.diginamic.VroomVroomCar.entity.Motorisation;
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
public class CompanyCarRequestDto {

    // Champs hérités de Car

    @NotBlank(message = "La marque est obligatoire")
    private String marque;

    @NotBlank(message = "Le modèle est obligatoire")
    private String modele;

    @Min(value = 1, message = "Le nombre de places doit être au minimum de 1")
    @Max(value = 10, message = "Le nombre de places doit être au maximum de 10")
    private int nbDePlaces;

    private String pollution;

    private Integer utilisateurId;

    private Motorisation motorisation;
    private Categorie categorie;

    // Champs spécifiques à CompanyCar

    @NotBlank(message = "L'immatriculation est obligatoire")
    private String immatriculation;

    private String urlPhoto;

    @NotNull(message = "Le statut est obligatoire")
    private CompanyCarStatus status;

    @NotNull(message = "L'ID de la voiture est obligatoire")
    private Integer voitureId;
}
