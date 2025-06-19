package fr.diginamic.VroomVroomCar.service;

import fr.diginamic.VroomVroomCar.dto.request.ReservationRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.CompanyCarResponseDto;
import fr.diginamic.VroomVroomCar.dto.response.ReservationResponseDto;
import fr.diginamic.VroomVroomCar.dto.response.UserResponseDto;
import fr.diginamic.VroomVroomCar.entity.CompanyCar;
import fr.diginamic.VroomVroomCar.entity.Reservation;
import fr.diginamic.VroomVroomCar.exception.FunctionnalException;
import fr.diginamic.VroomVroomCar.mapper.CompanyCarMapper;
import fr.diginamic.VroomVroomCar.mapper.ReservationMapper;
import fr.diginamic.VroomVroomCar.repository.CompanyCarRepository;
import fr.diginamic.VroomVroomCar.repository.ReservationRepository;
import fr.diginamic.VroomVroomCar.util.ValidationUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationMapper reservationMapper;

    @Mock
    private CompanyCarRepository companyCarRepository;
    @Mock
    private CompanyCarMapper companyCarMapper;

    @Mock
    private ValidationUtil validationUtil;

    @InjectMocks
    private ReservationService reservationService;

    private ReservationRequestDto createReservationRequestDto() {
        ReservationRequestDto requestDto = new ReservationRequestDto();
        requestDto.setDateDebut(Date.valueOf(LocalDate.now()));
        requestDto.setDateFin(Date.valueOf(LocalDate.now().plusDays(1)));
        return requestDto;
    }

    private UserResponseDto createUserResponseDto() {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(1);
        return userResponseDto;
    }

    private CompanyCarResponseDto createCompanyCarResponseDto() {
        CompanyCarResponseDto carResponseDto = new CompanyCarResponseDto();
        carResponseDto.setId(1);
        return carResponseDto;
    }

    private Reservation createReservation() {
        Reservation reservation = new Reservation();
        reservation.setId(1);
        reservation.setDateDebut(Date.valueOf(LocalDate.now()));
        reservation.setDateFin(Date.valueOf(LocalDate.now().plusDays(1)));
        return reservation;
    }

    @Test
    void testCreateReservation() throws FunctionnalException {
        ReservationRequestDto requestDto = createReservationRequestDto();
        UserResponseDto userResponseDto = createUserResponseDto();
        CompanyCarResponseDto carResponseDto = createCompanyCarResponseDto();

        Reservation reservation = createReservation();
        ReservationResponseDto responseDto = new ReservationResponseDto();

        when(reservationMapper.toEntity(any(ReservationRequestDto.class), any(UserResponseDto.class), any(CompanyCarResponseDto.class))).thenReturn(reservation);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        when(reservationMapper.toResponse(any(Reservation.class))).thenReturn(responseDto);

        ReservationResponseDto result = reservationService.createReservation(requestDto, userResponseDto, carResponseDto);

        assertNotNull(result);
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void testGetAllReservations() {
        Reservation reservation = createReservation();
        ReservationResponseDto responseDto = new ReservationResponseDto();

        when(reservationRepository.findAll()).thenReturn(Collections.singletonList(reservation));
        when(reservationMapper.toResponse(any(Reservation.class))).thenReturn(responseDto);

        List<ReservationResponseDto> result = reservationService.getAllReservations();

        assertFalse(result.isEmpty());
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    void testGetReservationById() throws FunctionnalException {
        Reservation reservation = createReservation();
        ReservationResponseDto responseDto = new ReservationResponseDto();

        when(reservationRepository.findById(anyInt())).thenReturn(Optional.of(reservation));
        when(reservationMapper.toResponse(any(Reservation.class))).thenReturn(responseDto);

        ReservationResponseDto result = reservationService.getReservationById(1);

        assertNotNull(result);
        verify(reservationRepository, times(1)).findById(anyInt());
    }

    @Test
    void testGetReservationsByCarId() throws FunctionnalException {
        int carId = 1;
        int page = 0;
        int size = 10;

        Reservation reservation = createReservation();
        ReservationResponseDto responseDto = new ReservationResponseDto();

        Pageable pageable = PageRequest.of(page, size, Sort.by("dateDebut").descending());
        Page<Reservation> reservationPage = new PageImpl<>(Collections.singletonList(reservation));

        when(companyCarRepository.existsById(carId)).thenReturn(true);
        when(reservationRepository.findByCompanyCar_Id(eq(carId), any(Pageable.class))).thenReturn(reservationPage);
        when(reservationMapper.toResponse(any(Reservation.class))).thenReturn(responseDto);

        Page<ReservationResponseDto> result = reservationService.getReservationsByCarId(carId, page, size);

        assertFalse(result.isEmpty());
        verify(reservationRepository, times(1)).findByCompanyCar_Id(eq(carId), any(Pageable.class));
    }

    @Test
    void testUpdateReservation() throws FunctionnalException {
        int reservationId = 1;
        ReservationRequestDto requestDto = createReservationRequestDto();
        UserResponseDto userResponseDto = createUserResponseDto();
        CompanyCarResponseDto carResponseDto = createCompanyCarResponseDto();

        Reservation existingReservation = createReservation();
        ReservationResponseDto responseDto = new ReservationResponseDto();

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(existingReservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(existingReservation);
        when(reservationMapper.toResponse(any(Reservation.class))).thenReturn(responseDto);

        ReservationResponseDto result = reservationService.updateReservation(reservationId, requestDto, userResponseDto, carResponseDto);

        assertNotNull(result);
        verify(reservationRepository, times(1)).findById(reservationId);
        verify(reservationRepository, times(1)).save(existingReservation);
    }

    @Test
    void testDeleteReservation() throws FunctionnalException {
        int reservationId = 1;

        when(reservationRepository.existsById(reservationId)).thenReturn(true);
        doNothing().when(reservationRepository).deleteById(reservationId);

        reservationService.deleteReservation(reservationId);

        verify(reservationRepository, times(1)).deleteById(reservationId);
    }
    @Test
    void getAvailableCars() {
        CompanyCar car1 = new CompanyCar();
        car1.setId(1);
        CompanyCar car2 = new CompanyCar();
        car2.setId(2);
        CompanyCar car3 = new CompanyCar();
        car3.setId(3);

        List<CompanyCar> allCars = Arrays.asList(car1, car2, car3);

        Reservation activeReservation = new Reservation();
        activeReservation.setCompanyCar(car2);
        activeReservation.setDateFin(Date.valueOf(LocalDate.now().plusDays(1))); // encore réservée

        when(companyCarRepository.findAll()).thenReturn(allCars);
        when(reservationRepository.findAll()).thenReturn(List.of(activeReservation));

        CompanyCarResponseDto dto1 = new CompanyCarResponseDto();
        CompanyCarResponseDto dto3 = new CompanyCarResponseDto();

        when(companyCarMapper.toResponseDto(car1)).thenReturn(dto1);
        when(companyCarMapper.toResponseDto(car3)).thenReturn(dto3);

        List<CompanyCarResponseDto> result = reservationService.getAllAvailableCompanyCars();

        assertEquals(2, result.size());
        assertTrue(result.contains(dto1));
        assertTrue(result.contains(dto3));
        assertFalse(result.contains(car2));

        verify(companyCarRepository).findAll();
        verify(reservationRepository).findAll();
        verify(companyCarMapper).toResponseDto(car1);
        verify(companyCarMapper).toResponseDto(car3);
        verify(companyCarMapper, never()).toResponseDto(car2);
    }
}