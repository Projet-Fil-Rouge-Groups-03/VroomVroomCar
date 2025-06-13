package fr.diginamic.VroomVroomCar.service;

import fr.diginamic.VroomVroomCar.dto.request.UserRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.UserResponseDto;
import fr.diginamic.VroomVroomCar.exception.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IUserService {
    @Transactional(readOnly = true)
    List<UserResponseDto> getAllUsers();

    UserResponseDto getUserById(Integer id) throws ResourceNotFoundException;

    UserResponseDto getByNom(String nom) throws ResourceNotFoundException;

    public UserResponseDto createUser(UserRequestDto userRequestDto) throws ResourceNotFoundException;
    public UserResponseDto updateUser(Integer id, UserRequestDto userRequestDto) throws ResourceNotFoundException;

    UserResponseDto updateUser(String nom, UserRequestDto userRequestDto) throws ResourceNotFoundException;

    public void deleteUser(Integer id) throws ResourceNotFoundException;
}
