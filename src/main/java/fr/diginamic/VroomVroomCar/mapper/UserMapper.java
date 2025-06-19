package fr.diginamic.VroomVroomCar.mapper;

import fr.diginamic.VroomVroomCar.dto.request.UserRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.UserResponseDto;
import fr.diginamic.VroomVroomCar.entity.Status;
import fr.diginamic.VroomVroomCar.entity.User;
import fr.diginamic.VroomVroomCar.util.StatusUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class UserMapper {

    public final StatusUtil statusUtil;

    /**
     * Convertit un UserRequestDto et un utilisateur en une entité User.
     *
     * @param dto Le DTO de requête contenant les informations sur l'utilisateur.
     * @param password
     * @param status
     * @return Une nouvelle instance de User initialisée avec les données du DTO et de l'utilisateur.
     */
    public User toEntity(UserRequestDto dto, String password, Status status){
        return new User(
                dto.getNom(),
                dto.getPrenom(),
                dto.getMail(),
                convertLibelleCpVilleToAdresse(dto.getLibelle(), dto.getCodePostal(), dto.getVille()),
                password,
                status);
    }

    /**
     * Convertit les trois String d'entrée en un String adresse séparé par des ";"
     * @param libelle Le numéro et nom de rue, le numéro d'appartement etc..
     * @param codePostal Le code postal.
     * @param ville La ville.
     * @return
     */
    private String convertLibelleCpVilleToAdresse(String libelle, String codePostal, String ville){
        return Stream.of(libelle, codePostal, ville)
                .map(s -> s.replace(";", ",")) // sécurité
                .collect(Collectors.joining(";"));
    }

    public List<String> convertAdresseToLibelleCpVille(String adresse) throws IllegalArgumentException {
        String[] parts = adresse.split(";");
        if (parts.length != 3) {
            throw new IllegalArgumentException("L'adresse doit contenir exactement 3 parties séparées par ';'");
        }
        return Arrays.asList(parts[0].trim(), parts[1].trim(), parts[2].trim());
    }

    /**
     * Convertit une entité User en un UserResponseDto.
     *
     * @param user L'entité User à convertir.
     * @return Une nouvelle instance de USerResponseDto initialisée avec les données de l'entité User.
     */
    public UserResponseDto toResponseDto(User user){
        List<String> adresse = convertAdresseToLibelleCpVille(user.getAdresse());
        return new UserResponseDto(
                user.getId(),
                user.getNom(),
                user.getPrenom(),
                user.getMail(),
                adresse.get(0),
                adresse.get(1),
                adresse.get(2),
                statusUtil.convertStatusToString(user.getStatus())
        );
    }

    /**
     * Met à jour une entité User existante avec les données d'un UserRequestDto et d'un utilisateur.
     *
     * @param user L'entité User à mettre à jour.
     * @param userRequestDto Le DTO de requête contenant les nouvelles informations sur l'utilisateur.
     */
    public void updateEntity(User user, UserRequestDto userRequestDto) {
        user.setNom(userRequestDto.getNom());
        user.setPrenom(userRequestDto.getPrenom());
        user.setMail(userRequestDto.getMail());
        user.setAdresse(convertLibelleCpVilleToAdresse
                (userRequestDto.getLibelle(), userRequestDto.getCodePostal(), userRequestDto.getVille()));
    }

}
