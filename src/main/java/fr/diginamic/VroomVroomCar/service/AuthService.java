package fr.diginamic.VroomVroomCar.service;

import fr.diginamic.VroomVroomCar.entity.User;
import fr.diginamic.VroomVroomCar.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService implements IAuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtAuthentificationService jwtAuthentificationService;
    @Autowired
    private BCryptPasswordEncoder bcrypt;
    public ResponseCookie logUser(User user) throws Exception {
        Optional<User> userOptional = userRepository.findByNom(user.getNom());
        if(userOptional.isPresent() && bcrypt.matches( user.getMotDePasse(),userOptional.get().getMotDePasse()) ){
            return jwtAuthentificationService.generateToken(user.getNom());
        }
        throw new Exception();
    }
    public ResponseCookie logoutUser() {
        return jwtAuthentificationService.invalidateToken();
    }
}
