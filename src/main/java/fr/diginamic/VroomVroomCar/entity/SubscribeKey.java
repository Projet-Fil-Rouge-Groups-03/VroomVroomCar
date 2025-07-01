package fr.diginamic.VroomVroomCar.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class SubscribeKey implements Serializable {
    @Column(name = "utilisateur_id")
    Integer userId;
    @Column(name = "trajet_id")
    Integer tripId;

}
