package fr.diginamic.VroomVroomCar.controller;

import fr.diginamic.VroomVroomCar.dto.request.UserRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.UserResponseDto;
import fr.diginamic.VroomVroomCar.entity.User;
import fr.diginamic.VroomVroomCar.exception.ResourceNotFoundException;
import fr.diginamic.VroomVroomCar.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping()
    public List<UserResponseDto> findAll(){
        return  userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserResponseDto findById(@RequestParam int id){
        try {
            return userService.getUserById(id);
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/find-by-nom/{nom}")
    public UserResponseDto findByNom(String nom){
        try {
            return userService.getByNom(nom);
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/create-user")
    public UserResponseDto addUser(@RequestBody UserRequestDto user){
        try {
           return userService.createUser(user);
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    @PutMapping("/edit-user/{id}")
    public UserResponseDto editUser(@RequestParam int id ,@RequestBody UserRequestDto user){
        try {
            return userService.updateUser(id,user);
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/edit-user-by-nom/{nom}")
    public UserResponseDto editUser(@RequestParam String nom ,@RequestBody UserRequestDto user){
        try {
            return userService.updateUser(nom,user);
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
