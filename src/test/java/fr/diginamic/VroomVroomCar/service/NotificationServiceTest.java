package fr.diginamic.VroomVroomCar.service;

import fr.diginamic.VroomVroomCar.dto.request.NotificationRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.NotificationResponseDto;
import fr.diginamic.VroomVroomCar.entity.Car;
import fr.diginamic.VroomVroomCar.entity.Notification;
import fr.diginamic.VroomVroomCar.entity.Trip;
import fr.diginamic.VroomVroomCar.entity.User;
import fr.diginamic.VroomVroomCar.exception.ResourceNotFoundException;
import fr.diginamic.VroomVroomCar.mapper.NotificationMapper;
import fr.diginamic.VroomVroomCar.repository.NotificationRepository;
import fr.diginamic.VroomVroomCar.repository.UserRepository;
import fr.diginamic.VroomVroomCar.util.NotificationUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

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

    private Trip createTrip() {
        Trip trip = new Trip();
        trip.setDateDebut(Date.valueOf(LocalDate.now()));
        trip.setDateFin(Date.valueOf(LocalDate.now().plusDays(1)));
        trip.setHeureDepart(LocalTime.now());
        trip.setLieuDepart("Gare Montparnasse");
        trip.setLieuArrivee("Gare Lille Flandres");
        trip.setVilleDepart("Paris");
        trip.setVilleArrivee("Lille");
        trip.setNbPlacesRestantes(4);
        trip.setOrganisateur(createUser());
        trip.setCar(createCar());
        return trip;
    }

    private User createUser() {
        User user = new User();
        user.setId(1);
        return user;
    }

    private Car createCar() {
        Car car = new Car();
        car.setId(1);
        car.setMarque("Toyoya");
        car.setModele("Yaris");
        return car;
    }

    @Test
    void testSendNotificationToOrganisateurOnSubscribe() {
        Trip trip = createTrip();
        User participant = createUser();

        doNothing().when(notificationUtil).createAndSaveNotification(anyString(), anyString(), any(User.class));

        notificationService.sendNotificationToOrganisateurOnSubscribe(trip, participant);

        verify(notificationUtil, times(1)).createAndSaveNotification(anyString(), anyString(), any(User.class));
    }

    @Test
    void testSendNotificationToOrganisateurOnUnsubscribe() {
        Trip trip = createTrip();
        User participant = createUser();

        doNothing().when(notificationUtil).createAndSaveNotification(anyString(), anyString(), any(User.class));

        notificationService.sendNotificationToOrganisateurOnUnsubscribe(trip, participant);

        verify(notificationUtil, times(1)).createAndSaveNotification(anyString(), anyString(), any(User.class));
    }

    @Test
    void testSendNotificationToParticipantsOnModification() {
        Trip trip = createTrip();
        User organisateur = createUser();

        doNothing().when(notificationUtil).sendNotificationToAllParticipants(any(Trip.class), anyString(), anyString());

        notificationService.sendNotificationToParticipantsOnModification(trip, organisateur);

        verify(notificationUtil, times(1)).sendNotificationToAllParticipants(any(Trip.class), anyString(), anyString());
    }

    @Test
    void testSendNotificationToParticipantsOnAnnulation() {
        Trip trip = createTrip();
        User organisateur = createUser();

        doNothing().when(notificationUtil).sendNotificationToAllParticipants(any(Trip.class), anyString(), anyString());

        notificationService.sendNotificationToParticipantsOnAnnulation(trip, organisateur);

        verify(notificationUtil, times(1)).sendNotificationToAllParticipants(any(Trip.class), anyString(), anyString());
    }
}