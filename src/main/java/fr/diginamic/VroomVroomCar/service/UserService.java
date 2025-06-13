package fr.diginamic.VroomVroomCar.service;

import fr.diginamic.VroomVroomCar.dto.request.UserRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.UserResponseDto;
import fr.diginamic.VroomVroomCar.entity.Status;
import fr.diginamic.VroomVroomCar.entity.User;
import fr.diginamic.VroomVroomCar.exception.ResourceNotFoundException;
import fr.diginamic.VroomVroomCar.mapper.UserMapper;
import fr.diginamic.VroomVroomCar.repository.UserRepository;
import fr.diginamic.VroomVroomCar.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service de gestion des utilisateurs.
 * Implémente les opérations de création, récupération, mise à jour et suppression d'utilisateurs.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements IUserService {

    /**
     * Mapper pour convertir entre les entités {@link User} et les DTOs {@link UserRequestDto} / {@link UserResponseDto}.
     */
    @Autowired
    UserMapper userMapper;

    /**
     * Référentiel pour accéder aux données utilisateur dans la base.
     */
    @Autowired
    UserRepository userRepository;

    /**
     * Récupère la liste de tous les utilisateurs.
     *
     * @return liste des utilisateurs sous forme de {@link UserResponseDto}.
     */
    @Transactional(readOnly = true)
    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Récupère un utilisateur par son identifiant.
     *
     * @param id identifiant de l'utilisateur.
     * @return l'utilisateur correspondant sous forme de {@link UserResponseDto}.
     * @throws ResourceNotFoundException si aucun utilisateur n'est trouvé.
     */
    @Transactional(readOnly = true)
    @Override
    public UserResponseDto getUserById(Integer id) throws ResourceNotFoundException {
        ValidationUtil.validateIdNotNull(id, "l'utilisateur'");
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'ID: " + id));

        return userMapper.toResponseDto(user);
    }

    /**
     * Récupère un utilisateur par son nom.
     *
     * @param nom nom de l'utilisateur.
     * @return l'utilisateur correspondant sous forme de {@link UserResponseDto}.
     * @throws ResourceNotFoundException si aucun utilisateur n'est trouvé.
     */
    @Transactional(readOnly = true)
    @Override
    public UserResponseDto getByNom(String nom) throws ResourceNotFoundException {
        ValidationUtil.validateNotNull(nom);
        User user = userRepository.findByNom(nom)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec le nom: " + nom));
        return userMapper.toResponseDto(user);
    }

    /**
     * Crée un nouvel utilisateur à partir des données fournies.
     *
     * @param userRequestDto les données de l'utilisateur à créer.
     * @return l'utilisateur créé sous forme de {@link UserResponseDto}.
     * @throws ResourceNotFoundException si des validations échouent.
     */
    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) throws ResourceNotFoundException {
        ValidationUtil.validateUserRequestDto(userRequestDto);

        User user = userMapper.toEntity(userRequestDto, Status.ACTIF);
        return userMapper.toResponseDto(userRepository.save(user));
    }

    /**
     * Met à jour un utilisateur existant à partir de son identifiant.
     *
     * @param id identifiant de l'utilisateur à mettre à jour.
     * @param userRequestDto les nouvelles données de l'utilisateur.
     * @return l'utilisateur mis à jour sous forme de {@link UserResponseDto}.
     * @throws ResourceNotFoundException si l'utilisateur n'existe pas.
     */
    @Override
    public UserResponseDto updateUser(Integer id, UserRequestDto userRequestDto) throws ResourceNotFoundException{
        ValidationUtil.validateIdNotNull(id, "l'utilisateur'");
        ValidationUtil.validateUserRequestDto(userRequestDto);

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvée avec l'ID: " + id));

        userMapper.updateEntity(existingUser, userRequestDto);
        return userMapper.toResponseDto(userRepository.save(existingUser));
    }

    /**
     * Met à jour un utilisateur existant à partir de son nom.
     *
     * @param nom nom de l'utilisateur à mettre à jour.
     * @param userRequestDto les nouvelles données de l'utilisateur.
     * @return l'utilisateur mis à jour sous forme de {@link UserResponseDto}.
     * @throws ResourceNotFoundException si l'utilisateur n'existe pas.
     */
    @Override
    public UserResponseDto updateUser(String nom, UserRequestDto userRequestDto) throws ResourceNotFoundException{
        ValidationUtil.validateNotNull(nom);
        ValidationUtil.validateUserRequestDto(userRequestDto);

        User existingUser = userRepository.findByNom(nom)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvée avec l'ID: " + nom));

        userMapper.updateEntity(existingUser, userRequestDto);
        return userMapper.toResponseDto(userRepository.save(existingUser));
    }

    /**
     * Supprime un utilisateur à partir de son identifiant.
     *
     * @param id identifiant de l'utilisateur à supprimer.
     * @throws ResourceNotFoundException si l'utilisateur n'existe pas.
     */
    @Override
    public void deleteUser(Integer id) throws ResourceNotFoundException {
        ValidationUtil.validateIdNotNull(id, "l'utilisateur");
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Utilisateur non trouvée avec l'ID: " + id);
        }
        userRepository.deleteById(id);
    }

}
