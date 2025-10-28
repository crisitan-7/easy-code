package com.crisi.easy.controller;

import com.crisi.easy.model.User;
import com.crisi.easy.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepo;

    public UserController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping
    public List<User> all() {
        return userRepo.findAll();
    }

    @GetMapping("/{id}")
    public User one(@PathVariable Long id) {
        return userRepo.findById(id);
    }

    @PostMapping
    public String add(@RequestBody User user) {
        userRepo.save(user);
        return "User added!";
    }
}