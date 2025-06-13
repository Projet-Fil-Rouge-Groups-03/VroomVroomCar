package fr.diginamic.VroomVroomCar.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Motorisation du véhicule")
public enum Motorisation {
    ESSENCE,
    HYBRIDE,
    ELECTRIQUE
}
