package fr.diginamic.VroomVroomCar.service;

import fr.diginamic.VroomVroomCar.dto.TripNotificationDetailsDto;
import fr.diginamic.VroomVroomCar.dto.request.TripRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.TripResponseDto;
import fr.diginamic.VroomVroomCar.entity.*;
import fr.diginamic.VroomVroomCar.exception.FunctionnalException;
import fr.diginamic.VroomVroomCar.mapper.TripMapper;
import fr.diginamic.VroomVroomCar.repository.*;
import fr.diginamic.VroomVroomCar.util.ValidationUtil;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

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
     * Teste la création d'un trajet via la méthode createTrip.
     */
    @Test
    void testCreateTrip() throws FunctionnalException {
        TripRequestDto requestDto = createTripRequestDto();
        User user = createUser(1, "Jean Dupont", "jean@test.com");
        Car car = createCar(1, 5);

        Trip trip = createTrip(
                null,
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

        doNothing().when(validationUtil).validateEndDateBeforeStartDate(any(), any());
        when(validationUtil.estVehiculeDeService(anyInt(), any())).thenReturn(true);

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(carRepository.findById(anyInt())).thenReturn(Optional.of(car));
        when(tripMapper.toEntity(any(), any(), any())).thenReturn(trip);
        when(openRouteService.getTravelDurationInSeconds(anyString(), anyString())).thenReturn(7200.0); // 2h
        when(reservationRepository.existsByCompanyCar_IdAndUser_IdAndDateDebutAndDateFin(
                anyInt(), anyInt(), any(), any()
        )).thenReturn(true);
        when(subscribeRepository.countByTrip_Id(any())).thenReturn(2);
        when(tripRepository.save(any(Trip.class))).thenAnswer(invocation -> {
            Trip savedTrip = invocation.getArgument(0);
            savedTrip.setId(42); // Simule l'ID généré
            return savedTrip;
        });
        when(tripMapper.toResponse(any(Trip.class))).thenReturn(tripResponseDto);

        TripResponseDto result = tripService.createTrip(requestDto);

        assertNotNull(result);

        ArgumentCaptor<Trip> tripCaptor = ArgumentCaptor.forClass(Trip.class);
        verify(tripRepository).save(tripCaptor.capture());

        Trip savedTrip = tripCaptor.getValue();
        assertNotNull(savedTrip.getHeureArrivee());
        assertTrue(savedTrip.getNbPlacesRestantes() >= 0);

        verify(validationUtil).validateEndDateBeforeStartDate(any(), any());
        verify(userRepository).findById(anyInt());
        verify(carRepository).findById(anyInt());
        verify(tripMapper).toResponse(any(Trip.class));
    }


    /**
     * Teste la récupération de tous les trajets via TripService.
     */
    @Test
    void testGetAllTrips() {
        User user = createUser(1, "Jean", "jean@test.com");
        Car car = createCar(1, 5);
        Trip trip = createTrip(1, new java.sql.Date(System.currentTimeMillis()), new java.sql.Date(System.currentTimeMillis()),
                LocalTime.now(), "Toulouse", "Paris", "Toulouse", "Paris", user, car);
        TripResponseDto tripResponseDto = new TripResponseDto();

        Page<Trip> page = new PageImpl<>(Collections.singletonList(trip));

        when(tripRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(tripMapper.toResponse(any(Trip.class))).thenReturn(tripResponseDto);

        Page<TripResponseDto> result = tripService.getAllTrips(0, 10);

        assertFalse(result.isEmpty());
        assertEquals(1, result.getContent().size());
        verify(tripRepository, times(1)).findAll(any(Pageable.class));
    }


    /**
     * Teste la récupération d'un trajet par son identifiant via TripService.
     */
    @Test
    void testGetTripById() throws FunctionnalException {
        User user = createUser(1, "Jean", "jean@test.com");
        Car car = createCar(1, 5);
        Trip trip = createTrip(1, new java.sql.Date(System.currentTimeMillis()), new java.sql.Date(System.currentTimeMillis()), LocalTime.now(),
                "Toulouse", "Paris", "Toulouse", "Paris", user, car);
        TripResponseDto tripResponseDto = new TripResponseDto();

        when(tripRepository.findById(1)).thenReturn(Optional.of(trip));
        when(tripMapper.toResponse(any(Trip.class))).thenReturn(tripResponseDto);

        TripResponseDto result = tripService.getTripById(1);

        assertNotNull(result);
        verify(tripRepository, times(1)).findById(1);
    }

    @Test
    void testSearchTrips() throws FunctionnalException {
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

        // Création du TripResponseDto attendu
        TripResponseDto tripResponseDto = new TripResponseDto();
        tripResponseDto.setVilleDepart(villeDepart);
        tripResponseDto.setVilleArrivee(villeArrivee);

        // Mock du repository
        when(tripRepository.findTripsWithFilters(
                villeDepart, villeArrivee, dateDebut, heureDepart, vehiculeType.name())
        ).thenReturn(expectedTrips);
        // Mock du mapper
        when(tripMapper.toResponse(trip1)).thenReturn(tripResponseDto);

        List<TripResponseDto> result = tripService.searchTrips(villeDepart, villeArrivee, dateDebut, heureDepart, vehiculeType);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(villeDepart, result.get(0).getVilleDepart());
        assertEquals(villeArrivee, result.get(0).getVilleArrivee());

        verify(tripRepository).findTripsWithFilters(villeDepart, villeArrivee, dateDebut, heureDepart, vehiculeType.name());
        verify(tripMapper).toResponse(trip1);
    }

    @Test
    void testGetUpcomingUserTrips() {
        Integer userId = 1;
        User user = createUser(userId, "Jean", "jean@test.com");
        Car car = createCar(1, 5);
        List<Trip> mockTrips = List.of(
                createTrip(1, new java.sql.Date(System.currentTimeMillis()), new java.sql.Date(System.currentTimeMillis()), LocalTime.now(), "A", "B", "Paris", "Lyon", user, car),
                createTrip(2, new java.sql.Date(System.currentTimeMillis()), new java.sql.Date(System.currentTimeMillis()), LocalTime.now(), "C", "D", "Lyon", "Nice", user, car)
        );

        when(tripRepository.findUpcomingUserTrips(userId)).thenReturn(mockTrips);

        List<TripResponseDto> result = tripService.getUpcomingUserTrips(userId);

        assertEquals(2, result.size());
        verify(tripRepository).findUpcomingUserTrips(userId);
    }

    @Test
    void testGetPastUserTrips() {
        Integer userId = 2;
        User user = createUser(userId, "Marie", "marie@test.com");
        Car car = createCar(1, 5);
        List<Trip> mockTrips = List.of(
                createTrip(1, new java.sql.Date(System.currentTimeMillis()), new java.sql.Date(System.currentTimeMillis()), LocalTime.now(), "A", "B", "Paris", "Lyon", user, car)
        );

        when(tripRepository.findPastUserTrips(userId)).thenReturn(mockTrips);

        List<TripResponseDto> result = tripService.getPastUserTrips(userId);

        assertEquals(1, result.size());
        verify(tripRepository).findPastUserTrips(userId);
    }

    /**
     * Teste la mise à jour d'un trajet via {@link TripService#updateTrip}.
     */
    @Test
    void testUpdateTrip() throws FunctionnalException {
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

        doNothing().when(validationUtil).validateEndDateBeforeStartDate(any(), any());
        when(tripRepository.findById(tripId)).thenReturn(Optional.of(existingTrip));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(organisateur));
        when(carRepository.findById(anyInt())).thenReturn(Optional.of(car));

        doAnswer(invocation -> {
            Trip tripToUpdate = invocation.getArgument(0);
            TripRequestDto dto = invocation.getArgument(1);
            tripToUpdate.setHeureDepart(dto.getHeureDepart());
            tripToUpdate.setLieuArrivee(dto.getLieuArrivee());
            return null;
        }).when(tripMapper).updateEntity(any(), any(), any(), any());

        when(openRouteService.getTravelDurationInSeconds(anyString(), anyString())).thenReturn(7200.0);

        when(validationUtil.estVehiculeDeService(anyInt(), any())).thenReturn(false);
        when(subscribeRepository.countByTrip_Id(any())).thenReturn(1);
        when(tripRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(tripMapper.toResponse(any())).thenReturn(updatedResponseDto);

        doNothing().when(notificationService).sendNotificationToParticipantsOnModification(any(Trip.class));

        TripResponseDto result = tripService.updateTrip(tripId, requestDto);

        assertNotNull(result);
        verify(tripRepository).findById(tripId);
        verify(tripMapper).updateEntity(eq(existingTrip), eq(requestDto), eq(organisateur), eq(car));
        verify(tripRepository).save(existingTrip);
        verify(notificationService).sendNotificationToParticipantsOnModification(existingTrip);
        verify(tripMapper).toResponse(existingTrip);
        verify(tripMapper).toResponse(any(Trip.class));

    }

    /**
     * Teste la suppression d'un trajet via {@link TripService#deleteTrip}.
     */
    @Test
    void testDeleteTrip() throws FunctionnalException {
        User user = createUser(1, "Jean", "jean@test.com");
        User participant = createUser(2, "Paul", "paul@test.com");
        Car car = createCar(1, 5);
        Trip trip = createTrip(1, new java.sql.Date(System.currentTimeMillis()), new java.sql.Date(System.currentTimeMillis()), LocalTime.now(),
                "A", "B", "Paris", "Lyon", user, car);

        trip.getSubscribes().add(createSubscribe(participant, trip));

        when(tripRepository.findById(1)).thenReturn(Optional.of(trip));
        doNothing().when(notificationService).sendNotificationToParticipantsOnAnnulation(any(), any());
        doNothing().when(tripRepository).delete(any(Trip.class));


        tripService.deleteTrip(1);

        verify(tripRepository, times(1)).findById(1);
        ArgumentCaptor<List<Subscribe>> listCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<TripNotificationDetailsDto> dtoCaptor = ArgumentCaptor.forClass(TripNotificationDetailsDto.class);

        verify(notificationService).sendNotificationToParticipantsOnAnnulation(listCaptor.capture(), dtoCaptor.capture());

        assertEquals(1, listCaptor.getValue().size());
        assertEquals("Paris", dtoCaptor.getValue().getVilleDepart());

        verify(tripRepository, times(1)).delete(trip);
    }

    /**
     * Teste le calcul de l'heure d'arrivée.
     */
    @Test
    void testCalculateArrivalTime() throws FunctionnalException{
        LocalTime heureDepart = LocalTime.of(9, 0);
        String lieuDepart = "Centre-ville";
        String lieuArrivee = "Aéroport";
        String villeDepart = "Toulouse";
        String villeArrivee = "Paris";

        when(openRouteService.getTravelDurationInSeconds(
                "Centre-ville, Toulouse", "Aéroport, Paris"
        )).thenReturn(7200.0);

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
        TripRequestDto requestDto = createTripRequestDto();
        Car car = createCar(1, 5);

        when(subscribeRepository.countByTrip_Id(any())).thenReturn(2);

        int result = tripService.calculatePlaceRest(requestDto, car);

        assertEquals(3, result);
        verify(subscribeRepository).countByTrip_Id(requestDto.getId());
    }

    /**
     * Teste le calcul du nombre de places restantes avec véhicule de service.
     */
    @Test
    void testCalculatePlaceRestWithServiceVehicle() throws FunctionnalException {
        TripRequestDto requestDto = createTripRequestDto();
        Car car = createCar(1, 5);

        when(validationUtil.estVehiculeDeService(eq(1), any())).thenReturn(true);
        when(reservationRepository.existsByCompanyCar_IdAndUser_IdAndDateDebutAndDateFin(
                eq(1), eq(1), eq(requestDto.getDateDebut()), eq(requestDto.getDateFin())
        )).thenReturn(true);
        when(subscribeRepository.countByTrip_Id(any())).thenReturn(1);

        int result = tripService.calculatePlaceRest(requestDto, car);

        assertEquals(3, result);
        verify(validationUtil).estVehiculeDeService(eq(1), any());
        verify(reservationRepository).existsByCompanyCar_IdAndUser_IdAndDateDebutAndDateFin(
                eq(1), eq(1), eq(requestDto.getDateDebut()), eq(requestDto.getDateFin())
        );
    }
}
