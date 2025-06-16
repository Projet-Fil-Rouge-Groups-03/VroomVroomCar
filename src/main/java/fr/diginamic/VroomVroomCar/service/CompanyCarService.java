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
import fr.diginamic.VroomVroomCar.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CompanyCarService implements ICompanyCarService {

    private final CompanyCarRepository companyCarRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final CompanyCarMapper companyCarMapper;
    private final CarApiService carApiService;
    private final NotificationService notificationService;

    // GET by ID

    @Override
    public CompanyCarResponseDto getCompanyCarById(Integer id) throws ResourceNotFoundException {
        ValidationUtil.validateIdNotNull(id, "la voiture de service");
        CompanyCar car = companyCarRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Voiture de service non trouvée avec l'ID: " + id));

        return companyCarMapper.toResponseDto(car);
    }

    // GET all

    @Transactional(readOnly = true)
    @Override
    public List<CompanyCarResponseDto> getAllCompanyCars() {
        return companyCarRepository.findAll().stream()
                .map(companyCarMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // CREATE

    @Override
    public CompanyCarResponseDto createCompanyCar(CompanyCarRequestDto companyCarRequestDto) throws ResourceNotFoundException, FunctionnalException {
        if (companyCarRepository.existsByImmatriculation(companyCarRequestDto.getImmatriculation())) {
            throw new FunctionnalException("Une voiture avec cette immatriculation existe déjà: " + companyCarRequestDto.getImmatriculation());
        }

        User user = userRepository.findById(companyCarRequestDto.getUtilisateurId())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'ID: " + companyCarRequestDto.getUtilisateurId()));

        CompanyCar companyCar = companyCarMapper.toEntity(companyCarRequestDto, user);
        boolean apiSuccess = carApiService.updatePollutionFromApi(companyCar);

        if (!apiSuccess) {
            log.info("Impossible de récupérer les données CO2 via API pour {} {}, " +
                    "utilisation des valeurs par défaut", companyCar.getMarque(), companyCar.getModele());
        }
        CompanyCar savedCar = companyCarRepository.save(companyCar);

        return companyCarMapper.toResponseDto(savedCar);
    }

    // UPDATE
    @Override
    public CompanyCarResponseDto updateCar(Integer id, CompanyCarRequestDto companyCarRequestDto) throws ResourceNotFoundException{
        ValidationUtil.validateIdNotNull(id, "la voiture de service");
        if (companyCarRequestDto == null) {
            throw new IllegalArgumentException("Le DTO de requête de voiture de service ne peut pas être null.");
        }

        CompanyCar existingCar = companyCarRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Voiture non trouvée avec l'ID: " + id));

        User user = userRepository.findById(companyCarRequestDto.getUtilisateurId())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'ID: " + companyCarRequestDto.getUtilisateurId()));

        boolean shouldUpdatePollution = !existingCar.getMarque().equals(companyCarRequestDto.getMarque()) ||
                !existingCar.getModele().equals(companyCarRequestDto.getModele()) ||
                !existingCar.getMotorisation().equals(companyCarRequestDto.getMotorisation());
        CompanyCarStatus oldStatus = existingCar.getStatus();

        companyCarMapper.updateEntity(existingCar, companyCarRequestDto, user);

        if (shouldUpdatePollution) {
            carApiService.updatePollutionFromApi(existingCar);
        }

        CompanyCar updatedCompanyCar = companyCarRepository.save(existingCar);

        // Si statut changé vers un reparation ou hors_service, notifier les utilisateurs qui ont reservés
        if (!oldStatus.equals(updatedCompanyCar.getStatus())
                && (updatedCompanyCar.getStatus() == CompanyCarStatus.REPARATION || updatedCompanyCar.getStatus() == CompanyCarStatus.HORS_SERVICE)) {

            List<Reservation> futureReservations = reservationRepository.findByCarAndDateDebutAfter(updatedCompanyCar, LocalDateTime.now());

            for (Reservation reservation : futureReservations) {
                notificationService.sendNotificationToUsersOnCarStatusUpdate(updatedCompanyCar, updatedCompanyCar.getStatus().toString(), reservation.getUser());
            }
        }

        return companyCarMapper.toResponseDto(updatedCompanyCar);
    }

    // DELETE
    @Override
    public void deleteCar(Integer id) throws ResourceNotFoundException {
        ValidationUtil.validateIdNotNull(id, "la voiture");
        if (!companyCarRepository.existsById(id)) {
            throw new ResourceNotFoundException("Voiture non trouvée avec l'ID: " + id);
        }
        companyCarRepository.deleteById(id);
    }

    // GET + spécifiques

    @Transactional(readOnly = true)
    @Override
    public List<CompanyCarResponseDto> searchCarsByMarque(String marque, int limit) {
        ValidationUtil.validateStringNotEmpty(marque, "marque");
        ValidationUtil.validateLimit(limit);
        Pageable pageable = PageRequest.of(0, limit);
        return companyCarRepository.findByMarqueContainingIgnoreCase(marque, pageable).stream()
                .map(companyCarMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<CompanyCarResponseDto> searchCarsByModele(String modele, int limit) {
        ValidationUtil.validateStringNotEmpty(modele, "modèle");
        ValidationUtil.validateLimit(limit);
        Pageable pageable = PageRequest.of(0, limit);
        return companyCarRepository.findByModeleContainingIgnoreCase(modele, pageable).stream()
                .map(companyCarMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<CompanyCarResponseDto> getCarsByCategorie(Categorie categorie, int limit) {
        ValidationUtil.validateNotNull("La catégorie");
        Pageable pageable = PageRequest.of(0, limit);
        return companyCarRepository.findByCategorie(categorie, pageable).stream()
                .map(companyCarMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<CompanyCarResponseDto> searchCarsByImmatriculation(String immatriculation, int limit) {
        ValidationUtil.validateStringNotEmpty(immatriculation, "immatriculation");
        ValidationUtil.validateLimit(limit);
        Pageable pageable = PageRequest.of(0, limit);
        return companyCarRepository.findByImmatriculationContainingIgnoreCase(immatriculation, pageable).stream()
                .map(companyCarMapper::toResponseDto)
                .collect(Collectors.toList());
    }

}
