package fr.diginamic.VroomVroomCar.mapper;

import fr.diginamic.VroomVroomCar.dto.request.CarRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.CarResponseDto;
import fr.diginamic.VroomVroomCar.entity.Car;
import fr.diginamic.VroomVroomCar.entity.User;
import org.springframework.stereotype.Component;


/**
 * Cette classe est un composant Spring qui fournit des méthodes pour mapper
 * entre les DTOs de requête et de réponse et l'entité Car.
 */
@Component
public class CarMapper {

    /**
     * Convertit un CarRequestDto et un utilisateur en une entité Car.
     *
     * @param dto Le DTO de requête contenant les informations sur la voiture.
     * @param user L'utilisateur associé à la voiture.
     * @return Une nouvelle instance de Car initialisée avec les données du DTO et de l'utilisateur.
     */
    public Car toEntity(CarRequestDto dto, User user) {
        Car car = new Car();
        car.setMarque(dto.getMarque());
        car.setModele(dto.getModele());
        car.setNbDePlaces(dto.getNbDePlaces());
        car.setPollution(dto.getPollution());
        car.setInfosSupp(dto.getInfosSupp());
        car.setUser(user);
        car.setMotorisation(dto.getMotorisation());
        car.setCategories(dto.getCategorie());
        return car;
    }

    /**
     * Convertit une entité Car en un CarResponseDto.
     *
     * @param car L'entité Car à convertir.
     * @return Une nouvelle instance de CarResponseDto initialisée avec les données de l'entité Car.
     */
    public CarResponseDto toResponseDto(Car car) {
        CarResponseDto dto = new CarResponseDto();
        dto.setId(car.getId());
        dto.setMarque(car.getMarque());
        dto.setModele(car.getModele());
        dto.setNbDePlaces(car.getNbDePlaces());
        dto.setPollution(car.getPollution());
        dto.setInfosSupp(car.getInfosSupp());
        dto.setUtilisateurId(car.getUser().getId());
        dto.setUtilisateurNom(car.getUser().getNom());
        dto.setMotorisation(car.getMotorisation());
        dto.setCategorie(car.getCategories());
        return dto;
    }

    /**
     * Met à jour une entité Car existante avec les données d'un CarRequestDto et d'un utilisateur.
     *
     * @param car L'entité Car à mettre à jour.
     * @param dto Le DTO de requête contenant les nouvelles informations sur la voiture.
     * @param user Le nouvel utilisateur associé à la voiture.
     */
    public void updateEntity(Car car, CarRequestDto dto, User user) {
        car.setMarque(dto.getMarque());
        car.setModele(dto.getModele());
        car.setNbDePlaces(dto.getNbDePlaces());
        car.setPollution(dto.getPollution());
        car.setInfosSupp(dto.getInfosSupp());
        car.setUser(user);
        car.setMotorisation(dto.getMotorisation());
        car.setCategories(dto.getCategorie());
    }
}
