package com.prajeet.bankingsystem.controller;

import com.prajeet.bankingsystem.dto.BeneficiaryRequestDTO;
import com.prajeet.bankingsystem.dto.BeneficiaryResponseDTO;
import com.prajeet.bankingsystem.service.BeneficiaryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/beneficiaries")
public class BeneficiaryController {

    private final BeneficiaryService beneficiaryService;

    public BeneficiaryController(
            BeneficiaryService beneficiaryService) {

        this.beneficiaryService = beneficiaryService;
    }

    // ADD BENEFICIARY
    @PostMapping
    public ResponseEntity<BeneficiaryResponseDTO> addBeneficiary(
            @Valid @RequestBody BeneficiaryRequestDTO request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        beneficiaryService.addBeneficiary(request)
                );
    }


    // GET USER BENEFICIARIES
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BeneficiaryResponseDTO>>
    getUserBeneficiaries(@PathVariable Long userId) {

        return ResponseEntity.ok(
                beneficiaryService
                        .getUserBeneficiaries(userId)
        );
    }


    // VERIFY
    @PutMapping("/{id}/verify")
    public ResponseEntity<BeneficiaryResponseDTO>
    verifyBeneficiary(@PathVariable Long id) {

        return ResponseEntity.ok(
                beneficiaryService
                        .verifyBeneficiary(id)
        );
    }


    // REMOVE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeBeneficiary(
            @PathVariable Long id) {

        beneficiaryService.removeBeneficiary(id);

        return ResponseEntity.ok(
                "Beneficiary removed successfully"
        );
    }
}