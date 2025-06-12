package fr.diginamic.VroomVroomCar.service;

import fr.diginamic.VroomVroomCar.entity.Status;
import fr.diginamic.VroomVroomCar.entity.User;
import fr.diginamic.VroomVroomCar.exception.ResourceNotFoundException;
import fr.diginamic.VroomVroomCar.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

public class UserService {

    @Autowired
    UserRepository userRepository;
    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(int id){
        return userRepository.findById(id)
                .get();
    }

    public User findByNom(String nom){
        return userRepository.findByNom(nom);
    }

    public void addUser(User user){
        userRepository.save(new User(
                user.getNom(),
                user.getPrenom(),
                user.getMail(),
                user.getAdresse(),
                user.getMotDePasse(),
                Status.ACTIF));
    }

    public void editUser(int id, User user){
    }

    public void editUser(String nom, User user){

    }

}
