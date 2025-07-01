package fr.diginamic.VroomVroomCar.service;

import fr.diginamic.VroomVroomCar.dto.TripNotificationDetailsDto;
import fr.diginamic.VroomVroomCar.entity.*;
import fr.diginamic.VroomVroomCar.entity.SubscribeKey;
import fr.diginamic.VroomVroomCar.mapper.NotificationMapper;
import fr.diginamic.VroomVroomCar.repository.NotificationRepository;
import fr.diginamic.VroomVroomCar.repository.UserRepository;
import fr.diginamic.VroomVroomCar.util.NotificationUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    // --- Mocks et Injections (inchangés) ---
    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NotificationMapper notificationMapper;

    @Mock
    private NotificationUtil notificationUtil;

    @InjectMocks
    private NotificationService notificationService;


    // --- Méthodes de création d'objets de test (améliorées) ---

    // Helper amélioré pour créer des utilisateurs distincts
    private User createUserWithId(Integer id) {
        User user = new User();
        user.setId(id);
        user.setPrenom("Prenom" + id);
        user.setNom("Nom" + id);
        return user;
    }

    private Car createCar() {
        Car car = new Car();
        car.setId(1);
        car.setMarque("Toyota");
        car.setModele("Yaris");
        return car;
    }

    // Helper pour créer un trajet simple
    private Trip createTrip(User organisateur) {
        Trip trip = new Trip();
        trip.setId(1);
        trip.setDateDebut(Date.valueOf(LocalDate.now()));
        trip.setDateFin(Date.valueOf(LocalDate.now().plusDays(1)));
        trip.setHeureDepart(LocalTime.now());
        trip.setLieuDepart("Gare Montparnasse");
        trip.setLieuArrivee("Gare Lille Flandres");
        trip.setVilleDepart("Paris");
        trip.setVilleArrivee("Lille");
        trip.setNbPlacesRestantes(4);
        trip.setOrganisateur(organisateur);
        trip.setCar(createCar());
        // Initialiser la collection pour éviter les NullPointerExceptions
        trip.setSubscribes(new HashSet<>());
        return trip;
    }

    // Helper pour créer une inscription
    private Subscribe createSubscribe(User user, Trip trip) {
        SubscribeKey key = new SubscribeKey(user.getId(), trip.getId());
        Subscribe subscribe = new Subscribe();
        subscribe.setId(key);
        subscribe.setUser(user);
        subscribe.setTrip(trip);
        return subscribe;
    }

    // --- Tests (inchangés car leur logique reste valide) ---

    @Test
    void testSendNotificationToOrganisateurOnSubscribe() {
        // Arrange
        User organisateur = createUserWithId(1);
        User participant = createUserWithId(2);
        Trip trip = createTrip(organisateur);

        doNothing().when(notificationUtil).createAndSaveNotification(anyString(), anyString(), any(User.class));

        // Act
        notificationService.sendNotificationToOrganisateurOnSubscribe(trip, participant);

        // Assert
        verify(notificationUtil, times(1)).createAndSaveNotification(anyString(), anyString(), eq(organisateur));
    }

    @Test
    void testSendNotificationToOrganisateurOnUnsubscribe() {
        // Arrange
        User organisateur = createUserWithId(1);
        User participant = createUserWithId(2);
        Trip trip = createTrip(organisateur);

        doNothing().when(notificationUtil).createAndSaveNotification(anyString(), anyString(), any(User.class));

        // Act
        notificationService.sendNotificationToOrganisateurOnUnsubscribe(trip, participant);

        // Assert
        verify(notificationUtil, times(1)).createAndSaveNotification(anyString(), anyString(), eq(organisateur));
    }


    // --- Tests corrigés ---

    @Test
    void testSendNotificationToParticipantsOnModification() {
        // 1. Arrange (Préparation)
        User organisateur = createUserWithId(1);
        User participant1 = createUserWithId(2);
        User participant2 = createUserWithId(3);
        Trip trip = createTrip(organisateur);

        // Ajouter des inscriptions au trajet
        trip.getSubscribes().add(createSubscribe(participant1, trip));
        trip.getSubscribes().add(createSubscribe(participant2, trip));

        // On mock la méthode qui est VRAIMENT appelée maintenant
        doNothing().when(notificationUtil).createAndSaveNotification(anyString(), anyString(), any(User.class));

        // 2. Act (Action)
        // La signature de la méthode a changé
        notificationService.sendNotificationToParticipantsOnModification(trip);

        // 3. Assert (Vérification)
        // On vérifie que la méthode de création a été appelée 2 fois (une par participant)
        // On vérifie aussi le "nom" de la notification pour être plus précis
        verify(notificationUtil, times(2)).createAndSaveNotification(anyString(), eq("Modification de trajet"), any(User.class));

        // Vérification avancée : s'assurer que les BONS utilisateurs ont été notifiés
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(notificationUtil, times(2)).createAndSaveNotification(anyString(), anyString(), userCaptor.capture());

        List<User> notifiedUsers = userCaptor.getAllValues();
        assertTrue(notifiedUsers.contains(participant1));
        assertTrue(notifiedUsers.contains(participant2));
    }

    @Test
    void testSendNotificationToParticipantsOnAnnulation() {
        // 1. Arrange (Préparation)
        User organisateur = createUserWithId(1);
        User participant1 = createUserWithId(2);
        User participant2 = createUserWithId(3);
        Trip trip = createTrip(organisateur);

        // La méthode attend maintenant une LISTE d'inscriptions et un DTO
        List<Subscribe> subscriptions = Arrays.asList(
                createSubscribe(participant1, trip),
                createSubscribe(participant2, trip)
        );
        TripNotificationDetailsDto detailsDto = new TripNotificationDetailsDto(trip);

        // On mock la méthode qui est VRAIMENT appelée
        doNothing().when(notificationUtil).createAndSaveNotification(anyString(), anyString(), any(User.class));

        // 2. Act (Action)
        // On appelle la méthode avec sa nouvelle signature
        notificationService.sendNotificationToParticipantsOnAnnulation(subscriptions, detailsDto);

        // 3. Assert (Vérification)
        // On vérifie que la méthode de création a été appelée 2 fois
        verify(notificationUtil, times(2)).createAndSaveNotification(anyString(), eq("Annulation de trajet"), any(User.class));

        // Vérification avancée : s'assurer que les BONS utilisateurs ont été notifiés
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(notificationUtil, times(2)).createAndSaveNotification(anyString(), anyString(), userCaptor.capture());

        List<User> notifiedUsers = userCaptor.getAllValues();
        assertTrue(notifiedUsers.contains(participant1));
        assertTrue(notifiedUsers.contains(participant2));
    }
}