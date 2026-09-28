package com.prajeet.bankingsystem.controller;

import com.prajeet.bankingsystem.dto.BankRequestDTO;
import com.prajeet.bankingsystem.dto.BankResponseDTO;
import com.prajeet.bankingsystem.service.BankService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/banks")
public class BankController {

    private final BankService bankService;

    public BankController(BankService bankService) {
        this.bankService = bankService;
    }

    @PostMapping
    public BankResponseDTO createBank(
            @Valid @RequestBody BankRequestDTO request) {

        return bankService.createBank(request);
    }
}