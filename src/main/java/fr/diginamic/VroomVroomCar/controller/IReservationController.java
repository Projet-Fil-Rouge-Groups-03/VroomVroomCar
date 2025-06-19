package fr.diginamic.VroomVroomCar.controller;

import fr.diginamic.VroomVroomCar.dto.request.ReservationRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.CarResponseDto;
import fr.diginamic.VroomVroomCar.dto.response.CompanyCarResponseDto;
import fr.diginamic.VroomVroomCar.dto.response.ReservationResponseDto;
import fr.diginamic.VroomVroomCar.dto.response.UserResponseDto;
import fr.diginamic.VroomVroomCar.exception.FunctionnalException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

/**
 * Interface pour la gestion des réservations.
 * Fournit les endpoints pour créer, lire, mettre à jour et supprimer des réservations.
 */
@Tag(name = "Reservations", description = "API pour la gestion des réservations de véhicules de service")
@RequestMapping("/api/reservations")
public interface IReservationController {

    /**
     * Crée une nouvelle réservation.
     *
     * @param requestDto       Détails de la réservation à créer
     * @param userResponseDto  Informations de l'utilisateur
     * @param carResponseDto   Informations du véhicule concerné
     * @return Réponse contenant la réservation créée
     */
    @Operation(summary = "Créer une réservation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Réservation créée avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides ou incohérentes"),
            @ApiResponse(responseCode = "409", description = "Conflit de réservation")
    })
    @PostMapping("/create")
    ResponseEntity<ReservationResponseDto> createReservation (
            @Valid @RequestBody ReservationRequestDto requestDto,
            @RequestBody UserResponseDto userResponseDto,
            @RequestBody CompanyCarResponseDto carResponseDto
    ) throws FunctionnalException;

    /**
     * Récupère toutes les réservations enregistrées.
     *
     * @return Liste des réservations
     */
    @Operation(summary = "Récupérer toutes les réservations")
    @ApiResponse(responseCode = "200", description = "Liste des réservations récupérée avec succès")
    @GetMapping
    ResponseEntity<List<ReservationResponseDto>> getAllReservations();

    /**
     * Récupère toutes les voitures non louées actuellement
     *
     * @return Liste des voitures
     */
    @GetMapping("/all-available-cars")
    ResponseEntity<List<CompanyCarResponseDto>> getAllAvailableCompanyCars();

    /**
     * Récupère une réservation à partir de son identifiant.
     *
     * @param id Identifiant de la réservation
     * @return Réservation trouvée
     */
    @Operation(summary = "Récupérer une réservation par ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Réservation trouvée"),
            @ApiResponse(responseCode = "404", description = "Réservation non trouvée")
    })
    @GetMapping("/{id}")
    ResponseEntity<ReservationResponseDto> getReservationById(
            @Parameter(description = "ID de la réservation") @PathVariable Integer id
    ) throws FunctionnalException;

    /**
     * Récupère une liste paginée des réservations associées à un véhicule spécifique.
     *
     * Cette méthode permet de consulter les réservations d’un véhicule donné, avec prise en charge
     * de la pagination via les paramètres page et size.
     *
     * @param carId l’identifiant du véhicule pour lequel les réservations sont demandées
     * @param page le numéro de la page à récupérer (0 par défaut)
     * @param size la taille de la page, c’est-à-dire le nombre de réservations par page (5 par défaut)
     * @return une réponse HTTP contenant une page de ReservationResponseDto
     */
    @Operation(summary = "Récupérer les réservations d’un véhicule avec pagination",
            description = "Permet de consulter les réservations associées à un véhicule donné, paginées par défaut par blocs de 5.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste paginée des réservations pour le véhicule"),
            @ApiResponse(responseCode = "404", description = "Aucune réservation trouvée ou véhicule inexistant")
    })
    @GetMapping("/car/{carId}")
    ResponseEntity<Page<ReservationResponseDto>> getReservationsByCarId(
            @Parameter(description = "Identifiant du véhicule", required = true)
            @PathVariable Integer carId,

            @Parameter(description = "Numéro de la page (0 par défaut)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Taille de la page (5 par défaut)", example = "5")
            @RequestParam(defaultValue = "5") int size
    ) throws FunctionnalException;


    /**
     * Met à jour une réservation existante.
     *
     * @param id               Identifiant de la réservation à mettre à jour
     * @param requestDto       Données de mise à jour
     * @param userResponseDto  Informations utilisateur
     * @param carResponseDto   Informations véhicule
     * @return Réservation mise à jour
     */
    @Operation(summary = "Mettre à jour une réservation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Réservation mise à jour avec succès"),
            @ApiResponse(responseCode = "404", description = "Réservation non trouvée")
    })
    @PutMapping("/update/{id}")
    ResponseEntity<ReservationResponseDto> updateReservation(
            @Parameter(description = "ID de la réservation") @PathVariable Integer id,
            @Valid @RequestBody ReservationRequestDto requestDto,
            @RequestBody UserResponseDto userResponseDto,
            @RequestBody CompanyCarResponseDto carResponseDto
    ) throws FunctionnalException;

    /**
     * Supprime une réservation selon son identifiant.
     *
     * @param id Identifiant de la réservation
     * @return Message de confirmation
     */
    @Operation(summary = "Supprimer une réservation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Réservation supprimée avec succès"),
            @ApiResponse(responseCode = "404", description = "Réservation non trouvée")
    })
    @DeleteMapping("/delete/{id}")
    ResponseEntity<String> deleteReservation(
            @Parameter(description = "ID de la réservation") @PathVariable Integer id
    ) throws FunctionnalException;
}

