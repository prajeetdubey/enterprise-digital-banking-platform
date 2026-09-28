package com.prajeet.bankingsystem.controller;

import com.prajeet.bankingsystem.dto.UserResponseDTO;
import com.prajeet.bankingsystem.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/users")
public class UserV2Controller {

    private final UserService userService;

    public UserV2Controller(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Page<UserResponseDTO> getUsers(
            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size) {

        Pageable pageable =
                PageRequest.of(page, size);

        return userService.getUsersPaginated(pageable);
    }
}