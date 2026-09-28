package com.prajeet.bankingsystem.controller;

import com.prajeet.bankingsystem.dto.TransactionRequestDTO;
import com.prajeet.bankingsystem.dto.TransactionResponseDTO;
import com.prajeet.bankingsystem.dto.TransferRequestDTO;
import com.prajeet.bankingsystem.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(
            TransactionService transactionService) {

        this.transactionService = transactionService;
    }


    // =========================
    // DEPOSIT
    // =========================

    @PostMapping("/deposit/{accountNumber}")
    public ResponseEntity<TransactionResponseDTO> deposit(
            @PathVariable String accountNumber,
            @Valid @RequestBody TransactionRequestDTO request) {

        return ResponseEntity.ok(
                transactionService.deposit(
                        accountNumber,
                        request
                )
        );
    }


    // =========================
    // WITHDRAW
    // =========================

    @PostMapping("/withdraw/{accountNumber}")
    public ResponseEntity<TransactionResponseDTO> withdraw(
            @PathVariable String accountNumber,
            @Valid @RequestBody TransactionRequestDTO request) {

        return ResponseEntity.ok(
                transactionService.withdraw(
                        accountNumber,
                        request
                )
        );
    }


    // =========================
    // TRANSFER
    // =========================

    @PostMapping("/transfer/{sourceAccountNumber}")
    public ResponseEntity<TransactionResponseDTO> transfer(
            @PathVariable String sourceAccountNumber,
            @Valid @RequestBody TransferRequestDTO request) {

        return ResponseEntity.ok(
                transactionService.transfer(
                        sourceAccountNumber,
                        request
                )
        );
    }


    // =========================
    // BALANCE
    // =========================

    @GetMapping("/balance/{accountNumber}")
    public ResponseEntity<BigDecimal> getBalance(
            @PathVariable String accountNumber) {

        return ResponseEntity.ok(
                transactionService.getBalance(
                        accountNumber
                )
        );
    }


    // =========================
    // TRANSACTION HISTORY
    // =========================

    @GetMapping("/history/{accountNumber}")
    public ResponseEntity<List<TransactionResponseDTO>>
    getTransactionHistory(
            @PathVariable String accountNumber) {

        return ResponseEntity.ok(
                transactionService.getTransactionHistory(
                        accountNumber
                )
        );
    }


    // =========================
    // MINI STATEMENT
    // =========================

    @GetMapping("/mini-statement/{accountNumber}")
    public ResponseEntity<List<TransactionResponseDTO>>
    getMiniStatement(
            @PathVariable String accountNumber) {

        return ResponseEntity.ok(
                transactionService.getMiniStatement(
                        accountNumber
                )
        );
    }
}