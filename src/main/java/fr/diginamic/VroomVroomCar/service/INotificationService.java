package fr.diginamic.VroomVroomCar.service;

import fr.diginamic.VroomVroomCar.dto.request.NotificationRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.NotificationResponseDto;
import fr.diginamic.VroomVroomCar.exception.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Cette interface définit les opérations de service pour la gestion des notifications.
 * Elle fournit des méthodes pour récupérer, créer et supprimer des notifications.
 */
public interface INotificationService {

    /**
     * Récupère les notifications pour un utilisateur donné.
     *
     * @param userId L'ID de l'utilisateur pour lequel récupérer les notifications.
     * @param limit Le nombre maximum de notifications à récupérer.
     * @return Une liste de {@link NotificationResponseDto} représentant les notifications de l'utilisateur.
     * @throws ResourceNotFoundException Si l'utilisateur avec l'ID spécifié n'est pas trouvé.
     */
    @Transactional(readOnly = true)
    List<NotificationResponseDto> getUserNotifications(Integer userId, int limit) throws ResourceNotFoundException;

    /**
     * Crée une nouvelle notification.
     *
     * @param dto Le DTO de requête contenant les informations nécessaires pour créer la notification.
     * @return Un {@link NotificationResponseDto} représentant la notification créée.
     */
    NotificationResponseDto createNotification(NotificationRequestDto dto);

    /**
     * Supprime une notification par son ID.
     *
     * @param id L'ID de la notification à supprimer.
     */
    void deleteNotification(Integer id);
}