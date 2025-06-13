package fr.diginamic.VroomVroomCar.dto.response;

import fr.diginamic.VroomVroomCar.entity.Categorie;
import fr.diginamic.VroomVroomCar.entity.Motorisation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Cette classe représente un DTO (Data Transfer Object) pour les réponses relatives à une voiture.
 * Elle est utilisée pour transférer les données de réponse relatives à une voiture entre les couches de l'application.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO pour les réponses relatives à une voiture.")
public class CarResponseDto {

    @Schema(description = "L'identifiant unique de la voiture", example = "1")
    private Integer id;

    @Schema(description = "La marque de la voiture", example = "Toyota")
    private String marque;

    @Schema(description = "Le modèle de la voiture", example = "Yaris")
    private String modele;

    @Schema(description = "Le nombre de places de la voiture", example = "5")
    private int nbDePlaces;

    @Schema(description = "Le niveau de pollution de la voiture", example = "120 g/km")
    private String pollution;

    @Schema(description = "Informations supplémentaires sur la voiture", example = "Pas de place dans le coffre.")
    private String infosSupp;

    @Schema(description = "L'identifiant de l'utilisateur associé à cette voiture", example = "1")
    private Integer utilisateurId;

    @Schema(description = "Le nom de l'utilisateur associé à cette voiture", example = "Didier Mazier")
    private String utilisateurNom;

    @Schema(description = "La motorisation de la voiture")
    private Motorisation motorisation;

    @Schema(description = "La catégorie de la voiture")
    private Categorie categorie;

    @Schema(description = "Émissions de CO2 par kilomètre", example = "120.5")
    private Double co2ParKm;
}