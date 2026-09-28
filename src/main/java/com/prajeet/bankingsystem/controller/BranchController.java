package com.prajeet.bankingsystem.controller;

import com.prajeet.bankingsystem.dto.BranchRequestDTO;
import com.prajeet.bankingsystem.dto.BranchResponseDTO;
import com.prajeet.bankingsystem.service.BranchService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/branches")
public class BranchController {

    private final BranchService branchService;

    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    // CREATE BRANCH
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BranchResponseDTO createBranch(
            @Valid @RequestBody BranchRequestDTO request) {

        return branchService.createBranch(request);
    }
    @GetMapping("/{id}")
    public BranchResponseDTO getBranchById(@PathVariable Long id) {

        return branchService.getBranchById(id);
    }
    @GetMapping
    public List<BranchResponseDTO> getAllBranches() {

        return branchService.getAllBranches();
    }
    @PutMapping("/{id}")
    public BranchResponseDTO updateBranch(
            @PathVariable Long id,
            @Valid @RequestBody BranchRequestDTO request) {

        return branchService.updateBranch(id, request);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBranch(@PathVariable Long id) {

        branchService.deleteBranch(id);
    }
}