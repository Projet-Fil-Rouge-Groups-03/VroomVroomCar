package fr.diginamic.VroomVroomCar.controller;

import fr.diginamic.VroomVroomCar.dto.request.TripRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.TripResponseDto;
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
 * Interface pour le contrôleur de gestion des trajets.
 * Définit les opérations REST pour la gestion des trajets.
 */

@Tag(name = "Trips", description = "API pour la gestion des trajets")
@RequestMapping("/api/trips")
public interface ITripController {

    /**
     * Crée un nouveau trajet.
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
     * Récupère tous les trajets
     */
    @Operation(summary = "Récupérer tous les trajets")
    @ApiResponse(responseCode = "200", description = "Liste des trajets")
    @GetMapping
    ResponseEntity<List<TripResponseDto>> getAllTrips();

    /**
     * Récupère un trajet selon son indentifiant
     */
    @Operation(summary = "Récupérer un trajet par son identifiant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trajet trouvé"),
            @ApiResponse(responseCode = "404", description = "Trajet non trouvé")
    })
    @GetMapping("/{id}")
    ResponseEntity<TripResponseDto> getTripById(
            @Parameter(description = "ID du trajet") @PathVariable Integer id
    ) throws FunctionnalException;

    /**
     * Modifier un trajet
     */
    @Operation(summary = "Mettre à jour un trajet existant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trajet mis à jour"),
            @ApiResponse(responseCode = "404", description = "Trajet non trouvé")
    })
    @PutMapping("/update/{id}")
    ResponseEntity<TripResponseDto> updateTrip(
            @Parameter(description = "ID du trajet") @PathVariable Integer id,
            @Valid @RequestBody TripRequestDto tripRequestDto
    ) throws FunctionnalException;

    /**
     * Supprimer un trajet
     */
    @Operation(summary = "Supprimer un trajet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trajet supprimé"),
            @ApiResponse(responseCode = "404", description = "Trajet non trouvé")
    })
    @DeleteMapping("/delete/{id}")
    ResponseEntity<String> deleteTrip(
            @Parameter(description = "ID du trajet") @PathVariable Integer id
    ) throws FunctionnalException;
}