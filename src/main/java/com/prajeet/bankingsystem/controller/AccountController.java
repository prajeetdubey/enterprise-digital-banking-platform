package com.prajeet.bankingsystem.controller;

import com.prajeet.bankingsystem.dto.AccountRequestDTO;
import com.prajeet.bankingsystem.dto.AccountResponseDTO;
import com.prajeet.bankingsystem.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService){
        this.accountService = accountService;
    }

    // CREATE
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponseDTO createAccount(
            @Valid @RequestBody AccountRequestDTO request){

        return accountService.createAccount(request);
    }

    // GET BY ID
    @GetMapping("/{id}")
    public AccountResponseDTO getAccount(@PathVariable Long id){

        return accountService.getAccountById(id);
    }

    // GET ALL
    @GetMapping
    public List<AccountResponseDTO> getAllAccounts(){

        return accountService.getAllAccounts();
    }

    // UPDATE
    @PutMapping("/{id}")
    public AccountResponseDTO updateAccount(
            @PathVariable Long id,
            @Valid @RequestBody AccountRequestDTO request){

        return accountService.updateAccount(id, request);
    }

    // DELETE
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount(@PathVariable Long id){

        accountService.deleteAccount(id);
    }
    @GetMapping("/search")
    public Page<AccountResponseDTO> searchAccounts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        return accountService.searchAccounts(keyword, pageable);
    }

}