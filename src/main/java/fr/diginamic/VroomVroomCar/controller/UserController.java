package fr.diginamic.VroomVroomCar.controller;

import fr.diginamic.VroomVroomCar.entity.User;
import fr.diginamic.VroomVroomCar.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/find-all")
    public List<User> findAll(){
        return  userService.findAll();
    }

    @GetMapping("/{id}")
    public User findById(@RequestParam int id){
        return userService.findById(id);
    }

    @GetMapping("/find-by-nom/{nom}")
    public User findByNom(String nom){
        return userService.findByNom(nom);
    }

    @PostMapping("/post-user")
    public void addUser(@RequestBody User user){
        userService.addUser(user);
    }
    @PutMapping("/put-user/{id}")
    public void editUser(@RequestParam int id ,@RequestBody User user){
        userService.editUser(id,user);
    }

    @PutMapping("/put-user-by-nom/{nom}")
    public void editUser(@RequestParam String nom ,@RequestBody User user){
        userService.editUser(nom,user);
    }

}
