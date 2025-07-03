package fr.diginamic.VroomVroomCar.service;

import fr.diginamic.VroomVroomCar.dto.request.AuthLoginRequestDto;
import fr.diginamic.VroomVroomCar.dto.request.UserRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.LoginResponseDto;
import fr.diginamic.VroomVroomCar.dto.response.UserResponseDto;
import fr.diginamic.VroomVroomCar.entity.Status;
import fr.diginamic.VroomVroomCar.entity.User;
import fr.diginamic.VroomVroomCar.exception.AuthenticationException;
import fr.diginamic.VroomVroomCar.exception.FunctionnalException;
import fr.diginamic.VroomVroomCar.mapper.UserMapper;
import fr.diginamic.VroomVroomCar.repository.UserRepository;
import fr.diginamic.VroomVroomCar.util.ValidationUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService implements IAuthService {
    @Autowired
    private BCryptPasswordEncoder bcrypt;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtAuthentificationService jwtAuthentificationService;

    @Override
    public LoginResponseDto logUser(AuthLoginRequestDto loginRequest) throws AuthenticationException {
        User user = userRepository.findByMail(loginRequest.getMail())
                .filter(u -> bcrypt.matches(loginRequest.getMotDePasse(), u.getMotDePasse()))
                .orElseThrow(() -> new AuthenticationException("Identifiants invalides"));

        String role = user.getStatus().name();
        ResponseCookie jwtCookie = jwtAuthentificationService.generateToken(user.getMail(), role);

        UserResponseDto userDto = userMapper.toResponseDto(user);

        return new LoginResponseDto(jwtCookie, userDto);
    }

    @Override
    public void logoutUser(HttpServletResponse http) throws Exception {
        try {
            jwtAuthentificationService.invalidateToken(http);
        } catch (Exception e){
            throw new Exception("erreur lors de la déconnexion :" + e.toString());
        }
    }

    @Override
    public void register(UserRequestDto userRequestDto) throws FunctionnalException {
        ValidationUtil.validateUserMail(userRequestDto.getMail());
        ValidationUtil.validateUserPassword(userRequestDto.getMotDePasse());
        if(userRepository.findByMail(userRequestDto.getMail()).isPresent()) throw new FunctionnalException("Cet utilisateur existe déjà");
        User user = userMapper.toEntity(userRequestDto, bcrypt.encode(userRequestDto.getMotDePasse()),Status.ROLE_ACTIF);
        userRepository.save(user);
    }

    @Override
    public UserResponseDto getCurrentAuthenticatedUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        String userEmail = authentication.getName();
        User user = userRepository.findByMail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec l'email : " + userEmail));

        return userMapper.toResponseDto(user);
    }
}
