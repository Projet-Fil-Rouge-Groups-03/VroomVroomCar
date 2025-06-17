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

    @Test
    void testGetUserNotifications() {
        Notification notification = new Notification();
        NotificationResponseDto responseDto = new NotificationResponseDto();

        when(notificationRepository.findByUserIdOrderByDateDesc(anyInt(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(notification)));
        when(notificationMapper.toResponseDto(any(Notification.class))).thenReturn(responseDto);

        List<NotificationResponseDto> result = notificationService.getUserNotifications(1, 10);

        assertFalse(result.isEmpty());
        verify(notificationRepository, times(1)).findByUserIdOrderByDateDesc(anyInt(), any(Pageable.class));
    }

    @Test
    void testCreateNotification() throws ResourceNotFoundException {
        NotificationRequestDto requestDto = new NotificationRequestDto();
        requestDto.setUserId(1);

        User user = new User();
        Notification notification = new Notification();
        NotificationResponseDto responseDto = new NotificationResponseDto();

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(notificationMapper.toEntity(any(NotificationRequestDto.class), any(User.class))).thenReturn(notification);
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);
        when(notificationMapper.toResponseDto(any(Notification.class))).thenReturn(responseDto);

        NotificationResponseDto result = notificationService.createNotification(requestDto);

        assertNotNull(result);
        verify(userRepository, times(1)).findById(anyInt());
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void testDeleteNotification() throws ResourceNotFoundException {
        doNothing().when(notificationRepository).deleteById(anyInt());
        when(notificationRepository.existsById(anyInt())).thenReturn(true);

        notificationService.deleteNotification(1);

        verify(notificationRepository, times(1)).deleteById(anyInt());
    }

    @Test
    void testSendNotificationToOrganisateurOnSubscribe() {
        Trip trip = new Trip();
        trip.setDateDebut(Date.valueOf(LocalDate.now())); // Initialisation de la date de début
        trip.setDateFin(Date.valueOf(LocalDate.now().plusDays(1))); // Initialisation de la date de fin
        trip.setHeureDepart(LocalTime.now()); // Initialisation de l'heure de départ
        trip.setLieuDepart("Lieu de départ");
        trip.setLieuArrivee("Lieu d'arrivée");
        trip.setVilleDepart("Ville de départ");
        trip.setVilleArrivee("Ville d'arrivée");
        trip.setNbPlacesRestantes(4);
        trip.setOrganisateur(1);
        trip.setCar(1);

        User participant = new User();
        participant.setId(2);

        NotificationRequestDto requestDto = new NotificationRequestDto();
        requestDto.setUserId(1);

        doNothing().when(notificationUtil).createAndSaveNotification(anyString(), anyString(), any(User.class));

        notificationService.sendNotificationToOrganisateurOnSubscribe(trip, participant);

        verify(notificationUtil, times(1)).createAndSaveNotification(anyString(), anyString(), any(User.class));
    }

    @Test
    void testSendNotificationToOrganisateurOnUnsubscribe() {
        Trip trip = new Trip();
        User participant = new User();

        doNothing().when(notificationUtil).createAndSaveNotification(anyString(), anyString(), any(User.class));

        notificationService.sendNotificationToOrganisateurOnUnsubscribe(trip, participant);

        verify(notificationUtil, times(1)).createAndSaveNotification(anyString(), anyString(), any(User.class));
    }

    @Test
    void testSendNotificationToParticipantsOnModification() {
        Trip trip = new Trip();
        User organisateur = new User();

        doNothing().when(notificationUtil).sendNotificationToAllParticipants(any(Trip.class), anyString(), anyString());

        notificationService.sendNotificationToParticipantsOnModification(trip, organisateur);

        verify(notificationUtil, times(1)).sendNotificationToAllParticipants(any(Trip.class), anyString(), anyString());
    }

    @Test
    void testSendNotificationToParticipantsOnAnnulation() {
        Trip trip = new Trip();
        User organisateur = new User();

        doNothing().when(notificationUtil).sendNotificationToAllParticipants(any(Trip.class), anyString(), anyString());

        notificationService.sendNotificationToParticipantsOnAnnulation(trip, organisateur);

        verify(notificationUtil, times(1)).sendNotificationToAllParticipants(any(Trip.class), anyString(), anyString());
    }

    @Test
    void testSendNotificationToUsersOnCarStatusUpdate() {
        Car car = new Car();
        car.setMarque("MarqueTest");
        car.setModele("ModeleTest");
        User user = new User();

        doNothing().when(notificationUtil).createAndSaveNotification(anyString(), anyString(), any(User.class));

        notificationService.sendNotificationToUsersOnCarStatusUpdate(car, "Nouveau Statut", user);

        verify(notificationUtil, times(1)).createAndSaveNotification(anyString(), anyString(), any(User.class));
    }

}