package fr.diginamic.VroomVroomCar.service;

import fr.diginamic.VroomVroomCar.dto.request.UserRequestDto;
import fr.diginamic.VroomVroomCar.entity.Status;
import fr.diginamic.VroomVroomCar.entity.User;
import fr.diginamic.VroomVroomCar.mapper.UserMapper;
import fr.diginamic.VroomVroomCar.repository.UserRepository;
import fr.diginamic.VroomVroomCar.util.ValidationUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.exception.AuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
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
    public ResponseCookie logUser(User user) throws Exception {
        Optional<User> userOptional = userRepository.findByNom(user.getMail());
        if(userOptional.isPresent() && bcrypt.matches( user.getMotDePasse(),userOptional.get().getMotDePasse()) ){
            String role = userOptional.get().getStatus().name();
            return jwtAuthentificationService.generateToken(user.getMail(), role);
        }
        throw new Exception();
    }
    public void logoutUser(HttpServletResponse http) throws Exception {
        try {
            jwtAuthentificationService.invalidateToken(http);
        } catch (Exception e){
            throw new Exception("erreur lors de la déconnexion :" + e.toString());
        }
    }

    public void register(UserRequestDto userRequestDto){
        ValidationUtil.validateUserMail(userRequestDto.getMail());
        ValidationUtil.validateUserPassword(userRequestDto.getMotDePasse());
        User user = userMapper.toEntity(userRequestDto, bcrypt.encode(userRequestDto.getMotDePasse()),Status.ROLE_ACTIF);
        userRepository.save(user);
    }
}
