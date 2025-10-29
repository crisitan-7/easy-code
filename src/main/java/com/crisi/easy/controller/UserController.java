package com.crisi.easy.controller;

import com.crisi.easy.model.User;
import com.crisi.easy.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository repo;

    public UserController(UserRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<User> all() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public User one(@PathVariable Long id) {
        return repo.findById(id);
    }

    @PostMapping
    public String add(@RequestBody User user) {
        repo.save(user);
        return "User added!";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        repo.delete(id);
        return "User deleted!";
    }
}