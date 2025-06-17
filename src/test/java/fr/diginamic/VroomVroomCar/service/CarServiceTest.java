package fr.diginamic.VroomVroomCar.service;

import static org.junit.jupiter.api.Assertions.*;

import fr.diginamic.VroomVroomCar.dto.request.CarRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.CarResponseDto;
import fr.diginamic.VroomVroomCar.entity.Car;
import fr.diginamic.VroomVroomCar.entity.Motorisation;
import fr.diginamic.VroomVroomCar.entity.User;
import fr.diginamic.VroomVroomCar.exception.ResourceNotFoundException;
import fr.diginamic.VroomVroomCar.mapper.CarMapper;
import fr.diginamic.VroomVroomCar.repository.CarRepository;
import fr.diginamic.VroomVroomCar.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceTest {
    @Mock
    private CarRepository carRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CarMapper carMapper;

    @Mock
    private CarApiService carApiService;

    @InjectMocks
    private CarService carService;

    @BeforeEach
    void setUp() {}

    @Test
    void testGetCarById() throws ResourceNotFoundException {
        Car car = new Car();
        CarResponseDto carResponseDto = new CarResponseDto();

        when(carRepository.findById(anyInt())).thenReturn(Optional.of(car));
        when(carMapper.toResponseDto(any(Car.class))).thenReturn(carResponseDto);

        CarResponseDto result = carService.getCarById(1);

        assertNotNull(result);
        verify(carRepository, times(1)).findById(anyInt());
    }

    @Test
    void testGetAllCars() {
        Car car = new Car();
        CarResponseDto carResponseDto = new CarResponseDto();

        when(carRepository.findAll()).thenReturn(Collections.singletonList(car));
        when(carMapper.toResponseDto(any(Car.class))).thenReturn(carResponseDto);

        List<CarResponseDto> result = carService.getAllCars();

        assertFalse(result.isEmpty());
        verify(carRepository, times(1)).findAll();
    }

    @Test
    void testCreateCar() throws ResourceNotFoundException {
        CarRequestDto carRequestDto = new CarRequestDto();
        carRequestDto.setUtilisateurId(1);
        User user = new User();
        Car car = new Car();
        CarResponseDto carResponseDto = new CarResponseDto();

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(carMapper.toEntity(any(CarRequestDto.class), any(User.class))).thenReturn(car);
        when(carApiService.updatePollutionFromApi(any(Car.class))).thenReturn(true);
        when(carRepository.save(any(Car.class))).thenReturn(car);
        when(carMapper.toResponseDto(any(Car.class))).thenReturn(carResponseDto);

        CarResponseDto result = carService.createCar(carRequestDto);

        assertNotNull(result);
        verify(userRepository, times(1)).findById(anyInt());
        verify(carRepository, times(1)).save(any(Car.class));
    }

    @Test
    void testUpdateCar() throws ResourceNotFoundException {
        CarRequestDto carRequestDto = new CarRequestDto();
        carRequestDto.setUtilisateurId(1);
        carRequestDto.setMarque("MarqueTest");
        carRequestDto.setModele("ModeleTest");
        carRequestDto.setMotorisation(Motorisation.ESSENCE);

        User user = new User();
        user.setId(1);

        Car existingCar = new Car();
        existingCar.setId(1);
        existingCar.setMarque("AncienneMarque");
        existingCar.setModele("AncienModele");
        existingCar.setMotorisation(Motorisation.HYBRIDE);
        existingCar.setUser(user);

        CarResponseDto carResponseDto = new CarResponseDto();

        when(carRepository.findById(anyInt())).thenReturn(Optional.of(existingCar));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(carApiService.updatePollutionFromApi(any(Car.class))).thenReturn(true);
        when(carRepository.save(any(Car.class))).thenReturn(existingCar);
        when(carMapper.toResponseDto(any(Car.class))).thenReturn(carResponseDto);

        CarResponseDto result = carService.updateCar(1, carRequestDto);

        assertNotNull(result);
        verify(carRepository, times(1)).findById(anyInt());
        verify(carRepository, times(1)).save(any(Car.class));
    }

    @Test
    void testDeleteCar() throws ResourceNotFoundException {
        doNothing().when(carRepository).deleteById(anyInt());
        when(carRepository.existsById(anyInt())).thenReturn(true);

        carService.deleteCar(1);

        verify(carRepository, times(1)).deleteById(anyInt());
    }

    @Test
    void testGetCarsByUserId() {
        Pageable pageable = PageRequest.of(0, 10);
        Car car = new Car();
        CarResponseDto carResponseDto = new CarResponseDto();

        when(carRepository.findByUserId(anyInt(), any(Pageable.class))).thenReturn(new PageImpl<>(Collections.singletonList(car)));
        when(carMapper.toResponseDto(any(Car.class))).thenReturn(carResponseDto);

        List<CarResponseDto> result = carService.getCarsByUserId(1, 10);

        assertFalse(result.isEmpty());
        verify(carRepository, times(1)).findByUserId(anyInt(), any(Pageable.class));
    }
}