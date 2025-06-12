package fr.diginamic.VroomVroomCar.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "voiture")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String marque;
    private String modele;
    private int nb_de_places;
    private String pollution;
    private String type;
    private String infos_supp;

    @Enumerated(EnumType.STRING)
    private Categorie categorie;

    @Enumerated(EnumType.STRING)
    private Motorisation motorisation;

    @OneToMany
    private Trip trip;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
