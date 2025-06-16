package fr.diginamic.VroomVroomCar.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Le contenu est obligatoire")
    @Column(nullable = false)
    private String contenu;

    @NotBlank(message = "Le nom est obligatoire")
    @Column(nullable = false, length = 30)
    private String nom;

    @NotNull
    @Column(nullable = false)
    private Date date;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Notification(String contenu, String nom, Date date, User user) {
        this.contenu = contenu;
        this.nom = nom;
        this.date = date;
        this.user = user;
    }
}
