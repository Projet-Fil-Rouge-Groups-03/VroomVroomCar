package fr.diginamic.VroomVroomCar.service;

import fr.diginamic.VroomVroomCar.dto.request.UserRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.UserResponseDto;
import fr.diginamic.VroomVroomCar.entity.User;
import fr.diginamic.VroomVroomCar.exception.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service de gestion des utilisateurs.
 * Implémente les opérations de création, récupération, mise à jour et suppression d'utilisateurs.
 */
public interface IUserService {

    /**
     * Récupère la liste de tous les utilisateurs.
     *
     * @return liste des utilisateurs sous forme de {@link UserResponseDto}.
     */
    @Transactional(readOnly = true)
    List<UserResponseDto> getAllUsers();

    /**
     * Récupère un utilisateur par son identifiant.
     *
     * @param id identifiant de l'utilisateur.
     * @return l'utilisateur correspondant sous forme de {@link UserResponseDto}.
     * @throws ResourceNotFoundException si aucun utilisateur n'est trouvé.
     */
    UserResponseDto getUserById(Integer id) throws ResourceNotFoundException;

    /**
     * Récupère un utilisateur par son nom.
     *
     * @param nom nom de l'utilisateur.
     * @return l'utilisateur correspondant sous forme de {@link UserResponseDto}.
     * @throws ResourceNotFoundException si aucun utilisateur n'est trouvé.
     */
    UserResponseDto getByNom(String nom) throws ResourceNotFoundException;

    @Transactional(readOnly = true)
    UserResponseDto getByEmail(String nom) throws ResourceNotFoundException;

    /**
     * Crée un nouvel utilisateur à partir des données fournies.
     *
     * @param userRequestDto les données de l'utilisateur à créer.
     * @return l'utilisateur créé sous forme de {@link UserResponseDto}.
     * @throws ResourceNotFoundException si des validations échouent.
     */
    public UserResponseDto createUser(UserRequestDto userRequestDto) throws ResourceNotFoundException;
    /**
     * Met à jour un utilisateur existant à partir de son identifiant.
     *
     * @param id identifiant de l'utilisateur à mettre à jour.
     * @param userRequestDto les nouvelles données de l'utilisateur.
     * @return l'utilisateur mis à jour sous forme de {@link UserResponseDto}.
     * @throws ResourceNotFoundException si l'utilisateur n'existe pas.
     */
    public UserResponseDto updateUser(Integer id, UserRequestDto userRequestDto) throws ResourceNotFoundException;
    /**
     * Met à jour un utilisateur existant à partir de son nom.
     *
     * @param nom nom de l'utilisateur à mettre à jour.
     * @param userRequestDto les nouvelles données de l'utilisateur.
     * @return l'utilisateur mis à jour sous forme de {@link UserResponseDto}.
     * @throws ResourceNotFoundException si l'utilisateur n'existe pas.
     */
    UserResponseDto updateUser(String nom, UserRequestDto userRequestDto) throws ResourceNotFoundException;

    /**
     * Supprime un utilisateur à partir de son identifiant.
     *
     * @param id identifiant de l'utilisateur à supprimer.
     * @throws ResourceNotFoundException si l'utilisateur n'existe pas.
     */
    public void deleteUser(Integer id) throws ResourceNotFoundException;
}
