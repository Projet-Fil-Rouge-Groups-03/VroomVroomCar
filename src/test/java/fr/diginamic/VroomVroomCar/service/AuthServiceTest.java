package fr.diginamic.VroomVroomCar.service;

import fr.diginamic.VroomVroomCar.dto.request.AuthLoginRequestDto;
import fr.diginamic.VroomVroomCar.dto.request.UserRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.LoginResponseDto;
import fr.diginamic.VroomVroomCar.dto.response.UserResponseDto;
import fr.diginamic.VroomVroomCar.entity.Status;
import fr.diginamic.VroomVroomCar.entity.User;
import fr.diginamic.VroomVroomCar.exception.FunctionnalException;
import fr.diginamic.VroomVroomCar.mapper.UserMapper;
import fr.diginamic.VroomVroomCar.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private UserMapper userMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    BCryptPasswordEncoder bcrypt;
    @Mock
    JwtAuthentificationService jwtAuthentificationService;
    @InjectMocks
    private AuthService authService;

    @Test
    void logUser_shouldReturnLoginResultDto_whenCredentialsAreValid() throws Exception {
        AuthLoginRequestDto loginRequest = new AuthLoginRequestDto();
        loginRequest.setMail("test@example.com");
        loginRequest.setMotDePasse("plainPassword123*");

        User userInDb = new User();
        userInDb.setId(1);
        userInDb.setMail("test@example.com");
        userInDb.setNom("Test");
        userInDb.setPrenom("User");
        userInDb.setMotDePasse("hashedPassword123*");
        userInDb.setStatus(Status.ROLE_ACTIF);

        UserResponseDto userDto = new UserResponseDto();
        userDto.setId(1);
        userDto.setMail("test@example.com");
        userDto.setNom("Test");
        userDto.setPrenom("User");
        userDto.setStatus("ROLE_ACTIF");

        ResponseCookie fakeCookie = ResponseCookie.from("jwt-token", "fake-token-value").build();

        when(userRepository.findByMail("test@example.com")).thenReturn(Optional.of(userInDb));
        when(bcrypt.matches("plainPassword123*", "hashedPassword123*")).thenReturn(true);
        when(jwtAuthentificationService.generateToken("test@example.com", "ROLE_ACTIF")).thenReturn(fakeCookie);
        when(userMapper.toResponseDto(userInDb)).thenReturn(userDto);

        LoginResponseDto result = authService.logUser(loginRequest);

        assertNotNull(result);

        assertNotNull(result.getUserDto());
        assertEquals(1, result.getUserDto().getId());
        assertEquals("Test", result.getUserDto().getNom());
        assertEquals("ROLE_ACTIF", result.getUserDto().getStatus());

        assertNotNull(result.getCookie());
        assertEquals("jwt-token", result.getCookie().getName());
        assertEquals("fake-token-value", result.getCookie().getValue());

        verify(userRepository).findByMail("test@example.com");
        verify(bcrypt).matches("plainPassword123*", "hashedPassword123*");
        verify(jwtAuthentificationService).generateToken("test@example.com", "ROLE_ACTIF");
        verify(userMapper).toResponseDto(userInDb);
    }

    @Test
    void logoutUser() throws Exception {
        HttpServletResponse mockRequest = mock(HttpServletResponse.class);

        doNothing().when(jwtAuthentificationService).invalidateToken( mockRequest);

        authService.logoutUser(mockRequest);

        verify(jwtAuthentificationService, times(1)).invalidateToken(mockRequest);
    }

    @Test
    void register() throws FunctionnalException {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setMail("email@test.fr");
        userRequestDto.setNom("NomTest");
        userRequestDto.setPrenom("PrenomTest");
        userRequestDto.setLibelle("127 rue d'ici");
        userRequestDto.setCodePostal("55372");
        userRequestDto.setVille("Nulle-Part");
        userRequestDto.setMotDePasse("Pz276-V7aab?=f");

        User user = new User();

        when(userRepository.findByMail(anyString())).thenReturn(Optional.empty());
        when(userMapper.toEntity(any(UserRequestDto.class), any(), any(Status.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);

        authService.register(userRequestDto);

        verify(userRepository, times(1)).findByMail(anyString());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void getCurrentAuthenticatedUser_shouldReturnUserDto_whenAuthenticationIsValid() {
        Authentication mockAuthentication = mock(Authentication.class);
        when(mockAuthentication.getName()).thenReturn("test@example.com");
        when(mockAuthentication.isAuthenticated()).thenReturn(true);

        User userInDb = new User();
        userInDb.setId(1);
        userInDb.setMail("test@example.com");
        userInDb.setNom("Test");
        userInDb.setStatus(Status.ROLE_ACTIF);

        UserResponseDto expectedDto = new UserResponseDto(/* ... initialisation ... */);
        expectedDto.setId(1);
        expectedDto.setMail("test@example.com");
        expectedDto.setNom("Test");
        expectedDto.setStatus("ACTIF");

        when(userRepository.findByMail("test@example.com")).thenReturn(Optional.of(userInDb));
        when(userMapper.toResponseDto(userInDb)).thenReturn(expectedDto);

        UserResponseDto actualDto = authService.getCurrentAuthenticatedUser(mockAuthentication);

        assertNotNull(actualDto);
        assertEquals(expectedDto.getId(), actualDto.getId());
        assertEquals(expectedDto.getNom(), actualDto.getNom());
        assertEquals(expectedDto.getStatus(), actualDto.getStatus());

        verify(userRepository).findByMail("test@example.com");
        verify(userMapper).toResponseDto(userInDb);
    }

    @Test
    void getCurrentAuthenticatedUser_shouldReturnNull_whenAuthenticationIsNull() {
        UserResponseDto result = authService.getCurrentAuthenticatedUser(null);
        assertNull(result);
    }

    @Test
    void getCurrentAuthenticatedUser_shouldThrowException_whenUserNotFoundInDb() {
        Authentication mockAuthentication = mock(Authentication.class);
        when(mockAuthentication.getName()).thenReturn("ghost@example.com");
        when(mockAuthentication.isAuthenticated()).thenReturn(true);

        when(userRepository.findByMail("ghost@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            authService.getCurrentAuthenticatedUser(mockAuthentication);
        });
        verify(userMapper, never()).toResponseDto(any(User.class));
    }
}
