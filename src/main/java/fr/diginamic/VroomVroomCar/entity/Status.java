package fr.diginamic.VroomVroomCar.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Statut du compte utilisateur, définit ses droits dans l'application")
public enum Status {
    ROLE_ACTIF,
    ROLE_BANNI,
    ROLE_ADMIN
}
