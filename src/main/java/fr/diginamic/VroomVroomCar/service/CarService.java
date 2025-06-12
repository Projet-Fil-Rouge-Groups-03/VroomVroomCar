package fr.diginamic.VroomVroomCar.service;

import fr.diginamic.VroomVroomCar.dto.request.CarRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.CarResponseDto;
import fr.diginamic.VroomVroomCar.entity.Car;
import fr.diginamic.VroomVroomCar.entity.Categorie;
import fr.diginamic.VroomVroomCar.entity.User;
import fr.diginamic.VroomVroomCar.exception.ResourceNotFoundException;
import fr.diginamic.VroomVroomCar.mapper.CarMapper;
import fr.diginamic.VroomVroomCar.repository.CarRepository;
import fr.diginamic.VroomVroomCar.repository.UserRepository;
import fr.diginamic.VroomVroomCar.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CarService implements ICarService {

    @Autowired
    private final CarRepository carRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final CarMapper carMapper;

    // GET by ID

    @Override
    public CarResponseDto getCarById(Integer id) throws ResourceNotFoundException {
        ValidationUtil.validateIdNotNull(id, "la voiture");
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Voiture non trouvée avec l'ID: " + id));

        return carMapper.toResponseDto(car);
    }

    // GET all

    @Transactional(readOnly = true)
    @Override
    public List<CarResponseDto> getAllCars() {
        return carRepository.findAll().stream()
                .map(carMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // CREATE

    @Override
    public CarResponseDto createCar(CarRequestDto carRequestDto) throws ResourceNotFoundException {
        ValidationUtil.validateCarRequestDto(carRequestDto);

        User user = userRepository.findById(carRequestDto.getUtilisateurId())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'ID: " + carRequestDto.getUtilisateurId()));

        Car car = carMapper.toEntity(carRequestDto, user);
        Car savedCar = carRepository.save(car);

        return carMapper.toResponseDto(savedCar);
    }

    // UPDATE

    @Override
    public CarResponseDto updateCar(Integer id, CarRequestDto carRequestDto) throws ResourceNotFoundException{
        ValidationUtil.validateIdNotNull(id, "la voiture");
        ValidationUtil.validateCarRequestDto(carRequestDto);

        Car existingCar = carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Voiture non trouvée avec l'ID: " + id));

        User user = userRepository.findById(carRequestDto.getUtilisateurId())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'ID: " + carRequestDto.getUtilisateurId()));

        carMapper.updateEntity(existingCar, carRequestDto, user);
        Car updatedCar = carRepository.save(existingCar);

        return carMapper.toResponseDto(updatedCar);
    }

    // DELETE

    @Override
    public void deleteCar(Integer id) throws ResourceNotFoundException {
        ValidationUtil.validateIdNotNull(id, "la voiture");
        if (!carRepository.existsById(id)) {
            throw new ResourceNotFoundException("Voiture non trouvée avec l'ID: " + id);
        }
        carRepository.deleteById(id);
    }

    // GET + spécifiques

    @Transactional(readOnly = true)
    @Override
    public List<CarResponseDto> getCarsByUserId(Integer userId, int limit) {
        ValidationUtil.validateUserId(userId);
        ValidationUtil.validateLimit(limit);
        Pageable pageable = PageRequest.of(0, limit);
        return carRepository.findByUserId(userId, pageable).stream()
                .map(carMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<CarResponseDto> searchCarsByMarque(String marque, int limit) {
        ValidationUtil.validateStringNotEmpty(marque, "marque");
        ValidationUtil.validateLimit(limit);
        Pageable pageable = PageRequest.of(0, limit);
        return carRepository.findByMarqueContainingIgnoreCase(marque, pageable).stream()
                .map(carMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<CarResponseDto> searchCarsByModele(String modele, int limit) {
        ValidationUtil.validateStringNotEmpty(modele, "modèle");
        ValidationUtil.validateLimit(limit);
        Pageable pageable = PageRequest.of(0, limit);
        return carRepository.findByModeleContainingIgnoreCase(modele, pageable).stream()
                .map(carMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<CarResponseDto> getCarsByCategorie(Categorie categorie, int limit) {
        ValidationUtil.validateNotNull("La catégorie");
        Pageable pageable = PageRequest.of(0, limit);
        return carRepository.findByCategorie(categorie, pageable).stream()
                .map(carMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}
