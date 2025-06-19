package fr.diginamic.VroomVroomCar.service;

import fr.diginamic.VroomVroomCar.dto.request.TripRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.CarResponseDto;
import fr.diginamic.VroomVroomCar.dto.response.CompanyCarResponseDto;
import fr.diginamic.VroomVroomCar.dto.response.TripResponseDto;
import fr.diginamic.VroomVroomCar.dto.response.UserResponseDto;
import fr.diginamic.VroomVroomCar.entity.CompanyCar;
import fr.diginamic.VroomVroomCar.entity.Trip;
import fr.diginamic.VroomVroomCar.entity.User;
import fr.diginamic.VroomVroomCar.entity.VehiculeType;
import fr.diginamic.VroomVroomCar.exception.FunctionnalException;
import fr.diginamic.VroomVroomCar.mapper.TripMapper;
import fr.diginamic.VroomVroomCar.repository.*;
import fr.diginamic.VroomVroomCar.util.ValidationUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Classe de test pour le service TripService.
 *
 * Cette classe utilise Mockito pour simuler les dépendances du service et
 * vérifier le comportement des méthodes liées à la gestion des trajets.
 *
 */
@ExtendWith(MockitoExtension.class)
public class TripServiceTest {

    @Mock
    private TripRepository tripRepository;
    @Mock
    private TripMapper tripMapper;
    @InjectMocks
    private TripService tripService;

    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private SubscribeRepository subscribeRepository;
    @Mock
    private NotificationService notificationService;

    @Mock
    private OpenRouteService openRouteService;

    @Mock
    private ValidationUtil validationUtil;


    /**
     * Crée et sauvegarde un trajet pour un utilisateur spécifique.
     *
     * @param dateDebut     La date de début du trajet.
     * @param dateFin       La date de fin du trajet.
     * @param heureDepart   L'heure de départ.
     * @param lieuDepart    Le lieu de départ.
     * @param lieuArrivee   Le lieu d'arrivée.
     * @param villeDepart   La ville de départ.
     * @param villeArrivee  La ville d'arrivée.
     * @param organisateur  L'utilisateur organisateur du trajet.
     * @param car           La voiture utilisée.
     */
    private Trip createAndSaveTrip(Date dateDebut, Date dateFin, LocalTime heureDepart,
                                          String lieuDepart, String lieuArrivee,
                                          String villeDepart, String villeArrivee,
                                          User organisateur, CompanyCar car) {
        return Trip.builder()
                .dateDebut(dateDebut)
                .dateFin(dateFin)
                .heureDepart(heureDepart)
                .lieuDepart(lieuDepart)
                .lieuArrivee(lieuArrivee)
                .villeDepart(villeDepart)
                .villeArrivee(villeArrivee)
                .organisateur(organisateur)
                .car(car)
                .build();
    }

    /**
     * Teste la création d'un trajet via la méthode {@link TripService#createTrip}.
     *
     * Ce test vérifie que le trajet est bien créé, validé, enregistré dans le repository
     * et que le mapping vers la réponse fonctionne correctement.
     *
     */
    @Test
    void testCreateTrip() throws FunctionnalException {
        // Arrange
        LocalDate today = LocalDate.now();

        TripRequestDto requestDto = new TripRequestDto();
        requestDto.setDateDebut(Date.valueOf(today));
        requestDto.setDateFin(Date.valueOf(today.plusDays(1)));
        requestDto.setHeureDepart(LocalTime.of(9, 0));
        requestDto.setLieuDepart("Toulouse");
        requestDto.setLieuArrivee("Bordeaux");
        requestDto.setVilleDepart("Toulouse");
        requestDto.setVilleArrivee("Bordeaux");
        requestDto.setNbPlacesRestantes(3);
        requestDto.setOrganisateurId(1);

        CarResponseDto carResponseDto = new CarResponseDto();
        carResponseDto.setNbDePlaces(5);
        carResponseDto.setId(1);

        CompanyCarResponseDto companyCarResponseDto = new CompanyCarResponseDto();
        companyCarResponseDto.setNbDePlaces(5);
        companyCarResponseDto.setId(1);

        UserResponseDto userResponseDto = new UserResponseDto();
        User user = new User();
        CompanyCar car = new CompanyCar();

        Trip trip = createAndSaveTrip(
                requestDto.getDateDebut(),
                requestDto.getDateFin(),
                requestDto.getHeureDepart(),
                requestDto.getLieuDepart(),
                requestDto.getLieuArrivee(),
                requestDto.getVilleDepart(),
                requestDto.getVilleArrivee(),
                user,
                car
        );

        TripResponseDto tripResponseDto = new TripResponseDto();

        // Mocks
        doNothing().when(validationUtil).validateEndDateBeforeStartDate(any(), any());

        when(tripMapper.toEntity(any(), any(), any())).thenReturn(trip);
        when(openRouteService.getTravelDurationInSeconds(anyString(), anyString())).thenReturn(7200.0); // 2h
        when(validationUtil.estVehiculeDeService(eq(carResponseDto.getId()), any())).thenReturn(true);
        when(reservationRepository.existsByCompanyCar_IdAndUser_IdAndDateDebutAndDateFin(
                eq(companyCarResponseDto.getId()),
                eq(requestDto.getOrganisateurId()),
                eq(requestDto.getDateDebut()),
                eq(requestDto.getDateFin())
        )).thenReturn(true);
        when(subscribeRepository.countByTrip_Id(any())).thenReturn(2);

        ArgumentCaptor<Trip> tripCaptor = ArgumentCaptor.forClass(Trip.class);
        when(tripRepository.save(tripCaptor.capture())).thenAnswer(i -> i.getArgument(0));
        when(tripMapper.toResponse(any())).thenReturn(tripResponseDto);

        // Act
        TripResponseDto result = tripService.createTrip(requestDto, userResponseDto, carResponseDto);

        // Assert
        assertNotNull(result);
        Trip savedTrip = tripCaptor.getValue();
        assertEquals(2, savedTrip.getNbPlacesRestantes());

        verify(tripRepository).save(any(Trip.class));
        verify(tripMapper).toResponse(any(Trip.class));
        verify(validationUtil).validateEndDateBeforeStartDate(any(), any());
    }

    /**
     * Teste la récupération de tous les trajets via TripService.
     *
     * Vérifie que la méthode retourne une liste non vide et que la méthode findAll du repository
     * est bien appelée.
     *
     */
    @Test
    void testGetAllTrips(){
        Trip trip = new Trip();
        TripResponseDto tripResponseDto = new TripResponseDto();

        when(tripRepository.findAll()).thenReturn(Collections.singletonList(trip));
        when(tripMapper.toResponse(any(Trip.class))).thenReturn(tripResponseDto);

        List<TripResponseDto> result = tripService.getAllTrips();

        assertFalse(result.isEmpty());
        verify(tripRepository, times(1)).findAll();
    }
    /**
     * Teste la récupération d'un trajet par son identifiant via TripService.
     *
     * Vérifie que la méthode retourne un objet non nul et que la recherche dans le repository est effectuée.
     *
     */
    @Test
    void testGetTripById() throws FunctionnalException {
        Trip trip = new Trip();
        TripResponseDto tripResponseDto = new TripResponseDto();

        when(tripRepository.findById(anyInt())).thenReturn(Optional.of(trip));
        when(tripMapper.toResponse(any(Trip.class))).thenReturn(tripResponseDto);

        TripResponseDto result = tripService.getTripById(1);

        assertNotNull(result);
        verify(tripRepository, times(1)).findById(anyInt());

    }

    @Test
    void testSearchTrips() throws FunctionnalException {
        String villeDepart = "Toulouse";
        String villeArrivee = "Paris";
        Date dateDebut = Date.valueOf(LocalDate.now());
        LocalTime heureDepart = LocalTime.of(8, 30);
        VehiculeType vehiculeType = VehiculeType.VOITURE_SERVICE;

        Trip trip1 = new Trip();
        trip1.setVilleDepart(villeDepart);
        trip1.setVilleArrivee(villeArrivee);
        trip1.setDateDebut(dateDebut);
        trip1.setHeureDepart(heureDepart);

        List<Trip> expectedTrips = List.of(trip1);

        when(tripRepository.findTripsWithFilters(
                villeDepart, villeArrivee, dateDebut, heureDepart, vehiculeType.name())
        ).thenReturn(expectedTrips);

        List<Trip> result = tripService.searchTrips(villeDepart, villeArrivee, dateDebut, heureDepart, vehiculeType);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(villeDepart, result.get(0).getVilleDepart());
        assertEquals(villeArrivee, result.get(0).getVilleArrivee());

        verify(tripRepository).findTripsWithFilters(villeDepart, villeArrivee, dateDebut, heureDepart, vehiculeType.name());
    }

    @Test
    void testGetUpcomingUserTrips() {
        Integer userId = 1;
        List<Trip> mockTrips = List.of(new Trip(), new Trip());
        when(tripRepository.findUpcomingUserTrips(userId)).thenReturn(mockTrips);

        List<Trip> result = tripService.getUpcomingUserTrips(userId);

        assertEquals(2, result.size());
        verify(tripRepository).findUpcomingUserTrips(userId);
    }

    @Test
    void testGetPastUserTrips() {
        Integer userId = 2;
        List<Trip> mockTrips = List.of(new Trip());
        when(tripRepository.findPastUserTrips(userId)).thenReturn(mockTrips);

        List<Trip> result = tripService.getPastUserTrips(userId);

        assertEquals(1, result.size());
        verify(tripRepository).findPastUserTrips(userId);
    }

    /**
     * Teste la mise à jour d'un trajet via {@link TripService#updateTrip}.
     *
     * Ce test vérifie que les modifications sont bien appliquées, que la validation est faite,
     * que la sauvegarde est effectuée et que la notification est envoyée aux participants.
     *
     */
    @Test
    void testUpdateTrip() throws FunctionnalException {
        // Arrange
        Integer tripId = 42;
        LocalDate today = LocalDate.now();

        TripRequestDto requestDto = new TripRequestDto();
        requestDto.setDateDebut(Date.valueOf(today));
        requestDto.setDateFin(Date.valueOf(today.plusDays(1)));
        requestDto.setHeureDepart(LocalTime.of(8, 0));
        requestDto.setLieuDepart("Toulouse");
        requestDto.setLieuArrivee("Paris");
        requestDto.setVilleDepart("Toulouse");
        requestDto.setVilleArrivee("Paris");
        requestDto.setNbPlacesRestantes(3);
        requestDto.setOrganisateurId(1);
        requestDto.setId(tripId);

        CarResponseDto carResponseDto = new CarResponseDto();
        carResponseDto.setId(10);
        carResponseDto.setNbDePlaces(5);

        UserResponseDto userResponseDto = new UserResponseDto();

        User organisateur = new User();
        organisateur.setId(1);

        CompanyCar car = new CompanyCar();
        car.setId(10);
        car.setNbDePlaces(5);

        Trip existingTrip = createAndSaveTrip(
                Date.valueOf(today),
                Date.valueOf(today.plusDays(1)),
                LocalTime.of(7, 0),
                "Toulouse",
                "Lyon",
                "Toulouse",
                "Lyon",
                organisateur,
                car
        );
        existingTrip.setId(tripId);
        existingTrip.setNbPlacesRestantes(4);

        TripResponseDto updatedResponseDto = new TripResponseDto();

        // Mocks
        when(tripRepository.findById(tripId)).thenReturn(Optional.of(existingTrip));
        doNothing().when(validationUtil).validateEndDateBeforeStartDate(any(), any());

        doAnswer(invocation -> {
            // Simule l'updateEntity
            Trip tripToUpdate = invocation.getArgument(0);
            tripToUpdate.setHeureDepart(requestDto.getHeureDepart());
            tripToUpdate.setLieuArrivee(requestDto.getLieuArrivee());
            return null;
        }).when(tripMapper).updateEntity(any(), any(), any(), any());

        when(openRouteService.getTravelDurationInSeconds(anyString(), anyString())).thenReturn(7200.0);
        when(validationUtil.estVehiculeDeService(eq(carResponseDto.getId()), any())).thenReturn(false); // pas de résa
        when(subscribeRepository.countByTrip_Id(any())).thenReturn(1);
        when(tripRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(tripMapper.toResponse(any())).thenReturn(updatedResponseDto);

        // Act
        TripResponseDto result = tripService.updateTrip(tripId, requestDto, userResponseDto, carResponseDto);

        // Assert
        assertNotNull(result);
        verify(tripRepository).findById(tripId);
        verify(validationUtil).validateEndDateBeforeStartDate(any(), any());
        verify(tripMapper).updateEntity(eq(existingTrip), eq(requestDto), eq(userResponseDto), eq(carResponseDto));
        verify(tripRepository).save(existingTrip);
        verify(notificationService).sendNotificationToParticipantsOnModification(existingTrip, organisateur);
        verify(tripMapper).toResponse(existingTrip);
    }

    /**
     * Teste la suppression d'un trajet via {@link TripService#deleteTrip}.
     *
     * Vérifie que le trajet est bien supprimé dans le repository.
     *
     */
    @Test
    void testDeleteTrip() throws FunctionnalException {
        Trip trip = new Trip();
        trip.setId(1);
        trip.setOrganisateur(new User());

        when(tripRepository.findById(1)).thenReturn(Optional.of(trip));
        doNothing().when(tripRepository).deleteById(1);

        tripService.deleteTrip(1);

        verify(tripRepository, times(1)).deleteById(1);
    }

}
