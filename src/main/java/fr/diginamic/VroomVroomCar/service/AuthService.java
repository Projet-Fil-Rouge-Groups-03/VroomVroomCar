package fr.diginamic.VroomVroomCar.service;

import fr.diginamic.VroomVroomCar.entity.User;
import fr.diginamic.VroomVroomCar.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

public class AuthService implements IAuthService {
    @Autowired
    private UserRepository userAppRepository;
    @Autowired
    private JwtAuthentificationService jwtAuthentificationService;
    @Autowired
    private BCryptPasswordEncoder bcrypt;
    public ResponseCookie logUser(User user) throws Exception {
        Optional<User> userOptional = userAppRepository.findByNom(user.getNom());
        if(userOptional.isPresent() && bcrypt.matches( user.getMotDePasse(),userOptional.get().getMotDePasse()) ){
            return jwtAuthentificationService.generateToken(user.getNom());
        }
        throw new Exception();
    }
}
