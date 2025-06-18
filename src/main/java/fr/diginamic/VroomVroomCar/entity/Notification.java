package fr.diginamic.VroomVroomCar.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    private LocalDateTime date;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Notification(String contenu, String nom, LocalDateTime date, User user) {
        this.contenu = contenu;
        this.nom = nom;
        this.date = date;
        this.user = user;
    }

    @PrePersist
    protected void onCreate() {
        this.date = LocalDateTime.now();
    }
}
