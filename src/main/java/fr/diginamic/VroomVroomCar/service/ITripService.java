package fr.diginamic.VroomVroomCar.service;

import fr.diginamic.VroomVroomCar.dto.request.TripRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.CarResponseDto;
import fr.diginamic.VroomVroomCar.dto.response.TripResponseDto;
import fr.diginamic.VroomVroomCar.dto.response.UserResponseDto;
import fr.diginamic.VroomVroomCar.entity.Car;
import fr.diginamic.VroomVroomCar.entity.Trip;
import fr.diginamic.VroomVroomCar.entity.VehiculeType;
import fr.diginamic.VroomVroomCar.exception.FunctionnalException;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

/**
 * Cette interface définit les opérations de service pour la gestion des trajets.
 * Elle fournit des méthodes pour récupérer, créer, mettre à jour et supprimer des trajets,
 * ainsi que pour effectuer des recherches spécifiques.
 */
public interface ITripService {

    /**
     * Crée un nouveau trajet à partir des données fournies.
     * Le service récupère automatiquement les entités User et Car depuis la base de données
     * à partir des identifiants fournis dans le DTO.
     *
     * @param tripRequestDto les informations nécessaires à la création du trajet
     * @return le trajet créé sous forme de DTO
     * @throws FunctionnalException si les données sont invalides, si l'utilisateur ou le véhicule n'existe pas, ou si une règle métier est violée
     */
    TripResponseDto createTrip(TripRequestDto tripRequestDto) throws FunctionnalException;

    /**
     * Récupère la liste de tous les trajets existants.
     *
     * @return une liste de trajets sous forme de DTO
     */
    List<TripResponseDto> getAllTrips();

    /**
     * Récupère un trajet à partir de son identifiant.
     *
     * @param id l'identifiant du trajet recherché
     * @return le trajet correspondant sous forme de DTO
     * @throws FunctionnalException si aucun trajet ne correspond à l'identifiant fourni
     */
    TripResponseDto getTripById(Integer id) throws FunctionnalException;

    /**
     * Recherche les trajets en fonction des critères de filtre fournis.
     * Tous les paramètres sont optionnels, mais au moins un doit être présent pour que la recherche ait du sens.
     *
     * @param villeDepart   la ville de départ du trajet (peut être {@code null} pour ignorer ce critère)
     * @param villeArrivee  la ville d'arrivée du trajet (peut être {@code null} pour ignorer ce critère)
     * @param dateDebut     la date de début du trajet (peut être {@code null} pour ignorer ce critère)
     * @param heureDepart   l'heure de départ du trajet (peut être {@code null} pour ignorer ce critère)
     * @param vehiculeType  le type de véhicule souhaité (ex : SERVICE, PERSONNEL, TOUS)
     * @return une liste de trajets correspondant aux critères spécifiés
     * @throws FunctionnalException si une erreur fonctionnelle survient lors de la recherche
     */
    List<TripResponseDto> searchTrips(String villeDepart, String villeArrivee, Date dateDebut,
                           LocalTime heureDepart, VehiculeType vehiculeType) throws FunctionnalException;

    /**
     * Service permettant de récupérer les trajets futurs (à venir) d'un utilisateur,
     * qu'il en soit l'organisateur ou un participant inscrit.
     *
     * @param userId l'identifiant de l'utilisateur concerné
     * @return la liste des trajets futurs
     */
    List<Trip> getUpcomingUserTrips(Integer userId);

    /**
     * Service permettant de récupérer les trajets passés d'un utilisateur,
     * qu'il en soit l'organisateur ou un participant inscrit.
     *
     * @param userId l'identifiant de l'utilisateur concerné
     * @return la liste des trajets passés
     */
    List<Trip> getPastUserTrips(Integer userId);

    /**
     * Met à jour un trajet existant avec les nouvelles données fournies.
     * Le service récupère automatiquement les entités User et Car depuis la base de données
     * si les identifiants sont modifiés dans le DTO.
     *
     * @param id l'identifiant du trajet à mettre à jour
     * @param tripRequestDto les nouvelles données du trajet
     * @return le trajet mis à jour sous forme de DTO
     * @throws FunctionnalException si le trajet n'existe pas, si l'utilisateur ou le véhicule n'existe pas, ou si une règle métier est enfreinte
     */
    TripResponseDto updateTrip(Integer id, TripRequestDto tripRequestDto) throws FunctionnalException;

    /**
     * Supprime un trajet à partir de son identifiant.
     *
     * @param id l'identifiant du trajet à supprimer
     * @throws FunctionnalException si le trajet n'existe pas ou ne peut pas être supprimé
     */
    void deleteTrip(Integer id) throws FunctionnalException;

    /**
     * Calcule l'heure d'arrivée estimée en fonction de l'heure de départ et des lieux indiqués.
     *
     * @param heureDepart l'heure de départ
     * @param lieuDepart le lieu précis de départ (ex: adresse ou point de rendez-vous)
     * @param lieuArrivee le lieu précis d'arrivée
     * @param villeDepart la ville de départ
     * @param villeArrivee la ville d'arrivée
     * @return l'heure estimée d'arrivée
     */
    LocalTime calculateArrivalTime(LocalTime heureDepart, String lieuDepart,
                                   String lieuArrivee, String villeDepart, String villeArrivee);

    /**
     * Calcule le nombre de places restantes pour un trajet donné.
     *
     * Si le véhicule associé est un véhicule de service et que l'organisateur a déjà une réservation
     * pour ce véhicule aux mêmes dates, une place lui est réservée (décomptée). Ensuite, toutes les
     * inscriptions existantes sont également décomptées du nombre total de places disponibles.
     *
     * @param tripRequestDto les informations du trajet (dates, identifiant, etc.)
     * @param car l'entité véhicule utilisé pour le trajet
     * @return le nombre de places restantes disponibles, toujours ≥ 0
     * @throws FunctionnalException en cas d'erreur de logique métier (véhicule introuvable, etc.)
     */
    int calculatePlaceRest(TripRequestDto tripRequestDto, Car car) throws FunctionnalException;
}