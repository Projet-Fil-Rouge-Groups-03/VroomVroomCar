package fr.diginamic.VroomVroomCar.service;

import fr.diginamic.VroomVroomCar.dto.request.ReservationRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.CarResponseDto;
import fr.diginamic.VroomVroomCar.dto.response.ReservationResponseDto;
import fr.diginamic.VroomVroomCar.exception.FunctionnalException;

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
     * @param carResponseDto   Informations du véhicule concerné
     * @return La réservation créée sous forme de DTO
     */
    ReservationResponseDto createReservation(ReservationRequestDto requestDto, CarResponseDto carResponseDto) throws FunctionnalException;

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
     * Met à jour une réservation existante.
     *
     * @param id              Identifiant de la réservation à modifier
     * @param requestDto      Nouvelles données de la réservation
     * @param carResponseDto  Informations du véhicule concerné
     * @return La réservation mise à jour sous forme de DTO
     */
    ReservationResponseDto updateReservation(Integer id, ReservationRequestDto requestDto, CarResponseDto carResponseDto) throws FunctionnalException;

    /**
     * Supprime une réservation selon son identifiant.
     *
     * @param id Identifiant de la réservation à supprimer
     */
    void deleteReservation(Integer id) throws FunctionnalException;
}
