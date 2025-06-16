package fr.diginamic.VroomVroomCar.service;

import fr.diginamic.VroomVroomCar.dto.request.NotificationRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.NotificationResponseDto;
import fr.diginamic.VroomVroomCar.entity.*;
import fr.diginamic.VroomVroomCar.exception.ResourceNotFoundException;
import fr.diginamic.VroomVroomCar.mapper.NotificationMapper;
import fr.diginamic.VroomVroomCar.repository.NotificationRepository;
import fr.diginamic.VroomVroomCar.repository.UserRepository;
import fr.diginamic.VroomVroomCar.util.NotificationUtil;
import fr.diginamic.VroomVroomCar.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService implements INotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final NotificationMapper notificationMapper;
    private final NotificationUtil notificationUtil;


    // GET by User ID

    @Transactional(readOnly = true)
    @Override
    public List<NotificationResponseDto> getUserNotifications(Integer userId, int limit) {
        ValidationUtil.validateIdNotNull(userId, "l'utilisateur");
        Pageable pageable = PageRequest.of(0, limit);
        return notificationRepository.findByUserIdOrderByDateDesc(userId, pageable).stream()
                .map(notificationMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // CREATE

    @Override
    public NotificationResponseDto createNotification(NotificationRequestDto dto) throws ResourceNotFoundException  {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'ID: " + dto.getUserId()));
        Notification notification = notificationMapper.toEntity(dto, user);
        Notification savedNotification = notificationRepository.save(notification);
        return notificationMapper.toResponseDto(savedNotification);
    }

    // DELETE (au cas où)

    @Override
    public void deleteNotification(Integer id) throws ResourceNotFoundException {
        ValidationUtil.validateIdNotNull(id, "la notification");
        if (!notificationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Notification non trouvée avec l'ID: " + id);
        }
        notificationRepository.deleteById(id);
    }

    // Envoi des Notifications

    public void sendNotificationToOrganisateurOnSubscribe(Trip trip, User participant) {
        String contenu = String.format("%s s'est inscrit à votre covoiturage du %s",
                NotificationUtil.getFullName(participant),
                NotificationUtil.formatTripDate(trip));

        notificationUtil.createAndSaveNotification(contenu, "Inscription", trip.getOrganisateur());
        // TODO : ajouter dans Create de subscribeService : notificationService.sendNotificationToOrganisateurOnSubscribe(trip, user);
        // Attention - l'organisateurID a qui envoyé la notif est dans Trip.getOrganisateur (lié à la table User)
    }

    public void sendNotificationToOrganisateurOnUnsubscribe(Trip trip, User participant) {
        String contenu = String.format("%s s'est désinscrit de votre covoiturage du %s",
                NotificationUtil.getFullName(participant),
                NotificationUtil.formatTripDate(trip));

        notificationUtil.createAndSaveNotification(contenu, "Désinscription", trip.getOrganisateur());
        // TODO : ajouter dans subscribeService à la suppression : notificationService.sendNotificationToOrganisateurOnUnsubscribe(trip, user);
        // Attention - l'organisateurID a qui envoyé la notif est dans Trip.getOrganisateur (lié à la table User)
    }

    public void sendNotificationToParticipantsOnModification(Trip trip, User organisateur) {
        String contenu = String.format("%s à fait une modification sur votre covoiturage du %s",
                NotificationUtil.getFullName(organisateur),
                NotificationUtil.formatTripDate(trip));

        notificationUtil.sendNotificationToAllParticipants(trip, contenu, "Modification Covoit");
    }

    public void sendNotificationToParticipantsOnAnnulation(Trip trip, User organisateur) {
        String contenu = String.format("%s a annulé votre covoiturage du %s",
                NotificationUtil.getFullName(organisateur),
                NotificationUtil.formatTripDate(trip));

        notificationUtil.sendNotificationToAllParticipants(trip, contenu, "Annulation Covoit");
    }

    public void sendNotificationToUsersOnCarStatusUpdate(Car car, String newStatus, User user) {
        String contenu = String.format("Votre réservation de %s %s a été annulée : %s",
                car.getMarque(), car.getModele(), newStatus);

        notificationUtil.createAndSaveNotification(contenu, "Annulation réservation", user);
    }

}
// TODO : vérifier que findByTrip soit bien dans subscribeRepository