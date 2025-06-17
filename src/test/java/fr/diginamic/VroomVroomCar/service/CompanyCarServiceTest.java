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
import java.util.Collections;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

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

}