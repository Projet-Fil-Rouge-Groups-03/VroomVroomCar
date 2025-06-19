package fr.diginamic.VroomVroomCar.controller;

import fr.diginamic.VroomVroomCar.dto.request.TripRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.CarResponseDto;
import fr.diginamic.VroomVroomCar.dto.response.TripResponseDto;
import fr.diginamic.VroomVroomCar.dto.response.UserResponseDto;
import fr.diginamic.VroomVroomCar.entity.Trip;
import fr.diginamic.VroomVroomCar.entity.VehiculeType;
import fr.diginamic.VroomVroomCar.exception.FunctionnalException;
import fr.diginamic.VroomVroomCar.exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalTime;
import java.util.List;

/**
 * Interface du contrôleur REST pour la gestion des trajets.
 *
 * Cette interface définit les points d'entrée de l'API permettant de créer,
 * lire, mettre à jour et supprimer des trajets.
 */
@Tag(name = "Trips", description = "API permettant la gestion complète des trajets (création, consultation, mise à jour, suppression)")
@RequestMapping("/api/trips")
public interface ITripController {

    /**
     * Crée un nouveau trajet à partir des données reçues.
     *
     * @param tripRequestDto les données du trajet à créer
     * @param userResponseDto les données de l'organisateur
     * @param carResponseDto les données du véhicule utilisé
     * @return le trajet créé avec son identifiant
     * @throws ResourceNotFoundException si une ressource liée (véhicule, utilisateur...) est introuvable
     * @throws FunctionnalException en cas d'erreur métier (ex : réservation en conflit)
     */
    @Operation(summary = "Créer un nouveau trajet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Trajet créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "404", description = "Ressource liée non trouvée")
    })
    @PostMapping("/create")
    ResponseEntity<TripResponseDto> createTrip(
            @Valid @RequestBody TripRequestDto tripRequestDto,
            UserResponseDto userResponseDto, CarResponseDto carResponseDto
    ) throws ResourceNotFoundException, FunctionnalException;

    /**
     * Récupère la liste de tous les trajets existants.
     *
     * @return une liste de trajets
     */
    @Operation(summary = "Récupérer tous les trajets")
    @ApiResponse(responseCode = "200", description = "Liste des trajets")
    @GetMapping
    ResponseEntity<List<TripResponseDto>> getAllTrips();

    /**
     * Récupère un trajet à partir de son identifiant.
     *
     * @param id l'identifiant du trajet à récupérer
     * @return le trajet correspondant
     * @throws FunctionnalException si le trajet n'existe pas
     */
    @Operation(summary = "Récupérer un trajet par son identifiant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trajet trouvé"),
            @ApiResponse(responseCode = "404", description = "Trajet non trouvé")
    })
    @GetMapping("/{id}")
    ResponseEntity<TripResponseDto> getTripById(
            @Parameter(description = "ID du trajet", required = true) @PathVariable Integer id
    ) throws FunctionnalException;

    /**
     * Recherche des trajets en fonction de critères facultatifs tels que la ville de départ,
     * la ville d'arrivée, la date de début, l'heure de départ et le type de véhicule.
     *
     * @param villeDepart   la ville de départ (facultative)
     * @param villeArrivee  la ville d'arrivée (facultative)
     * @param dateDebut     la date de début du trajet (facultative, au format ISO: yyyy-MM-dd)
     * @param heureDepart   l'heure de départ du trajet (facultative, au format ISO: HH:mm:ss)
     * @param vehiculeType  le type de véhicule souhaité (par défaut : TOUS)
     * @return une liste de trajets correspondant aux critères, ou un code 400 en cas d'erreur de requête
     */
    @Operation(
            summary = "Recherche de trajets",
            description = "Permet de rechercher des trajets selon différents critères (ville de départ, d'arrivée, date, heure, type de véhicule)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des trajets trouvés"),
            @ApiResponse(responseCode = "400", description = "Paramètres invalides ou erreur fonctionnelle")
    })
    @GetMapping("/search")
    ResponseEntity<List<Trip>> searchTrips(
            @Parameter(description = "Ville de départ")
            @RequestParam(required = false) String villeDepart,

            @Parameter(description = "Ville d'arrivée")
            @RequestParam(required = false) String villeArrivee,

            @Parameter(description = "Date de début (format: yyyy-MM-dd)")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateDebut,

            @Parameter(description = "Heure de départ (format: HH:mm:ss)")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime heureDepart,

            @Parameter(description = "Type de véhicule (ex: SERVICE, PERSONNEL, TOUS)")
            @RequestParam(defaultValue = "TOUS") VehiculeType vehiculeType);

    /**
     * Récupère la liste des trajets à venir pour un utilisateur donné.
     * L'utilisateur peut être organisateur ou simple passager.
     *
     * @param userId l'identifiant de l'utilisateur
     * @return liste des trajets futurs triés par date croissante
     */
    @Operation(summary = "Récupérer les trajets à venir d'un utilisateur",
            description = "Retourne la liste des trajets futurs auxquels un utilisateur participe ou qu'il organise.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des trajets récupérée avec succès"),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    List<Trip> getUpcomingTrip(@PathVariable Integer userId) throws ResourceNotFoundException;
    /**
     * Récupère la liste des trajets passés pour un utilisateur donné.
     * L'utilisateur peut être organisateur ou simple passager (abonné).
     *
     * @param userId l'identifiant de l'utilisateur
     * @return liste des trajets passés triés par date décroissante
     */
    @Operation(summary = "Récupérer les trajets passés d'un utilisateur",
            description = "Retourne la liste des trajets déjà effectués par un utilisateur, qu’il soit organisateur ou passager.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des trajets récupérée avec succès"),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    List<Trip> getPastTrip(@PathVariable Integer userId) throws ResourceNotFoundException;

    /**
     * Met à jour un trajet existant avec de nouvelles données.
     *
     * @param id l'identifiant du trajet à modifier
     * @param tripRequestDto les nouvelles données du trajet
     * @return le trajet mis à jour
     * @throws FunctionnalException si le trajet n'existe pas ou en cas d’erreur métier
     */
    @Operation(summary = "Mettre à jour un trajet existant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trajet mis à jour"),
            @ApiResponse(responseCode = "404", description = "Trajet non trouvé")
    })
    @PutMapping("/update/{id}")
    ResponseEntity<TripResponseDto> updateTrip(
            @Parameter(description = "ID du trajet", required = true) @PathVariable Integer id,
            @Valid @RequestBody TripRequestDto tripRequestDto,
            UserResponseDto userResponseDto, CarResponseDto carResponseDto
    ) throws FunctionnalException;

    /**
     * Supprime un trajet existant à partir de son identifiant.
     *
     * @param id l'identifiant du trajet à supprimer
     * @return un message de confirmation
     * @throws FunctionnalException si le trajet n'existe pas
     */
    @Operation(summary = "Supprimer un trajet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trajet supprimé"),
            @ApiResponse(responseCode = "404", description = "Trajet non trouvé")
    })
    @DeleteMapping("/delete/{id}")
    ResponseEntity<String> deleteTrip(
            @Parameter(description = "ID du trajet", required = true) @PathVariable Integer id
    ) throws FunctionnalException;
}
