package fr.diginamic.VroomVroomCar.dto.request;

import fr.diginamic.VroomVroomCar.entity.Categorie;
import fr.diginamic.VroomVroomCar.entity.Motorisation;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarRequestDto {

    @NotBlank(message = "La marque est obligatoire")
    private String marque;

    @NotBlank(message = "Le modèle est obligatoire")
    private String modele;

    @Min(value = 1, message = "Le nombre de places doit être au minimum de 1")
    @Max(value = 10, message = "Le nombre de places doit être au maximum de 10")
    private int nbDePlaces;

    private String pollution;
    private String infosSupp;

    @NotNull(message = "L'utilisateur est obligatoire")
    private Integer utilisateurId;

    private Motorisation motorisation;
    private Categorie categorie;
}
