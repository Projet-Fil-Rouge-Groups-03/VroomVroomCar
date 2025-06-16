package fr.diginamic.VroomVroomCar.mapper;

import fr.diginamic.VroomVroomCar.dto.request.CompanyCarRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.CompanyCarResponseDto;
import fr.diginamic.VroomVroomCar.entity.CompanyCar;
import fr.diginamic.VroomVroomCar.entity.User;
import org.springframework.stereotype.Component;

/**
 * Cette classe est un composant Spring qui fournit des méthodes pour mapper
 * entre les DTOs de requête et de réponse et l'entité CompanyCar.
 */
@Component
public class CompanyCarMapper {

    /**
     * Convertit un CompanyCarRequestDto et un utilisateur en une entité CompanyCar.
     *
     * @param dto Le DTO de requête contenant les informations sur la voiture.
     * @param user L'utilisateur associé à la voiture.
     * @return Une nouvelle instance de Car initialisée avec les données du DTO et de l'utilisateur.
     */
    public CompanyCar toEntity(CompanyCarRequestDto dto, User user) {
        CompanyCar companyCar = new CompanyCar();

        // Champs hérités de Car
        companyCar.setMarque(dto.getMarque());
        companyCar.setModele(dto.getModele());
        companyCar.setNbDePlaces(dto.getNbDePlaces());
        companyCar.setPollution(dto.getPollution());
        companyCar.setInfosSupp(dto.getInfosSupp());
        companyCar.setUser(user);
        companyCar.setMotorisation(dto.getMotorisation());
        companyCar.setCategories(dto.getCategorie());

        // Champs spécifiques à CompanyCar
        companyCar.setImmatriculation(dto.getImmatriculation());
        companyCar.setUrlPhoto(dto.getUrlPhoto());
        companyCar.setStatus(dto.getStatus());

        return companyCar;
    }

    /**
     * Convertit une entité CompanyCar en un CompanyCarResponseDto.
     *
     * @param companyCar L'entité CompanyCar à convertir.
     * @return Une nouvelle instance de CarResponseDto initialisée avec les données de l'entité CompanyCar.
     */
    public CompanyCarResponseDto toResponseDto(CompanyCar companyCar) {
        CompanyCarResponseDto dto = new CompanyCarResponseDto();

        // Champs hérités de Car
        dto.setId(companyCar.getId());
        dto.setMarque(companyCar.getMarque());
        dto.setModele(companyCar.getModele());
        dto.setNbDePlaces(companyCar.getNbDePlaces());
        dto.setPollution(companyCar.getPollution());
        dto.setInfosSupp(companyCar.getInfosSupp());
        dto.setUtilisateurId(companyCar.getUser().getId());
        dto.setMotorisation(companyCar.getMotorisation());
        dto.setCategorie(companyCar.getCategories());

        // Champs spécifiques à CompanyCar
        dto.setImmatriculation(companyCar.getImmatriculation());
        dto.setUrlPhoto(companyCar.getUrlPhoto());
        dto.setStatus(companyCar.getStatus());

        return dto;
    }

    /**
     * Met à jour une entité Car existante avec les données d'un CarRequestDto et d'un utilisateur.
     *
     * @param companyCar L'entité Car à mettre à jour.
     * @param dto Le DTO de requête contenant les nouvelles informations sur la voiture.
     * @param user Le nouvel utilisateur associé à la voiture.
     */
    public void updateEntity(CompanyCar companyCar, CompanyCarRequestDto dto, User user) {
        // Champs hérités de Car
        companyCar.setMarque(dto.getMarque());
        companyCar.setModele(dto.getModele());
        companyCar.setNbDePlaces(dto.getNbDePlaces());
        companyCar.setPollution(dto.getPollution());
        companyCar.setInfosSupp(dto.getInfosSupp());
        companyCar.setUser(user);
        companyCar.setMotorisation(dto.getMotorisation());
        companyCar.setCategories(dto.getCategorie());

        // Champs spécifiques à CompanyCar
        companyCar.setImmatriculation(dto.getImmatriculation());
        companyCar.setUrlPhoto(dto.getUrlPhoto());
        companyCar.setStatus(dto.getStatus());
    }

}
