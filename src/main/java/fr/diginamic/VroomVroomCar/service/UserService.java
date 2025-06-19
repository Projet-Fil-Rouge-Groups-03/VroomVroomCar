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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements IUserService {
    @Autowired
    private BCryptPasswordEncoder bcrypt;
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public UserResponseDto getUserById(Integer id) throws ResourceNotFoundException {
        ValidationUtil.validateIdNotNull(id, "l'utilisateur'");
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'ID: " + id));

        return userMapper.toResponseDto(user);
    }

    @Transactional(readOnly = true)
    @Override
    public UserResponseDto getByNom(String nom) throws ResourceNotFoundException {
        ValidationUtil.validateNotNull(nom);
        User user = userRepository.findByNom(nom)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec le nom: " + nom));
        return userMapper.toResponseDto(user);
    }

    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) throws ResourceNotFoundException {
        ValidationUtil.validateUserRequestDto(userRequestDto);

        User user = userMapper.toEntity(userRequestDto, bcrypt.encode(userRequestDto.getMotDePasse()), Status.ROLE_ACTIF);
        return userMapper.toResponseDto(userRepository.save(user));
    }

    @Override
    public UserResponseDto updateUser(Integer id, UserRequestDto userRequestDto) throws ResourceNotFoundException{
        ValidationUtil.validateIdNotNull(id, "l'utilisateur'");
        ValidationUtil.validateUserRequestDto(userRequestDto);

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvée avec l'ID: " + id));

        userMapper.updateEntity(existingUser, userRequestDto);
        return userMapper.toResponseDto(userRepository.save(existingUser));
    }

    @Override
    public UserResponseDto updateUser(String nom, UserRequestDto userRequestDto) throws ResourceNotFoundException{
        ValidationUtil.validateNotNull(nom);
        ValidationUtil.validateUserRequestDto(userRequestDto);

        User existingUser = userRepository.findByNom(nom)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvée avec le nom: " + nom));

        userMapper.updateEntity(existingUser, userRequestDto);
        return userMapper.toResponseDto(userRepository.save(existingUser));
    }

    @Override
    public void deleteUser(Integer id) throws ResourceNotFoundException {
        ValidationUtil.validateIdNotNull(id, "l'utilisateur");
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Utilisateur non trouvée avec l'ID: " + id);
        }
        userRepository.deleteById(id);
    }

}
