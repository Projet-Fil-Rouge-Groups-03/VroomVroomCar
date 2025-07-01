package fr.diginamic.VroomVroomCar.controller;

import fr.diginamic.VroomVroomCar.dto.request.UserRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.UserResponseDto;
import fr.diginamic.VroomVroomCar.exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Interface pour le contrôleur de gestion des utilisateurs.
 * Définit les opérations REST pour la gestion des utilisateurs.
 */
@Tag(name = "Utilisateur", description = "API pour la gestion des utilisateurs")
public interface IUserController {
    /**
     * Récupère la liste de tous les utilisateurs.
     *
     * @return une {@link ResponseEntity} contenant une liste d'objets {@link UserResponseDto}.
     */
    @Operation(summary = "Récupérer tous les utilisateurs", description = "Renvoie une liste de tous les utilisateurs enregistrés.")
    public ResponseEntity<List<UserResponseDto>> findAll();

    /**
     * Recherche un utilisateur par son identifiant.
     *
     * @param id l'identifiant de l'utilisateur à rechercher.
     * @return une {@link ResponseEntity} contenant un objet {@link UserResponseDto}.
     * @throws ResourceNotFoundException si aucun utilisateur avec ce nom n'est trouvé.
     */
    @Operation(summary = "Rechercher un utilisateur par id", description = "Renvoie les informations d'un utilisateur à partir de son id.")
    public ResponseEntity<UserResponseDto> findById(@PathVariable Integer id) throws ResourceNotFoundException;
    /**
     * Recherche un utilisateur par son nom.
     *
     * @param nom le nom de l'utilisateur à rechercher.
     * @return une ResponseEntity contenant un objet UserResponseDto.
     * @throws ResourceNotFoundException si aucun utilisateur avec ce nom n'est trouvé.
     */
    @Operation(summary = "Rechercher un utilisateur par nom", description = "Renvoie les informations d'un utilisateur à partir de son nom.")
    public ResponseEntity<UserResponseDto> findByNom(@RequestParam String nom) throws ResourceNotFoundException;

    /**
     * Recherche les utilisateurs dont le nom contient une chaîne donnée (sans tenir compte de la casse),
     * avec un nombre maximum de résultats défini par size.
     *
     * @param nom  la chaîne à rechercher dans les noms d'utilisateurs (obligatoire)
     * @param size le nombre maximum de résultats à retourner (valeur par défaut : 5)
     * @return une ResponseEntity contenant la liste des UserResponseDto correspondant aux critères
     */
    @Operation(summary = "Filtrer une liste d'utilisateurs par le nom", description = "Renvoie une liste d'utilisateurs correspondant au nom.")
    ResponseEntity<List<UserResponseDto>> searchUserByNom(@RequestParam String nom, @RequestParam(defaultValue = "5") int size);

    /**
     * Ajoute un nouvel utilisateur.
     *
     * @param user les données du nouvel utilisateur à ajouter, encapsulées dans un {@link UserRequestDto}.
     * @return une {@link ResponseEntity} contenant l'objet {@link UserResponseDto} créé.
     * @throws ResourceNotFoundException si une ressource nécessaire à la création de l'utilisateur est manquante.
     */
    @Operation(summary = "Ajouter un nouvel utilisateur", description = "Crée un nouvel utilisateur avec les données fournies.")
    public ResponseEntity<UserResponseDto> addUser(
            @Parameter(description = "Données du nouvel utilisateur") @RequestBody UserRequestDto user
    ) throws ResourceNotFoundException;
    /**
     * Modifie un utilisateur existant en se basant sur son identifiant.
     *
     * @param id l'identifiant de l'utilisateur à modifier.
     * @param user les nouvelles données de l'utilisateur, encapsulées dans un {@link UserRequestDto}.
     * @return une {@link ResponseEntity} contenant l'objet {@link UserResponseDto} mis à jour.
     * @throws ResourceNotFoundException si aucun utilisateur avec cet identifiant n'est trouvé.
     */
    @Operation(summary = "Modifier un utilisateur par ID", description = "Met à jour un utilisateur existant à partir de son identifiant.")
    ResponseEntity<UserResponseDto> editUser(
            @Parameter(description = "ID de l'utilisateur à modifier") @PathVariable Integer id,
            @Parameter(description = "Nouvelles données de l'utilisateur") @RequestBody UserRequestDto user
    ) throws ResourceNotFoundException;
    /**
     * Modifie un utilisateur existant en se basant sur son nom.
     *
     * @param nom le nom de l'utilisateur à modifier.
     * @param user les nouvelles données de l'utilisateur, encapsulées dans un {@link UserRequestDto}.
     * @return une {@link ResponseEntity} contenant l'objet {@link UserResponseDto} mis à jour.
     * @throws ResourceNotFoundException si aucun utilisateur avec ce nom n'est trouvé.
     */
    @Operation(summary = "Modifier un utilisateur par nom", description = "Met à jour un utilisateur existant à partir de son nom.")
    ResponseEntity<UserResponseDto> editUser(
            @Parameter(description = "Nom de l'utilisateur à modifier") @RequestParam String nom,
            @Parameter(description = "Nouvelles données de l'utilisateur") @RequestBody UserRequestDto user
    ) throws ResourceNotFoundException;

    /**
     * Supprime un utilisateur existant à partir de son identifiant.
     *
     * @param id l'identifiant de l'utilisateur à supprimer.
     * @return une {@link ResponseEntity} contenant un message de confirmation.
     * @throws ResourceNotFoundException si aucun utilisateur avec cet identifiant n'est trouvé.
     */
    @Operation(summary = "Supprimer un utilisateur", description = "Supprime un utilisateur existant à partir de son identifiant.")
    ResponseEntity<String> deleteUser(
            @Parameter(description = "ID de l'utilisateur à supprimer") @PathVariable Integer id
    ) throws ResourceNotFoundException;


}
