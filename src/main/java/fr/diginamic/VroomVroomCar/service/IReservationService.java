package fr.diginamic.VroomVroomCar.service;

import fr.diginamic.VroomVroomCar.dto.request.ReservationRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.ReservationResponseDto;
import fr.diginamic.VroomVroomCar.exception.FunctionnalException;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Interface définissant les opérations métier liées à la gestion des réservations.
 * Fournit les méthodes nécessaires pour créer, lire, mettre à jour et supprimer des réservations.
 */
public interface IReservationService {

    /**
     * Crée une nouvelle réservation pour un véhicule donné.
     *
     * @param requestDto       Données de la réservation à créer
     * @return La réservation créée sous forme de DTO
     */
    ReservationResponseDto createReservation(ReservationRequestDto requestDto) throws FunctionnalException;

    /**
     * Récupère la liste de toutes les réservations enregistrées.
     *
     * @return Liste des réservations sous forme de DTO
     */
    List<ReservationResponseDto> getAllReservations();

    /**
     * Récupère une réservation à partir de son identifiant.
     *
     * @param id Identifiant de la réservation
     * @return La réservation correspondante sous forme de DTO
     */
    ReservationResponseDto getReservationById(Integer id) throws FunctionnalException;

    /**
     * Récupère une page de réservations associées à un véhicule donné.
     *
     * Cette méthode permet de paginer les résultats en spécifiant la taille de la page
     * et le numéro de page souhaité. Les réservations sont généralement triées par date de début.
     *
     * @param carId l'identifiant du véhicule pour lequel les réservations doivent être récupérées
     * @param page le numéro de la page (0 correspond à la première page)
     * @param size le nombre d'éléments par page
     * @return une page contenant des {@link ReservationResponseDto} pour le véhicule spécifié
     * @throws FunctionnalException si le véhicule spécifié n'existe pas (à gérer selon implémentation)
     */
    Page<ReservationResponseDto> getReservationsByCarId(Integer carId, int page, int size) throws FunctionnalException;

    /**
     * Met à jour une réservation existante.
     *
     * @param id              Identifiant de la réservation à modifier
     * @param requestDto      Nouvelles données de la réservation
     * @return La réservation mise à jour sous forme de DTO
     */
    ReservationResponseDto updateReservation(Integer id, ReservationRequestDto requestDto) throws FunctionnalException;

    /**
     * Supprime une réservation selon son identifiant.
     *
     * @param id Identifiant de la réservation à supprimer
     */
    void deleteReservation(Integer id) throws FunctionnalException;

    /**
     * Retourne toutes les voitures non louées actuellement.
     * @return Une liste de voitures mise sous forme de DTO.
     */
    List<CompanyCarResponseDto> getAllAvailableCompanyCars();
}
