package fr.diginamic.VroomVroomCar.controller;

import fr.diginamic.VroomVroomCar.dto.request.AuthLoginRequestDto;
import fr.diginamic.VroomVroomCar.dto.request.UserRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.UserResponseDto;
import fr.diginamic.VroomVroomCar.exception.AuthenticationException;
import fr.diginamic.VroomVroomCar.exception.FunctionnalException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface IAuthController {
    @PostMapping("/login")
    ResponseEntity<UserResponseDto> login(@RequestBody AuthLoginRequestDto loginRequest) throws AuthenticationException;

    @PostMapping("/logout")
    ResponseEntity<?> logout(HttpServletResponse http) throws Exception;

    @PostMapping("/register")
    ResponseEntity<?> register(@RequestBody UserRequestDto userRequestDto) throws FunctionnalException;

    /**
     * Endpoint pour récupérer les informations de l'utilisateur connecté ("moi").
     * Indispensable pour que le front-end puisse restaurer l'état de connexion.
     * L'URL est préfixée avec /api pour la cohérence.
     */
    @GetMapping("/api/auth/me")
    ResponseEntity<UserResponseDto> getCurrentUser(Authentication authentication);
}
