package fr.diginamic.VroomVroomCar.controller;

import fr.diginamic.VroomVroomCar.entity.User;
import fr.diginamic.VroomVroomCar.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) throws Exception {
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, authService.logUser(user).toString())
                .body("vous êtes login");
    }
}