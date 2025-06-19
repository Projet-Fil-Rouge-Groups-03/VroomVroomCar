package fr.diginamic.VroomVroomCar.service;



import fr.diginamic.VroomVroomCar.dto.request.UserRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.UserResponseDto;
import fr.diginamic.VroomVroomCar.entity.Status;
import fr.diginamic.VroomVroomCar.entity.User;
import fr.diginamic.VroomVroomCar.exception.ResourceNotFoundException;
import fr.diginamic.VroomVroomCar.mapper.UserMapper;
import fr.diginamic.VroomVroomCar.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    BCryptPasswordEncoder bcrypt;
    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {}

    @Test
    void getAllUsers() {
        User user = new User();
        UserResponseDto userResponseDto = new UserResponseDto();

        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));
        when(userMapper.toResponseDto(any(User.class))).thenReturn(userResponseDto);

        List<UserResponseDto> result = userService.getAllUsers();

        assertFalse(result.isEmpty());
        verify(userRepository, times(1)).findAll();
    }


    @Test
    void getUserById() throws ResourceNotFoundException {
        User user = new User();
        UserResponseDto userResponseDto = new UserResponseDto();

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(userMapper.toResponseDto(any(User.class))).thenReturn(userResponseDto);

        UserResponseDto result = userService.getUserById(1);

        assertNotNull(result);
        verify(userRepository, times(1)).findById(anyInt());
    }

    @Test
    void getByNom() throws ResourceNotFoundException {
        User user = new User();
        UserResponseDto userResponseDto = new UserResponseDto();

        when(userRepository.findByNom(anyString())).thenReturn(Optional.of(user));
        when(userMapper.toResponseDto(any(User.class))).thenReturn(userResponseDto);

        UserResponseDto result = userService.getByNom("admin");

        assertNotNull(result);
        verify(userRepository, times(1)).findByNom(anyString());
    }

    @Test
    void createUser() throws ResourceNotFoundException {
        UserRequestDto userRequestDto = new UserRequestDto();
        User user = new User();
        UserResponseDto userResponseDto = new UserResponseDto();

        when(userMapper.toEntity(any(UserRequestDto.class), any(), any(Status.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toResponseDto(any(User.class))).thenReturn(userResponseDto);

        UserResponseDto result = userService.createUser(userRequestDto);

        assertNotNull(result);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUser() throws ResourceNotFoundException {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setMail("email@test.fr");
        userRequestDto.setNom("NomTest");
        userRequestDto.setPrenom("PrenomTest");
        userRequestDto.setLibelle("127 rue d'ici");
        userRequestDto.setCodePostal("55372");
        userRequestDto.setVille("Nulle-Part");

        User user = new User();
        user.setId(1);

        User existingUser = new User();
        existingUser.setId(1);
        existingUser.setNom("AncienNom");
        existingUser.setPrenom("AncienPrenom");
        existingUser.setMail("ancien@mail.com");
        existingUser.setAdresse("127 rue d'avant, Ancienne-Ville");

        UserResponseDto userResponseDto = new UserResponseDto();

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);
        when(userMapper.toResponseDto(any(User.class))).thenReturn(userResponseDto);

        UserResponseDto result = userService.updateUser(1, userRequestDto);

        assertNotNull(result);
        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUser() throws ResourceNotFoundException {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setMail("email@test.fr");
        userRequestDto.setNom("NomTest");
        userRequestDto.setPrenom("PrenomTest");
        userRequestDto.setLibelle("127 rue d'ici");
        userRequestDto.setCodePostal("55372");
        userRequestDto.setVille("Nulle-Part");

        User existingUser = new User();
        existingUser.setNom("AncienNom");
        existingUser.setPrenom("AncienPrenom");
        existingUser.setMail("ancien@mail.com");
        existingUser.setAdresse("127 rue d'avant, Ancienne-Ville");

        UserResponseDto userResponseDto = new UserResponseDto();

        when(userRepository.findByNom(anyString())).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);
        when(userMapper.toResponseDto(any(User.class))).thenReturn(userResponseDto);

        UserResponseDto result = userService.updateUser("AncienNom", userRequestDto);

        assertNotNull(result);
        verify(userRepository, times(1)).findByNom(anyString());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void deleteUser() throws ResourceNotFoundException {
        doNothing().when(userRepository).deleteById(anyInt());
        when(userRepository.existsById(anyInt())).thenReturn(true);

        userService.deleteUser(1);

        verify(userRepository, times(1)).deleteById(anyInt());
    }
}
