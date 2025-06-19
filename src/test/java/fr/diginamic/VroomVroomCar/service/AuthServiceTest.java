package fr.diginamic.VroomVroomCar.service;

import fr.diginamic.VroomVroomCar.dto.request.AuthLoginRequestDto;
import fr.diginamic.VroomVroomCar.dto.request.UserRequestDto;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    void logUser() throws Exception {
        AuthLoginRequestDto loginDto = new AuthLoginRequestDto();
        loginDto.setMail("test@example.com");
        loginDto.setMotDePasse("plainPassword");

        User user = new User();
        user.setMail("test@example.com");
        user.setMotDePasse("hashedPassword");
        user.setStatus(Status.ROLE_ACTIF);

        ResponseCookie fakeCookie = ResponseCookie.from("jwt", "fake-token").build();

        //Soulève une exception car l'email n'existe pas dans la base
        //when(userRepository.findByMail("test@example.com")).thenReturn(Optional.empty());
        when(userRepository.findByMail("test@example.com")).thenReturn(Optional.of(user));
        //Soulève un exception car les passwords ne correspondent pas
        //when(bcrypt.matches("plainPassword", "hashedPassword")).thenReturn(false);
        when(bcrypt.matches("plainPassword", "hashedPassword")).thenReturn(true);
        when(jwtAuthentificationService.generateToken("test@example.com", "ROLE_ACTIF")).thenReturn(fakeCookie);

        ResponseCookie result = authService.logUser(loginDto);

        assertNotNull(result);
        assertEquals("fake-token", result.getValue());
        verify(userRepository).findByMail("test@example.com");
        verify(bcrypt).matches("plainPassword", "hashedPassword");
        verify(jwtAuthentificationService).generateToken("test@example.com", "ROLE_ACTIF");
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
        userRequestDto.setAdresse("127 rue d'ici, Nulle-Part");
        userRequestDto.setMotDePasse("Pz276-V7aab?=f");

        User user = new User();

        when(userRepository.findByMail(anyString())).thenReturn(Optional.empty());
        when(userMapper.toEntity(any(UserRequestDto.class), any(), any(Status.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);

        authService.register(userRequestDto);

        verify(userRepository, times(1)).findByMail(anyString());
        verify(userRepository, times(1)).save(any(User.class));
    }
}
