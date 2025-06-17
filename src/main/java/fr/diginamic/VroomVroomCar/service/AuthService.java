package fr.diginamic.VroomVroomCar.service;

import fr.diginamic.VroomVroomCar.dto.request.UserRequestDto;
import fr.diginamic.VroomVroomCar.entity.Status;
import fr.diginamic.VroomVroomCar.entity.User;
import fr.diginamic.VroomVroomCar.mapper.UserMapper;
import fr.diginamic.VroomVroomCar.repository.UserRepository;
import fr.diginamic.VroomVroomCar.util.ValidationUtil;
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
        Optional<User> userOptional = userRepository.findByNom(user.getNom());
        if(userOptional.isPresent() && bcrypt.matches( user.getMotDePasse(),userOptional.get().getMotDePasse()) ){
            return jwtAuthentificationService.generateToken(user.getNom());
        }
        throw new Exception();
    }
    public ResponseCookie logoutUser() {
        return jwtAuthentificationService.invalidateToken();
    }

    public void register(UserRequestDto userRequestDto){
        ValidationUtil.validateUserMail(userRequestDto.getMail());
        ValidationUtil.validateUserPassword(userRequestDto.getMotDePasse());
        User user = userMapper.toEntity(userRequestDto, bcrypt.encode(userRequestDto.getMotDePasse()),Status.ACTIF);
        //vérification sur l'email
        userRepository.save(user);
    }
}
