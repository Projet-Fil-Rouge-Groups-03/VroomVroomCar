package fr.diginamic.VroomVroomCar.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Catégorie du véhicule")
public enum Categorie {
    MICRO_URBAINES,
    MINI_CITADINES,
    CITADINES,
    POLYVALENTES,
    COMPACTES,
    BERLINES_TAILLE_S,
    BERLINES_TAILLE_M,
    BERLINES_TAILLE_L,
    SUV,TOUT_TERRAINS,
    PICK_UP
}
