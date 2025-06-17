package fr.diginamic.VroomVroomCar.util;

import fr.diginamic.VroomVroomCar.entity.Notification;
import fr.diginamic.VroomVroomCar.entity.Subscribe;
import fr.diginamic.VroomVroomCar.entity.Trip;
import fr.diginamic.VroomVroomCar.entity.User;
import fr.diginamic.VroomVroomCar.repository.NotificationRepository;
import fr.diginamic.VroomVroomCar.repository.SubscribeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Utilitaire pour la gestion des notifications.
 * Cette classe fournit des méthodes pour créer, envoyer et formater des notifications.
 */
@Component
@RequiredArgsConstructor
public class NotificationUtil {

    private final NotificationRepository notificationRepository;
    private final SubscribeRepository subscribeRepository;

    /**
     * Crée et sauvegarde une notification pour un utilisateur spécifique.
     *
     * @param contenu Le contenu de la notification.
     * @param nom Le nom ou le titre de la notification.
     * @param user L'utilisateur destinataire de la notification.
     */
    public void createAndSaveNotification(String contenu, String nom, User user) {
        Notification notification = Notification.builder()
                .contenu(contenu)
                .nom(nom)
                .user(user)
                .build();

        notificationRepository.save(notification);
    }

    /**
     * Envoie une notification à tous les participants d'un voyage (trip).
     *
     * @param trip Le voyage pour lequel envoyer les notifications.
     * @param contenu Le contenu de la notification à envoyer.
     * @param type Le type ou le titre de la notification.
     */
    public void sendNotificationToAllParticipants(Trip trip, String contenu, String type) {
        List<Subscribe> inscriptions = subscribeRepository.findByTrip(trip);
        inscriptions.forEach(inscription ->
                createAndSaveNotification(contenu, type, inscription.getUser())
        );
    }

    /**
     * Génère le nom complet d'un utilisateur à partir de son prénom et de son nom.
     *
     * @param user L'utilisateur dont on veut obtenir le nom complet.
     * @return Le nom complet de l'utilisateur.
     */
    public static String getFullName(User user) {
        return user.getPrenom() + " " + user.getNom();
    }

    /**
     * Formate la date de début d'un voyage en une chaîne de caractères.
     *
     * @param trip Le voyage dont on veut formater la date de début.
     * @return La date de début du voyage formatée en tant que chaîne de caractères.
     */
    public static String formatTripDate(Trip trip) {
        return DateUtil.formatToFrench(trip.getDateDebut().toLocalDate());
    }



}
