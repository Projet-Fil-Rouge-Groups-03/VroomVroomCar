package fr.diginamic.VroomVroomCar.service;

import fr.diginamic.VroomVroomCar.dto.request.CompanyCarRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.CompanyCarResponseDto;
import fr.diginamic.VroomVroomCar.entity.*;
import fr.diginamic.VroomVroomCar.exception.FunctionnalException;
import fr.diginamic.VroomVroomCar.exception.ResourceNotFoundException;
import fr.diginamic.VroomVroomCar.mapper.CompanyCarMapper;
import fr.diginamic.VroomVroomCar.repository.CompanyCarRepository;
import fr.diginamic.VroomVroomCar.repository.ReservationRepository;
import fr.diginamic.VroomVroomCar.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyCarServiceTest {
    @Mock
    private CompanyCarRepository companyCarRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private CompanyCarMapper companyCarMapper;

    @Mock
    private CarApiService carApiService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private CompanyCarService companyCarService;


    @Test
    void testGetCompanyCarById() throws ResourceNotFoundException {
        CompanyCar companyCar = new CompanyCar();
        CompanyCarResponseDto companyCarResponseDto = new CompanyCarResponseDto();

        when(companyCarRepository.findById(anyInt())).thenReturn(Optional.of(companyCar));
        when(companyCarMapper.toResponseDto(any(CompanyCar.class))).thenReturn(companyCarResponseDto);

        CompanyCarResponseDto result = companyCarService.getCompanyCarById(1);

        assertNotNull(result);
        verify(companyCarRepository, times(1)).findById(anyInt());
    }

    @Test
    void testGetAllCompanyCars() {
        CompanyCar companyCar = new CompanyCar();
        CompanyCarResponseDto companyCarResponseDto = new CompanyCarResponseDto();

        when(companyCarRepository.findAll()).thenReturn(Collections.singletonList(companyCar));
        when(companyCarMapper.toResponseDto(any(CompanyCar.class))).thenReturn(companyCarResponseDto);

        List<CompanyCarResponseDto> result = companyCarService.getAllCompanyCars();

        assertFalse(result.isEmpty());
        verify(companyCarRepository, times(1)).findAll();
    }

    @Test
    void testCreateCompanyCar() throws ResourceNotFoundException, FunctionnalException {
        CompanyCarRequestDto companyCarRequestDto = new CompanyCarRequestDto();
        companyCarRequestDto.setImmatriculation("ABC123");
        companyCarRequestDto.setUtilisateurId(1);
        companyCarRequestDto.setMarque("MarqueTest");
        companyCarRequestDto.setModele("ModeleTest");
        companyCarRequestDto.setMotorisation(Motorisation.ESSENCE);

        User user = new User();
        user.setId(1);

        CompanyCar companyCar = new CompanyCar();
        CompanyCarResponseDto companyCarResponseDto = new CompanyCarResponseDto();

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(companyCarMapper.toEntity(any(CompanyCarRequestDto.class), any(User.class))).thenReturn(companyCar);
        when(carApiService.updatePollutionFromApi(any(CompanyCar.class))).thenReturn(true);
        when(companyCarRepository.save(any(CompanyCar.class))).thenReturn(companyCar);
        when(companyCarMapper.toResponseDto(any(CompanyCar.class))).thenReturn(companyCarResponseDto);

        CompanyCarResponseDto result = companyCarService.createCompanyCar(companyCarRequestDto);

        assertNotNull(result);
        verify(userRepository, times(1)).findById(anyInt());
        verify(companyCarRepository, times(1)).save(any(CompanyCar.class));
    }

    @Test
    void testUpdateCar() throws ResourceNotFoundException {
        CompanyCarRequestDto companyCarRequestDto = new CompanyCarRequestDto();
        companyCarRequestDto.setUtilisateurId(1);
        companyCarRequestDto.setMarque("MarqueTest");
        companyCarRequestDto.setModele("ModeleTest");
        companyCarRequestDto.setMotorisation(Motorisation.ESSENCE);
        companyCarRequestDto.setStatus(CompanyCarStatus.EN_SERVICE);

        User user = new User();
        user.setId(1);

        CompanyCar existingCar = new CompanyCar();
        existingCar.setId(1);
        existingCar.setMarque("AncienneMarque");
        existingCar.setModele("AncienModele");
        existingCar.setMotorisation(Motorisation.HYBRIDE);
        existingCar.setUser(user);
        existingCar.setStatus(CompanyCarStatus.EN_SERVICE);

        CompanyCarResponseDto companyCarResponseDto = new CompanyCarResponseDto();

        when(companyCarRepository.findById(anyInt())).thenReturn(Optional.of(existingCar));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(carApiService.updatePollutionFromApi(any(CompanyCar.class))).thenReturn(true);
        when(companyCarRepository.save(any(CompanyCar.class))).thenReturn(existingCar);
        when(companyCarMapper.toResponseDto(any(CompanyCar.class))).thenReturn(companyCarResponseDto);

        CompanyCarResponseDto result = companyCarService.updateCar(1, companyCarRequestDto);

        assertNotNull(result);
        verify(companyCarRepository, times(1)).findById(anyInt());
        verify(companyCarRepository, times(1)).save(any(CompanyCar.class));
    }

    @Test
    void testUpdateCarStatusChangeToReparation() throws ResourceNotFoundException {
        Integer carId = 1;

        CompanyCarRequestDto companyCarRequestDto = new CompanyCarRequestDto();
        companyCarRequestDto.setUtilisateurId(1);
        companyCarRequestDto.setMarque("NouvelleMarque");
        companyCarRequestDto.setModele("NouveauModele");
        companyCarRequestDto.setMotorisation(Motorisation.ESSENCE);
        companyCarRequestDto.setStatus(CompanyCarStatus.REPARATION);

        User user = new User();
        user.setId(1);

        CompanyCar existingCar = new CompanyCar();
        existingCar.setId(carId);
        existingCar.setMarque("AncienneMarque");
        existingCar.setModele("AncienModele");
        existingCar.setMotorisation(Motorisation.ESSENCE);
        existingCar.setStatus(CompanyCarStatus.EN_SERVICE);
        existingCar.setUser(user);

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setCompanyCar(existingCar);
        reservation.setDateDebut(Date.valueOf(LocalDate.now().plusDays(1)));

        CompanyCarResponseDto responseDto = new CompanyCarResponseDto();

        when(companyCarRepository.findById(carId)).thenReturn(Optional.of(existingCar));
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(companyCarRepository.save(any(CompanyCar.class))).thenReturn(existingCar);
        when(reservationRepository.findByCompanyCarAndDateDebutAfter(eq(existingCar), any()))
                .thenReturn(Collections.singletonList(reservation));
        when(carApiService.updatePollutionFromApi(any(CompanyCar.class))).thenReturn(true);
        when(companyCarMapper.toResponseDto(existingCar)).thenReturn(responseDto);

        doAnswer(invocation -> {
            CompanyCar car = invocation.getArgument(0);
            CompanyCarRequestDto dto = invocation.getArgument(1);
            User u = invocation.getArgument(2);

            car.setMarque(dto.getMarque());
            car.setModele(dto.getModele());
            car.setMotorisation(dto.getMotorisation());
            car.setStatus(dto.getStatus());
            car.setUser(u);

            return null;
        }).when(companyCarMapper).updateEntity(any(CompanyCar.class), any(CompanyCarRequestDto.class), any(User.class));

        CompanyCarResponseDto result = companyCarService.updateCar(carId, companyCarRequestDto);

        assertNotNull(result);
        verify(companyCarRepository, times(1)).findById(carId);
        verify(userRepository, times(1)).findById(1);
        verify(carApiService, times(1)).updatePollutionFromApi(existingCar);
        verify(companyCarRepository, times(1)).save(existingCar);
        verify(reservationRepository, times(1)).findByCompanyCarAndDateDebutAfter(eq(existingCar), any());
        verify(notificationService, times(1)).sendNotificationToUsersOnCarStatusUpdate(
                eq(existingCar),
                eq(CompanyCarStatus.REPARATION.toString()),
                eq(user)
        );
        verify(companyCarMapper).toResponseDto(existingCar);
    }

    @Test
    void testUpdateCarStatusChangeToHorsService() throws ResourceNotFoundException {
        Integer carId = 1;

        CompanyCarRequestDto companyCarRequestDto = new CompanyCarRequestDto();
        companyCarRequestDto.setUtilisateurId(1);
        companyCarRequestDto.setMarque("NouvelleMarque");
        companyCarRequestDto.setModele("NouveauModele");
        companyCarRequestDto.setMotorisation(Motorisation.ESSENCE);
        companyCarRequestDto.setStatus(CompanyCarStatus.HORS_SERVICE);

        User user = new User();
        user.setId(1);

        CompanyCar existingCar = new CompanyCar();
        existingCar.setId(carId);
        existingCar.setMarque("AncienneMarque");
        existingCar.setModele("AncienModele");
        existingCar.setMotorisation(Motorisation.ESSENCE);
        existingCar.setStatus(CompanyCarStatus.EN_SERVICE);
        existingCar.setUser(user);

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setCompanyCar(existingCar);
        reservation.setDateDebut(Date.valueOf(LocalDate.now().plusDays(1)));

        CompanyCarResponseDto responseDto = new CompanyCarResponseDto();

        when(companyCarRepository.findById(carId)).thenReturn(Optional.of(existingCar));
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(companyCarRepository.save(any(CompanyCar.class))).thenReturn(existingCar);
        when(reservationRepository.findByCompanyCarAndDateDebutAfter(eq(existingCar), any()))
                .thenReturn(Collections.singletonList(reservation));
        when(carApiService.updatePollutionFromApi(any(CompanyCar.class))).thenReturn(true);
        when(companyCarMapper.toResponseDto(existingCar)).thenReturn(responseDto);

        doAnswer(invocation -> {
            CompanyCar car = invocation.getArgument(0);
            CompanyCarRequestDto dto = invocation.getArgument(1);
            User u = invocation.getArgument(2);

            car.setMarque(dto.getMarque());
            car.setModele(dto.getModele());
            car.setMotorisation(dto.getMotorisation());
            car.setStatus(dto.getStatus());
            car.setUser(u);

            return null;
        }).when(companyCarMapper).updateEntity(any(CompanyCar.class), any(CompanyCarRequestDto.class), any(User.class));

        CompanyCarResponseDto result = companyCarService.updateCar(carId, companyCarRequestDto);

        assertNotNull(result);
        verify(companyCarRepository, times(1)).findById(carId);
        verify(userRepository, times(1)).findById(1);
        verify(carApiService, times(1)).updatePollutionFromApi(existingCar);
        verify(companyCarRepository, times(1)).save(existingCar);
        verify(reservationRepository, times(1)).findByCompanyCarAndDateDebutAfter(eq(existingCar), any());
        verify(notificationService, times(1)).sendNotificationToUsersOnCarStatusUpdate(
                eq(existingCar),
                eq(CompanyCarStatus.HORS_SERVICE.toString()),
                eq(user)
        );
        verify(companyCarMapper).toResponseDto(existingCar);
    }


    @Test
    void testDeleteCar() throws ResourceNotFoundException {
        doNothing().when(companyCarRepository).deleteById(anyInt());
        when(companyCarRepository.existsById(anyInt())).thenReturn(true);

        companyCarService.deleteCar(1);

        verify(companyCarRepository, times(1)).deleteById(anyInt());
    }

    @Test
    void testSearchCarsByMarque() {
        Pageable pageable = PageRequest.of(0, 10);
        CompanyCar companyCar = new CompanyCar();
        CompanyCarResponseDto companyCarResponseDto = new CompanyCarResponseDto();

        when(companyCarRepository.findByMarqueContainingIgnoreCase(anyString(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(companyCar)));
        when(companyCarMapper.toResponseDto(any(CompanyCar.class))).thenReturn(companyCarResponseDto);

        List<CompanyCarResponseDto> result = companyCarService.searchCarsByMarque("Marque", 10);

        assertFalse(result.isEmpty());
        verify(companyCarRepository, times(1)).findByMarqueContainingIgnoreCase(anyString(), any(Pageable.class));
    }

    @Test
    void testSearchCarsByImmatriculation() {
        Pageable pageable = PageRequest.of(0, 10);
        CompanyCar companyCar = new CompanyCar();
        CompanyCarResponseDto companyCarResponseDto = new CompanyCarResponseDto();

        when(companyCarRepository.findByImmatriculationContainingIgnoreCase(anyString(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(companyCar)));
        when(companyCarMapper.toResponseDto(any(CompanyCar.class))).thenReturn(companyCarResponseDto);

        List<CompanyCarResponseDto> result = companyCarService.searchCarsByImmatriculation("ABC123", 10);

        assertFalse(result.isEmpty());
        verify(companyCarRepository, times(1)).findByImmatriculationContainingIgnoreCase(anyString(), any(Pageable.class));
    }

    @Test
    void testSearchCompanyCars() throws FunctionnalException {
        String marque = "Renault";
        String modele = "Twingo";
        int nbDePlaces = 5;
        Date dateDebut = Date.valueOf(LocalDate.now().plusDays(1)); // Demain
        Date dateFin = Date.valueOf(LocalDate.now().plusDays(3));   // Dans 3 jours

        // Création des DTOs de réponse attendus
        List<CompanyCarResponseDto> expectedCars = Arrays.asList(
                createCarDto(1L, "Renault", "Twingo", 5),
                createCarDto(2L, "Renault", "Twingo", 7),
                createCarDto(3L, "Renault", "Twingo", 5)
        );

        // Mock du repository
        when(companyCarRepository.findCompanyCarWithFilters(
                eq(marque), eq(modele), eq(nbDePlaces), eq(dateDebut), eq(dateFin)))
                .thenReturn(expectedCars);

        List<CompanyCarResponseDto> result = companyCarService.searchCompanyCar(
                marque, modele, nbDePlaces, dateDebut, dateFin);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(3);
        assertThat(result.get(0).getMarque()).isEqualTo("Renault");
        assertThat(result.get(0).getModele()).isEqualTo("Twingo");
        assertThat(result.get(0).getNbDePlaces()).isEqualTo(5);

        // Vérification que le repository a été appelé avec les bons paramètres
        verify(companyCarRepository, times(1)).findCompanyCarWithFilters(
                marque, modele, nbDePlaces, dateDebut, dateFin);

        // Test avec paramètres NULL (filtres optionnels)
        List<CompanyCarResponseDto> allCars = Arrays.asList(
                createCarDto(4L, "Peugeot", "308", 5),
                createCarDto(5L, "Citroën", "C3", 5)
        );

        when(companyCarRepository.findCompanyCarWithFilters(
                isNull(), isNull(), eq(0), isNull(), isNull()))
                .thenReturn(allCars);

        List<CompanyCarResponseDto> resultAll = companyCarService.searchCompanyCar(
                null, null, 0, null, null);

        assertThat(resultAll).hasSize(2);

        verify(companyCarRepository, times(1)).findCompanyCarWithFilters(
                null, null, 0, null, null);

        // Test avec liste vide (aucune voiture trouvée)
        when(companyCarRepository.findCompanyCarWithFilters(
                eq("Toyota"), eq("Prius"), eq(4), any(Date.class), any(Date.class)))
                .thenReturn(Collections.emptyList());

        List<CompanyCarResponseDto> resultEmpty = companyCarService.searchCompanyCar(
                "Toyota", "Prius", 4, dateDebut, dateFin);

        assertThat(resultEmpty).isEmpty();

        // Test de validation des dates (dates inversées)
        Date dateFinPassee = Date.valueOf(LocalDate.now().minusDays(1));

        assertThatThrownBy(() ->
                companyCarService.searchCompanyCar(marque, modele, nbDePlaces, dateDebut, dateFinPassee))
                .isInstanceOf(FunctionnalException.class)
                .hasMessageContaining("La date de fin doit être postérieure à la date de début.");

        // Test avec dates identiques (même jour)
        Date memeDate = Date.valueOf(LocalDate.now().plusDays(5));
        List<CompanyCarResponseDto> sameDayResult = Arrays.asList(
                createCarDto(6L, "Renault", "Twingo", 5)
        );

        when(companyCarRepository.findCompanyCarWithFilters(
                eq(marque), eq(modele), eq(nbDePlaces), eq(memeDate), eq(memeDate)))
                .thenReturn(sameDayResult);

        List<CompanyCarResponseDto> resultSameDay = companyCarService.searchCompanyCar(
                marque, modele, nbDePlaces, memeDate, memeDate);

        assertThat(resultSameDay).hasSize(1);

        // Vérifier le nombre total d'appels au repository
        verify(companyCarRepository, times(4)).findCompanyCarWithFilters(
                any(), any(), anyInt(), any(), any());

        // Vérifier qu'aucune autre méthode du repository n'a été appelée
        verifyNoMoreInteractions(companyCarRepository);

        System.out.println("✅ Tous les scénarios de test sont passés avec succès !");
    }

    private CompanyCarResponseDto createCarDto(Long id, String marque, String modele, int nbDePlaces) {
        CompanyCarResponseDto dto = new CompanyCarResponseDto();
        dto.setId(Math.toIntExact(id));
        dto.setMarque(marque);
        dto.setModele(modele);
        dto.setNbDePlaces(nbDePlaces);
        return dto;
    }

}