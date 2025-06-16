package fr.diginamic.VroomVroomCar.controller;

import fr.diginamic.VroomVroomCar.dto.request.NotificationRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.NotificationResponseDto;
import fr.diginamic.VroomVroomCar.exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Cette interface définit les opérations du contrôleur pour la gestion des notifications.
 * Elle fournit des méthodes pour récupérer, créer et supprimer des notifications.
 */
@Tag(name = "Notifications", description = "API pour la gestion des notifications")
public interface INotificationController {

    /**
     * Récupère les notifications pour un utilisateur donné.
     *
     * @param userId L'ID de l'utilisateur pour lequel récupérer les notifications.
     * @param size Le nombre maximum de notifications à récupérer.
     * @return Une liste de {@link NotificationResponseDto} représentant les notifications de l'utilisateur.
     */
    @Operation(summary = "Récupère les notifications d'un utilisateur", description = "Récupère les notifications pour un utilisateur donné avec une taille de page spécifiée.")
    @ApiResponse(responseCode = "200", description = "Notifications récupérées avec succès", content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotificationResponseDto.class)))
    @GetMapping("/user/{userId}")
    ResponseEntity<List<NotificationResponseDto>> getUserNotifications(
            @Parameter(description = "ID de l'utilisateur pour lequel récupérer les notifications") @PathVariable Integer userId,
            @Parameter(description = "Nombre maximum de notifications à récupérer") @RequestParam(defaultValue = "5") int size);

    /**
     * Crée une nouvelle notification.
     *
     * @param dto Le DTO de requête contenant les informations nécessaires pour créer la notification.
     * @return Un {@link NotificationResponseDto} représentant la notification créée.
     * @throws ResourceNotFoundException Si l'utilisateur associé à la notification n'est pas trouvé.
     */
    @Operation(summary = "Crée une nouvelle notification", description = "Crée une nouvelle notification pour un utilisateur.")
    @ApiResponse(responseCode = "201", description = "Notification créée avec succès", content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotificationResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    @PostMapping("/create")
    ResponseEntity<NotificationResponseDto> createNotification(
            @Parameter(description = "DTO de requête contenant les informations nécessaires pour créer la notification") @Valid @RequestBody NotificationRequestDto dto) throws ResourceNotFoundException;

    /**
     * Supprime une notification par son ID.
     *
     * @param id L'ID de la notification à supprimer.
     * @return Un message de confirmation de suppression.
     * @throws ResourceNotFoundException Si la notification à supprimer n'est pas trouvée.
     */
    @Operation(summary = "Supprime une notification", description = "Supprime une notification par son ID.")
    @ApiResponse(responseCode = "200", description = "Notification supprimée avec succès")
    @ApiResponse(responseCode = "404", description = "Notification non trouvée")
    @DeleteMapping("/delete/{id}")
    ResponseEntity<String> deleteNotification(
            @Parameter(description = "ID de la notification à supprimer") @PathVariable Integer id) throws ResourceNotFoundException;
}