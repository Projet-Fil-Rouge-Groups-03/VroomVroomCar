package fr.diginamic.VroomVroomCar.service;

import fr.diginamic.VroomVroomCar.dto.request.AuthLoginRequestDto;
import fr.diginamic.VroomVroomCar.dto.request.UserRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.LoginResponseDto;
import fr.diginamic.VroomVroomCar.exception.AuthenticationException;
import fr.diginamic.VroomVroomCar.exception.FunctionnalException;
import jakarta.servlet.http.HttpServletResponse;

public interface IAuthService {
    LoginResponseDto logUser(AuthLoginRequestDto loginRequest) throws AuthenticationException;

    void logoutUser(HttpServletResponse http) throws Exception;

    void register(UserRequestDto userRequestDto) throws FunctionnalException;
}
