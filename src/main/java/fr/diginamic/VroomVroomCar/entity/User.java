package fr.diginamic.VroomVroomCar.entity;

import fr.diginamic.VroomVroomCar.config.StringCryptoConverter;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "utilisateur")
@ToString(exclude = {"cars", "trips", "subscribes", "notifications", "reservations"})
@EqualsAndHashCode(of = "id")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nom;
    private String prenom;

    @Column(unique = true, nullable = false)
    @Convert(converter = StringCryptoConverter.class)
    private String mail;
    @Convert(converter = StringCryptoConverter.class)
    private String ville;
    @Convert(converter = StringCryptoConverter.class)
    private String codePostal;
    @Convert(converter = StringCryptoConverter.class)
    private String libelle;

    private String motDePasse;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Car> cars;

    @OneToMany(mappedBy = "organisateur", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private Set<Trip> trips;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Subscribe> subscribes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Notification> notifications;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Reservation> reservations;

    public User(String nom, String prenom, String mail, String ville, String codePostal, String libelle, String motDePasse, Status status) {
        this.nom = nom;
        this.prenom = prenom;
        this.mail = mail;
        this.ville = ville;
        this.codePostal = codePostal;
        this.libelle = libelle;
        this.motDePasse = motDePasse;
        this.status = status;
    }
}
