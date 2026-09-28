package com.prajeet.bankingsystem.service;

import com.prajeet.bankingsystem.dto.BankTransactionRequestDTO;
import com.prajeet.bankingsystem.dto.BankTransactionResponseDTO;
import com.prajeet.bankingsystem.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface BankTransactionService {

    BankTransactionResponseDTO createTransaction(
            BankTransactionRequestDTO requestDTO
    );

    Page<BankTransactionResponseDTO> searchTransactions(
            Long accountId,
            TransactionType transactionType,
            BigDecimal minAmount,
            BigDecimal maxAmount,
            Pageable pageable
    );
}