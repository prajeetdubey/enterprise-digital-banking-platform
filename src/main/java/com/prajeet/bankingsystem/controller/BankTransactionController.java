package com.prajeet.bankingsystem.controller;

import com.prajeet.bankingsystem.dto.BankTransactionRequestDTO;
import com.prajeet.bankingsystem.dto.BankTransactionResponseDTO;
import com.prajeet.bankingsystem.enums.TransactionType;
import com.prajeet.bankingsystem.service.BankTransactionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/transactions")
public class BankTransactionController {

    private final BankTransactionService transactionService;

    public BankTransactionController(
            BankTransactionService transactionService) {

        this.transactionService = transactionService;
    }

    // =========================
    // CREATE TRANSACTION
    // =========================

    @PostMapping
    public BankTransactionResponseDTO createTransaction(
            @Valid @RequestBody BankTransactionRequestDTO requestDTO) {

        return transactionService.createTransaction(requestDTO);
    }

    // =========================
    // SEARCH TRANSACTIONS
    // =========================

    @GetMapping("/search")
    public Page<BankTransactionResponseDTO> searchTransactions(

            @RequestParam(required = false)
            Long accountId,

            @RequestParam(required = false)
            TransactionType transactionType,

            @RequestParam(required = false)
            BigDecimal minAmount,

            @RequestParam(required = false)
            BigDecimal maxAmount,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(defaultValue = "createdAt")
            String sortBy,

            @RequestParam(defaultValue = "desc")
            String direction) {

        Sort.Direction sortDirection =
                direction.equalsIgnoreCase("asc")
                        ? Sort.Direction.ASC
                        : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sortDirection, sortBy)
        );

        return transactionService.searchTransactions(
                accountId,
                transactionType,
                minAmount,
                maxAmount,
                pageable
        );
    }
}