package fr.diginamic.VroomVroomCar.mapper;

import fr.diginamic.VroomVroomCar.dto.request.NotificationRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.NotificationResponseDto;
import fr.diginamic.VroomVroomCar.entity.Notification;
import fr.diginamic.VroomVroomCar.entity.User;
import org.springframework.stereotype.Component;

/**
 * Cette classe est un composant Spring qui fournit des méthodes pour mapper
 * entre les DTOs de requête et de réponse et l'entité Notification.
 */
@Component
public class NotificationMapper {

    /**
     * Convertit un NotificationRequestDto et un utilisateur en une entité Notification.
     *
     * @param dto Le DTO de requête contenant les informations sur la notification.
     * @param user L'utilisateur associé à la notification.
     * @return Une nouvelle instance de Notification initialisée avec les données du DTO et de l'utilisateur.
     */
    public Notification toEntity(NotificationRequestDto dto, User user) {
        Notification notification = new Notification();
        notification.setContenu(dto.getContenu());
        notification.setNom(dto.getNom());
        notification.setDate(dto.getDate());
        notification.setUser(user);
        return notification;
    }

    /**
     * Convertit une entité Notification en un NotificationResponseDto.
     *
     * @param notification L'entité Notification à convertir.
     * @return Une nouvelle instance de NotificationResponseDto initialisée avec les données de l'entité Notification.
     */
    public NotificationResponseDto toResponseDto(Notification notification) {
        NotificationResponseDto dto = new NotificationResponseDto();
        dto.setId(notification.getId());
        dto.setContenu(notification.getContenu());
        dto.setNom(notification.getNom());
        dto.setDate(notification.getDate());
        dto.setUserId(notification.getUser().getId());
        dto.setUserName(notification.getUser().getPrenom() +  " " + notification.getUser().getNom());
        return dto;
    }

    /**
     * Met à jour une entité Notification existante avec les données d'un NotificationRequestDto et d'un utilisateur.
     *
     * @param notification L'entité Notification à mettre à jour.
     * @param dto Le DTO de requête contenant les nouvelles informations sur la notification.
     * @param user Le nouvel utilisateur associé à la notification.
     */
    public void updateEntity(Notification notification, NotificationRequestDto dto, User user) {
        notification.setContenu(dto.getContenu());
        notification.setNom(dto.getNom());
        notification.setDate(dto.getDate());
        notification.setUser(user);
    }
}
