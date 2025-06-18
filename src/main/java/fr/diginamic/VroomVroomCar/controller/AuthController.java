package fr.diginamic.VroomVroomCar.controller;

import fr.diginamic.VroomVroomCar.dto.request.AuthLoginRequestDto;
import fr.diginamic.VroomVroomCar.dto.request.UserRequestDto;
import fr.diginamic.VroomVroomCar.entity.User;
import fr.diginamic.VroomVroomCar.exception.FunctionnalException;
import fr.diginamic.VroomVroomCar.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthLoginRequestDto user) throws Exception {
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, authService.logUser(user).toString())
                .body("vous êtes connecté");
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse http) throws Exception {
        authService.logoutUser(http);
        return ResponseEntity.ok().body("vous êtes déconnecté");
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRequestDto userRequestDto) throws FunctionnalException {
        authService.register(userRequestDto);
        return ResponseEntity.ok("Utilisateur ajouté");
    }
}