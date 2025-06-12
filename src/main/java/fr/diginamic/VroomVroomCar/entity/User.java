package fr.diginamic.VroomVroomCar.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "utilisateur")
public class User {
    @Id
    private Integer id;
    private String nom;
    private String prenom;
    private String mail;
    private String adresse;
    private String motDePasse;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany
    @JoinColumn(name = "voiture_id")
    private Set<Car> cars;

    @OneToMany
    @JoinColumn(name = "trip_id")
    private Set<Trip> trips;

    @OneToMany(mappedBy = "utilisateur")
    private Set<Subscribe> subscribes;

    @OneToMany
    @JoinColumn(name = "notification_id")
    private Set<Notification> notifications;

    @OneToMany
    @JoinColumn(name = "reservation_id")
    private Set<Reservation> reservations;

    public User(String nom, String prenom, String mail, String adresse, String motDePasse) {
        this.nom = nom;
        this.prenom = prenom;
        this.mail = mail;
        this.adresse = adresse;
        this.motDePasse = motDePasse;
    }
}
