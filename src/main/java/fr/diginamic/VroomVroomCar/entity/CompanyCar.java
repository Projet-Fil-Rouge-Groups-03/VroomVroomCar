package fr.diginamic.VroomVroomCar.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.HashSet;
import java.util.Set;

/**
 * Cette classe représente une voiture de société dans le système.
 * Elle étend la classe Car pour inclure des informations spécifiques aux voitures de société.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "voiture_de_service")
public class CompanyCar extends Car {

    /**
     * L'immatriculation de la voiture de société.
     * Ce champ est obligatoire et doit être unique.
     */
    @NotBlank(message = "L'immatriculation est obligatoire")
    @Column(nullable = false, length = 30, unique = true)
    private String immatriculation;

    /**
     * L'URL de la photo de la voiture de société.
     */
    @Column(name = "url_photo", length = 255)
    private String urlPhoto;

    /**
     * Le statut de la voiture de société.
     * Ce champ est obligatoire.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CompanyCarStatus status;


    /**
     * Les réservations associées à cette voiture de société.
     * Les réservations sont chargées de manière paresseuse et toutes les opérations de persistance
     * sont cascadées depuis la voiture de société.
     */
    @OneToMany(mappedBy = "companyCar", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Reservation> reservations = new HashSet<>();
}
