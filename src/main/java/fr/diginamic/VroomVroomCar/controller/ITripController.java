package fr.diginamic.VroomVroomCar.controller;

import fr.diginamic.VroomVroomCar.dto.request.TripRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.CarResponseDto;
import fr.diginamic.VroomVroomCar.dto.response.TripResponseDto;
import fr.diginamic.VroomVroomCar.dto.response.UserResponseDto;
import fr.diginamic.VroomVroomCar.exception.FunctionnalException;
import fr.diginamic.VroomVroomCar.exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
