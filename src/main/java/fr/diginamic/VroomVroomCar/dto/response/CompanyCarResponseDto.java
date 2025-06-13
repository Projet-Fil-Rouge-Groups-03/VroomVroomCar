package fr.diginamic.VroomVroomCar.dto.response;

import fr.diginamic.VroomVroomCar.entity.Categorie;
import fr.diginamic.VroomVroomCar.entity.CompanyCarStatus;
import fr.diginamic.VroomVroomCar.entity.Motorisation;
import fr.diginamic.VroomVroomCar.entity.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Cette classe représente un DTO (Data Transfer Object) pour les réponses relatives à une voiture de service.
 * Elle est utilisée pour transférer les données de réponse relatives à une voiture de service entre les couches de l'application.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO pour les réponses relatives à une voiture de service.")
public class CompanyCarResponseDto {

    // Champs hérités de Car

    @Schema(description = "L'identifiant unique de la voiture", example = "1")
    private Integer id;

    @Schema(description = "La marque du véhicule", example = "Peugeot")
    private String marque;

    @Schema(description = "Le modèle du véhicule", example = "308")
    private String modele;

    @Schema(description = "Le nombre de places", example = "5")
    private int nbDePlaces;

    @Schema(description = "Informations supplémentaires sur le véhicule", example = "Avec attelage")
    private String infosSupp;

    @Schema(description = "ID de l'utilisateur assigné à cette voiture", example = "3")
    private Integer utilisateurId;

    @Schema(description = "Motorisation du véhicule", example = "ELECTRIQUE")
    private Motorisation motorisation;

    @Schema(description = "Catégorie du véhicule", example = "SUV")
    private Categorie categorie;

    @Schema(description = "Émissions de CO2 par kilomètre", example = "95.4")
    private Double co2;

    // Champs spécifiques à CompanyCar

    @Schema(description = "Numéro d'immatriculation du véhicule", example = "AB-456-CD")
    private String immatriculation;

    @Schema(description = "URL de la photo du véhicule", example = "https://site.com/voitures/peugeot-308.jpg")
    private String urlPhoto;

    @Schema(description = "Statut actuel de la voiture de service", example = "EN_SERVICE")
    private CompanyCarStatus status;
}
