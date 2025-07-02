package fr.diginamic.VroomVroomCar.controller;

import fr.diginamic.VroomVroomCar.dto.request.AuthLoginRequestDto;
import fr.diginamic.VroomVroomCar.dto.request.UserRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.LoginResponseDto;
import fr.diginamic.VroomVroomCar.dto.response.UserResponseDto;
import fr.diginamic.VroomVroomCar.entity.User;
import fr.diginamic.VroomVroomCar.exception.AuthenticationException;
import fr.diginamic.VroomVroomCar.exception.FunctionnalException;
import fr.diginamic.VroomVroomCar.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController implements IAuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    @Override
    public ResponseEntity<UserResponseDto> login(@RequestBody AuthLoginRequestDto loginRequest)  throws AuthenticationException {
        try {
            LoginResponseDto result = authService.logUser(loginRequest);

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, result.getCookie().toString())
                    .body(result.getUserDto());

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/logout")
    @Override
    public ResponseEntity<?> logout(HttpServletResponse http) throws Exception {
        authService.logoutUser(http);
        return ResponseEntity.ok().body("vous êtes déconnecté");
    }
    @PostMapping("/register")
    @Override
    public ResponseEntity<?> register(@RequestBody UserRequestDto userRequestDto) throws FunctionnalException {
        authService.register(userRequestDto);
        return ResponseEntity.ok("Utilisateur ajouté");
    }

    @GetMapping("/api/auth/me")
    @Override
    public ResponseEntity<UserResponseDto> getCurrentUser(Authentication authentication) {
        UserResponseDto userDto = authService.getCurrentAuthenticatedUser(authentication);

        if (userDto == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(userDto);
    }
}