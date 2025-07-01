package fr.diginamic.VroomVroomCar.service;

import fr.diginamic.VroomVroomCar.dto.TripNotificationDetailsDto;
import fr.diginamic.VroomVroomCar.dto.request.TripRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.CarResponseDto;
import fr.diginamic.VroomVroomCar.dto.response.CompanyCarResponseDto;
import fr.diginamic.VroomVroomCar.dto.response.TripResponseDto;
import fr.diginamic.VroomVroomCar.dto.response.UserResponseDto;
import fr.diginamic.VroomVroomCar.entity.*;
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
import java.util.HashSet;
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
 */
@ExtendWith(MockitoExtension.class)
public class TripServiceTest {

    @Mock
    private TripRepository tripRepository;
    @Mock
    private TripMapper tripMapper;
    @Mock
    private CarRepository carRepository;
    @Mock
    private UserRepository userRepository;
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

    @InjectMocks
    private TripService tripService;

    // ============ MÉTHODES UTILITAIRES ============

    /**
     * Crée un utilisateur pour les tests.
     */
    private User createUser(Integer id, String nom, String email) {
        User user = new User();
        user.setId(id);
        user.setNom(nom);
        user.setMail(email);
        user.setPrenom("Prenom");
        return user;
    }

    /**
     * Crée une voiture pour les tests.
     */
    private Car createCar(Integer id, int nbPlaces) {
        Car car = new Car();
        car.setId(id);
        car.setNbDePlaces(nbPlaces);
        return car;
    }

    /**
     * Crée un TripRequestDto pour les tests.
     */
    private TripRequestDto createTripRequestDto() {
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
        requestDto.setCarId(1);
        return requestDto;
    }

    /**
     * Crée un trajet pour les tests.
     */
    private Trip createTrip(Integer id, Date dateDebut, Date dateFin, LocalTime heureDepart,
                            String lieuDepart, String lieuArrivee, String villeDepart,
                            String villeArrivee, User organisateur, Car car) {
        Trip trip = Trip.builder()
                .id(id)
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

        trip.setSubscribes(new HashSet<>());
        return trip;
    }

    private Subscribe createSubscribe(User user, Trip trip) {
        SubscribeKey key = new SubscribeKey(user.getId(), trip.getId());
        Subscribe subscribe = new Subscribe();
        subscribe.setId(key);
        subscribe.setUser(user);
        subscribe.setTrip(trip);
        return subscribe;
    }

    // ============ TESTS ============

    /**
     * Teste la création d'un trajet via la méthode {@link TripService#createTrip}.
     */
    @Test
    void testCreateTrip() throws FunctionnalException {
        // Arrange
        TripRequestDto requestDto = createTripRequestDto();
        User user = createUser(1, "Jean Dupont", "jean@test.com");
        Car car = createCar(1, 5);

        Trip trip = createTrip(
                null, // ID null car pas encore sauvé
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
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(carRepository.findById(1)).thenReturn(Optional.of(car));
        when(tripMapper.toEntity(any(), any(), any())).thenReturn(trip);
        when(openRouteService.getTravelDurationInSeconds(anyString(), anyString())).thenReturn(7200.0); // 2h
        when(validationUtil.estVehiculeDeService(eq(1), any())).thenReturn(true);
        when(reservationRepository.existsByCompanyCar_IdAndUser_IdAndDateDebutAndDateFin(
                eq(1), eq(1), eq(requestDto.getDateDebut()), eq(requestDto.getDateFin())
        )).thenReturn(true);
        when(subscribeRepository.countByTrip_Id(any())).thenReturn(2);

        ArgumentCaptor<Trip> tripCaptor = ArgumentCaptor.forClass(Trip.class);
        when(tripRepository.save(tripCaptor.capture())).thenAnswer(i -> {
            Trip savedTrip = i.getArgument(0);
            savedTrip.setId(42); // Simule l'ID généré
            return savedTrip;
        });
        when(tripMapper.toResponse(any())).thenReturn(tripResponseDto);

        // Act
        TripResponseDto result = tripService.createTrip(requestDto);

        // Assert
        assertNotNull(result);
        Trip savedTrip = tripCaptor.getValue();
        assertNotNull(savedTrip.getHeureArrivee());
        assertTrue(savedTrip.getNbPlacesRestantes() >= 0);

        verify(userRepository).findById(1);
        verify(carRepository).findById(1);
        verify(tripRepository).save(any(Trip.class));
        verify(tripMapper).toResponse(any(Trip.class));
        verify(validationUtil).validateEndDateBeforeStartDate(any(), any());
    }

    /**
     * Teste la récupération de tous les trajets via TripService.
     */
    @Test
    void testGetAllTrips() {
        // Arrange
        User user = createUser(1, "Jean", "jean@test.com");
        Car car = createCar(1, 5);
        Trip trip = createTrip(1, new java.sql.Date(System.currentTimeMillis()), new java.sql.Date(System.currentTimeMillis()), LocalTime.now(),
                "Toulouse", "Paris", "Toulouse", "Paris", user, car);
        TripResponseDto tripResponseDto = new TripResponseDto();

        when(tripRepository.findAll()).thenReturn(Collections.singletonList(trip));
        when(tripMapper.toResponse(any(Trip.class))).thenReturn(tripResponseDto);

        // Act
        List<TripResponseDto> result = tripService.getAllTrips();

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(tripRepository, times(1)).findAll();
    }

    /**
     * Teste la récupération d'un trajet par son identifiant via TripService.
     */
    @Test
    void testGetTripById() throws FunctionnalException {
        // Arrange
        User user = createUser(1, "Jean", "jean@test.com");
        Car car = createCar(1, 5);
        Trip trip = createTrip(1, new java.sql.Date(System.currentTimeMillis()), new java.sql.Date(System.currentTimeMillis()), LocalTime.now(),
                "Toulouse", "Paris", "Toulouse", "Paris", user, car);
        TripResponseDto tripResponseDto = new TripResponseDto();

        when(tripRepository.findById(1)).thenReturn(Optional.of(trip));
        when(tripMapper.toResponse(any(Trip.class))).thenReturn(tripResponseDto);

        // Act
        TripResponseDto result = tripService.getTripById(1);

        // Assert
        assertNotNull(result);
        verify(tripRepository, times(1)).findById(1);
    }

    @Test
    void testSearchTrips() throws FunctionnalException {
        // Arrange
        String villeDepart = "Toulouse";
        String villeArrivee = "Paris";
        Date dateDebut = Date.valueOf(LocalDate.now());
        LocalTime heureDepart = LocalTime.of(8, 30);
        VehiculeType vehiculeType = VehiculeType.VOITURE_SERVICE;

        User user = createUser(1, "Jean", "jean@test.com");
        Car car = createCar(1, 5);
        Trip trip1 = createTrip(1, dateDebut, dateDebut, heureDepart,
                "Centre", "Gare", villeDepart, villeArrivee, user, car);

        List<Trip> expectedTrips = List.of(trip1);

        when(tripRepository.findTripsWithFilters(
                villeDepart, villeArrivee, dateDebut, heureDepart, vehiculeType.name())
        ).thenReturn(expectedTrips);

        // Act
        List<Trip> result = tripService.searchTrips(villeDepart, villeArrivee, dateDebut, heureDepart, vehiculeType);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(villeDepart, result.get(0).getVilleDepart());
        assertEquals(villeArrivee, result.get(0).getVilleArrivee());

        verify(tripRepository).findTripsWithFilters(villeDepart, villeArrivee, dateDebut, heureDepart, vehiculeType.name());
    }

    @Test
    void testGetUpcomingUserTrips() {
        // Arrange
        Integer userId = 1;
        User user = createUser(userId, "Jean", "jean@test.com");
        Car car = createCar(1, 5);
        List<Trip> mockTrips = List.of(
                createTrip(1, new java.sql.Date(System.currentTimeMillis()), new java.sql.Date(System.currentTimeMillis()), LocalTime.now(), "A", "B", "Paris", "Lyon", user, car),
                createTrip(2, new java.sql.Date(System.currentTimeMillis()), new java.sql.Date(System.currentTimeMillis()), LocalTime.now(), "C", "D", "Lyon", "Nice", user, car)
        );

        when(tripRepository.findUpcomingUserTrips(userId)).thenReturn(mockTrips);

        // Act
        List<TripResponseDto> result = tripService.getUpcomingUserTrips(userId);

        // Assert
        assertEquals(2, result.size());
        verify(tripRepository).findUpcomingUserTrips(userId);
    }

    @Test
    void testGetPastUserTrips() {
        // Arrange
        Integer userId = 2;
        User user = createUser(userId, "Marie", "marie@test.com");
        Car car = createCar(1, 5);
        List<Trip> mockTrips = List.of(
                createTrip(1, new java.sql.Date(System.currentTimeMillis()), new java.sql.Date(System.currentTimeMillis()), LocalTime.now(), "A", "B", "Paris", "Lyon", user, car)
        );

        when(tripRepository.findPastUserTrips(userId)).thenReturn(mockTrips);

        // Act
        List<TripResponseDto> result = tripService.getPastUserTrips(userId);

        // Assert
        assertEquals(1, result.size());
        verify(tripRepository).findPastUserTrips(userId);
    }

    /**
     * Teste la mise à jour d'un trajet via {@link TripService#updateTrip}.
     */
    @Test
    void testUpdateTrip() throws FunctionnalException {
        // Arrange
        Integer tripId = 42;
        TripRequestDto requestDto = createTripRequestDto();
        requestDto.setId(tripId);
        requestDto.setHeureDepart(LocalTime.of(8, 0));
        requestDto.setLieuArrivee("Paris");
        User organisateur = createUser(1, "Jean", "jean@test.com");
        Car car = createCar(1, 5);

        Trip existingTrip = createTrip(
                tripId,
                requestDto.getDateDebut(),
                requestDto.getDateFin(),
                LocalTime.of(7, 0),
                "Toulouse",
                "Lyon",
                "Toulouse",
                "Lyon",
                organisateur,
                car
        );
        existingTrip.setNbPlacesRestantes(4);
        TripResponseDto updatedResponseDto = new TripResponseDto();

        // Mocks
        when(tripRepository.findById(tripId)).thenReturn(Optional.of(existingTrip));
        doNothing().when(validationUtil).validateEndDateBeforeStartDate(any(), any());
        when(userRepository.findById(1)).thenReturn(Optional.of(organisateur));
        when(carRepository.findById(1)).thenReturn(Optional.of(car));
        doAnswer(invocation -> {
            Trip tripToUpdate = invocation.getArgument(0);
            TripRequestDto dto = invocation.getArgument(1);
            tripToUpdate.setHeureDepart(dto.getHeureDepart());
            tripToUpdate.setLieuArrivee(dto.getLieuArrivee());
            return null;
        }).when(tripMapper).updateEntity(any(), any(), any(), any());
        when(openRouteService.getTravelDurationInSeconds(anyString(), anyString())).thenReturn(7200.0);
        when(validationUtil.estVehiculeDeService(eq(1), eq(carRepository))).thenReturn(false); // Utilisez les bons arguments ici
        when(subscribeRepository.countByTrip_Id(any())).thenReturn(1);
        when(tripRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(tripMapper.toResponse(any())).thenReturn(updatedResponseDto);

        doNothing().when(notificationService).sendNotificationToParticipantsOnModification(any(Trip.class));

        // Act
        TripResponseDto result = tripService.updateTrip(tripId, requestDto);

        // Assert
        assertNotNull(result);
        verify(tripRepository).findById(tripId);
        verify(validationUtil).validateEndDateBeforeStartDate(any(), any());
        verify(tripMapper).updateEntity(eq(existingTrip), eq(requestDto), eq(organisateur), eq(car));
        verify(tripRepository).save(existingTrip);

        // --- CORRECTION ---
        // La méthode a changé, elle ne prend plus l'organisateur en second paramètre.
        verify(notificationService).sendNotificationToParticipantsOnModification(existingTrip);

        verify(tripMapper).toResponse(existingTrip);
    }

    /**
     * Teste la suppression d'un trajet via {@link TripService#deleteTrip}.
     */
    @Test
    void testDeleteTrip() throws FunctionnalException {
        // Arrange
        User user = createUser(1, "Jean", "jean@test.com");
        User participant = createUser(2, "Paul", "paul@test.com");
        Car car = createCar(1, 5);
        Trip trip = createTrip(1, new java.sql.Date(System.currentTimeMillis()), new java.sql.Date(System.currentTimeMillis()), LocalTime.now(),
                "A", "B", "Paris", "Lyon", user, car);

        trip.getSubscribes().add(createSubscribe(participant, trip));

        when(tripRepository.findById(1)).thenReturn(Optional.of(trip));
        doNothing().when(notificationService).sendNotificationToParticipantsOnAnnulation(any(), any());
        // La méthode du service appelle delete(objet), pas deleteById(id)
        doNothing().when(tripRepository).delete(any(Trip.class));


        // Act
        tripService.deleteTrip(1);

        // Assert
        verify(tripRepository, times(1)).findById(1);
        ArgumentCaptor<List<Subscribe>> listCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<TripNotificationDetailsDto> dtoCaptor = ArgumentCaptor.forClass(TripNotificationDetailsDto.class);

        // On vérifie que la bonne méthode de notification a été appelée
        verify(notificationService).sendNotificationToParticipantsOnAnnulation(listCaptor.capture(), dtoCaptor.capture());

        // On vérifie le contenu des arguments capturés
        assertEquals(1, listCaptor.getValue().size()); // Il y avait bien 1 participant
        assertEquals("Paris", dtoCaptor.getValue().getVilleDepart()); // Le DTO contient les bonnes infos

        // On vérifie que la méthode de suppression a été appelée avec le bon objet
        verify(tripRepository, times(1)).delete(trip);
    }

    /**
     * Teste le calcul de l'heure d'arrivée.
     */
    @Test
    void testCalculateArrivalTime() {
        // Arrange
        LocalTime heureDepart = LocalTime.of(9, 0);
        String lieuDepart = "Centre-ville";
        String lieuArrivee = "Aéroport";
        String villeDepart = "Toulouse";
        String villeArrivee = "Paris";

        when(openRouteService.getTravelDurationInSeconds(
                "Centre-ville, Toulouse", "Aéroport, Paris"
        )).thenReturn(7200.0); // 2 heures

        // Act
        LocalTime result = tripService.calculateArrivalTime(
                heureDepart, lieuDepart, lieuArrivee, villeDepart, villeArrivee
        );

        // Assert
        assertEquals(LocalTime.of(11, 0), result);
        verify(openRouteService).getTravelDurationInSeconds("Centre-ville, Toulouse", "Aéroport, Paris");
    }

    /**
     * Teste le calcul du nombre de places restantes.
     */
    @Test
    void testCalculatePlaceRest() throws FunctionnalException {
        // Arrange
        TripRequestDto requestDto = createTripRequestDto();
        Car car = createCar(1, 5);

        when(validationUtil.estVehiculeDeService(eq(1), any())).thenReturn(false);
        when(subscribeRepository.countByTrip_Id(any())).thenReturn(2);

        // Act
        int result = tripService.calculatePlaceRest(requestDto, car);

        // Assert
        assertEquals(3, result); // 5 places - 2 souscriptions = 3 places restantes
        verify(subscribeRepository).countByTrip_Id(requestDto.getId());
    }

    /**
     * Teste le calcul du nombre de places restantes avec véhicule de service.
     */
    @Test
    void testCalculatePlaceRestWithServiceVehicle() throws FunctionnalException {
        // Arrange
        TripRequestDto requestDto = createTripRequestDto();
        Car car = createCar(1, 5);

        when(validationUtil.estVehiculeDeService(eq(1), any())).thenReturn(true);
        when(reservationRepository.existsByCompanyCar_IdAndUser_IdAndDateDebutAndDateFin(
                eq(1), eq(1), eq(requestDto.getDateDebut()), eq(requestDto.getDateFin())
        )).thenReturn(true);
        when(subscribeRepository.countByTrip_Id(any())).thenReturn(1);

        // Act
        int result = tripService.calculatePlaceRest(requestDto, car);

        // Assert
        assertEquals(3, result); // 5 places - 1 organisateur - 1 souscription = 3 places restantes
        verify(validationUtil).estVehiculeDeService(eq(1), any());
        verify(reservationRepository).existsByCompanyCar_IdAndUser_IdAndDateDebutAndDateFin(
                eq(1), eq(1), eq(requestDto.getDateDebut()), eq(requestDto.getDateFin())
        );
    }
}
