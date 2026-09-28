package com.prajeet.bankingsystem.controller;

import com.prajeet.bankingsystem.dto.UserResponseDTO;
import com.prajeet.bankingsystem.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserV1Controller {

    private final UserService userService;

    public UserV1Controller(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserResponseDTO> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserResponseDTO getUser(
            @PathVariable Long id) {

        return userService.getUserById(id);
    }
}