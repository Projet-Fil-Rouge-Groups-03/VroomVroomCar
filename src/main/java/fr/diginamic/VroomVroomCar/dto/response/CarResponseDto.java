package fr.diginamic.VroomVroomCar.dto.response;

import fr.diginamic.VroomVroomCar.entity.Categorie;
import fr.diginamic.VroomVroomCar.entity.Motorisation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarResponseDto {

    private Integer id;
    private String marque;
    private String modele;
    private int nbDePlaces;
    private String pollution;
    private String infosSupp;
    private Integer utilisateurId;
    private String utilisateurNom;
    private Motorisation motorisation;
    private Categorie categorie;

}
