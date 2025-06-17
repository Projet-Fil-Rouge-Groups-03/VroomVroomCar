package fr.diginamic.VroomVroomCar.service;

import org.springframework.http.ResponseCookie;

public interface IJwtAuthentificationService {
    ResponseCookie generateToken(String nom);
}
