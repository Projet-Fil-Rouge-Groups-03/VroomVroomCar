package fr.diginamic.VroomVroomCar.controller;

import fr.diginamic.VroomVroomCar.dto.request.TripRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.TripResponseDto;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "Créer un nouveau trajet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Trajet créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "404", description = "Ressource liée non trouvée")
    })
    @PostMapping("/create")
    ResponseEntity<TripResponseDto> createTrip(
            @Parameter(description = "Données du trajet à créer", required = true)
            @Valid @RequestBody TripRequestDto tripRequestDto
    ) throws ResourceNotFoundException, FunctionnalException;

    @Operation(summary = "Récupérer tous les trajets")
    @ApiResponse(responseCode = "200", description = "Liste des trajets")
    @GetMapping
    ResponseEntity<Page<TripResponseDto>> getAllTrips(
            @Parameter(description = "Numéro de la page", required = true) @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Taille de la page", required = true) @RequestParam(defaultValue = "10") int size
    );

    @Operation(summary = "Récupérer un trajet par son identifiant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trajet trouvé"),
            @ApiResponse(responseCode = "404", description = "Trajet non trouvé")
    })
    @GetMapping("/{id}")
    ResponseEntity<TripResponseDto> getTripById(
            @Parameter(description = "ID du trajet", required = true) @PathVariable Integer id
    ) throws FunctionnalException;

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
            @Parameter(description = "Ville de départ") @RequestParam(required = false) String villeDepart,
            @Parameter(description = "Ville d'arrivée") @RequestParam(required = false) String villeArrivee,
            @Parameter(description = "Date de début (format: yyyy-MM-dd)") @RequestParam(required = false) String dateDebutStr,
            @Parameter(description = "Heure de départ (format: HH:mm:ss)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime heureDepart,
            @Parameter(description = "Type de véhicule (ex: SERVICE, PERSONNEL, TOUS)") @RequestParam(defaultValue = "TOUS") VehiculeType vehiculeType,
            @Parameter(description = "Numéro de la page") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Taille de la page") @RequestParam(defaultValue = "5") int size
    );

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

    @Operation(summary = "Mettre à jour un trajet existant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trajet mis à jour"),
            @ApiResponse(responseCode = "404", description = "Trajet non trouvé")
    })
    @PutMapping("/update/{id}")
    ResponseEntity<TripResponseDto> updateTrip(
            @Parameter(description = "ID du trajet", required = true) @PathVariable Integer id,
            @Parameter(description = "Nouvelles données du trajet", required = true) @Valid @RequestBody TripRequestDto tripRequestDto
    ) throws FunctionnalException;

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