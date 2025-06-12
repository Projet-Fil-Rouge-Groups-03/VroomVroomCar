package fr.diginamic.VroomVroomCar.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "voiture")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "La marque est obligatoire")
    @Column(nullable = false, length = 30)
    private String marque;

    @NotBlank(message = "Le modèle est obligatoire")
    @Column(nullable = false, length = 30)
    private String modele;

    @Min(value = 1)
    @Max(value = 10)
    @Column(name = "nb_places", nullable = false)
    private int nbDePlaces;

    @Column(length = 30)
    private String pollution;

    @Lob
    @Column(name = "infos_supp")
    private String infosSupp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateurId", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private Motorisation motorisation;

    @Enumerated(EnumType.STRING)
    private Categorie categories;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Trip> trips = new HashSet<>();

    public Car(String marque, String modele, int nbDePlaces, String pollution, String infosSupp, User user, Motorisation motorisation, Categorie categories, Set<Trip> trips) {
        this.marque = marque;
        this.modele = modele;
        this.nbDePlaces = nbDePlaces;
        this.pollution = pollution;
        this.infosSupp = infosSupp;
        this.user = user;
        this.motorisation = motorisation;
        this.categories = categories;
        this.trips = trips;
    }
}
