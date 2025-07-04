package fr.diginamic.VroomVroomCar.service;

import fr.diginamic.VroomVroomCar.dto.request.AuthLoginRequestDto;
import fr.diginamic.VroomVroomCar.dto.request.UserRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.LoginResponseDto;
import fr.diginamic.VroomVroomCar.dto.response.UserResponseDto;
import fr.diginamic.VroomVroomCar.entity.User;
import fr.diginamic.VroomVroomCar.exception.AuthenticationException;
import fr.diginamic.VroomVroomCar.exception.FunctionnalException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface IAuthService {
    LoginResponseDto logUser(AuthLoginRequestDto loginRequest) throws AuthenticationException;

    void logoutUser(HttpServletResponse http) throws Exception;

    User register(UserRequestDto userRequestDto) throws FunctionnalException;

    /**
     * Récupère les informations de l'utilisateur actuellement authentifié.
     * C'est la logique pour l'endpoint /me.
     * @param authentication L'objet d'authentification fourni par Spring Security.
     * @return Le DTO de l'utilisateur.
     * @throws UsernameNotFoundException si l'utilisateur du token n'est pas trouvé en base.
     */
    UserResponseDto getCurrentAuthenticatedUser(Authentication authentication);
}
