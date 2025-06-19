package fr.diginamic.VroomVroomCar.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Définit le type de véhicule pour les filtres")
public enum VehiculeType {
    VOITURE_SERVICE,
    VOITURE_COVOIT,
    TOUS
}
