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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
     * Les entités User et Car sont automatiquement récupérées par le service
     * à partir des identifiants fournis dans le TripRequestDto.
     *
     * @param tripRequestDto les données du trajet à créer (incluant organisateurId et carId)
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
            @Valid @RequestBody TripRequestDto tripRequestDto
    ) throws ResourceNotFoundException, FunctionnalException;

    /**
     * Récupère la liste de tous les trajets existants.
     *
     * @return une liste de trajets
     */
    @Operation(summary = "Récupérer tous les trajets")
    @ApiResponse(responseCode = "200", description = "Liste des trajets")
    @GetMapping
    ResponseEntity<Page<TripResponseDto>> getAllTrips(
            @Parameter(description = "Numéro de la page à récupérer. La numérotation des pages commence à zéro.")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Nombre d'éléments par page.")
            @RequestParam(defaultValue = "5") int size
    );

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
     * @param villeDepart la ville de départ (facultative)
     * @param villeArrivee la ville d'arrivée (facultative)
     * @param dateDebutStr la date de début du trajet (facultative, au format ISO: yyyy-MM-dd)
     * @param heureDepart l'heure de départ du trajet (facultative, au format ISO: HH:mm:ss)
     * @param vehiculeType le type de véhicule souhaité (par défaut : TOUS)
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
    ResponseEntity<Page<TripResponseDto>> searchTrips(
            @Parameter(description = "Ville de départ")
            @RequestParam String villeDepart,
            @Parameter(description = "Ville d'arrivée")
            @RequestParam String villeArrivee,
            @Parameter(description = "Date de début (format: yyyy-MM-dd)")
            @RequestParam String dateDebutStr,
            @Parameter(description = "Heure de départ (format: HH:mm:ss)")
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime heureDepart,
            @Parameter(description = "Type de véhicule (ex: SERVICE, PERSONNEL, TOUS)")
            @RequestParam(defaultValue = "TOUS") VehiculeType vehiculeType,
            @Parameter(description = "Numéro de la page à récupérer. La numérotation des pages commence à zéro.")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Nombre d'éléments par page.")
            @RequestParam(defaultValue = "5") int size
    );

    /**
     * Récupère la liste des trajets à venir pour un utilisateur donné.
     * L'utilisateur peut être organisateur ou simple passager.
     *
     * @param userId l'identifiant de l'utilisateur
     * @return liste des trajets futurs triés par date croissante
     * @throws ResourceNotFoundException si l'utilisateur n'existe pas
     */
    @Operation(summary = "Récupérer les trajets à venir d'un utilisateur",
            description = "Retourne la liste des trajets futurs auxquels un utilisateur participe ou qu'il organise.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des trajets récupérée avec succès"),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    @GetMapping("/upcoming/{userId}")
    ResponseEntity<List<TripResponseDto>> getUpcomingTrip(
            @Parameter(description = "ID de l'utilisateur", required = true) @PathVariable Integer userId
    ) throws ResourceNotFoundException;

    /**
     * Récupère la liste des trajets passés pour un utilisateur donné.
     * L'utilisateur peut être organisateur ou simple passager (abonné).
     *
     * @param userId l'identifiant de l'utilisateur
     * @return liste des trajets passés triés par date décroissante
     * @throws ResourceNotFoundException si l'utilisateur n'existe pas
     */
    @Operation(summary = "Récupérer les trajets passés d'un utilisateur",
            description = "Retourne la liste des trajets déjà effectués par un utilisateur, qu'il soit organisateur ou passager.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des trajets récupérée avec succès"),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    @GetMapping("/past/{userId}")
    ResponseEntity<List<TripResponseDto>> getPastTrip(
            @Parameter(description = "ID de l'utilisateur", required = true) @PathVariable Integer userId
    ) throws ResourceNotFoundException;

    /**
     * Met à jour un trajet existant avec de nouvelles données.
     * Les entités User et Car sont automatiquement récupérées par le service
     * si les identifiants sont modifiés dans le TripRequestDto.
     *
     * @param id l'identifiant du trajet à modifier
     * @param tripRequestDto les nouvelles données du trajet
     * @return le trajet mis à jour
     * @throws FunctionnalException si le trajet n'existe pas ou en cas d'erreur métier
     */
    @Operation(summary = "Mettre à jour un trajet existant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trajet mis à jour"),
            @ApiResponse(responseCode = "404", description = "Trajet non trouvé")
    })
    @PutMapping("/update/{id}")
    ResponseEntity<TripResponseDto> updateTrip(
            @Parameter(description = "ID du trajet", required = true) @PathVariable Integer id,
            @Valid @RequestBody TripRequestDto tripRequestDto
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