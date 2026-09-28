package com.prajeet.bankingsystem.service;

import com.prajeet.bankingsystem.dto.TransactionRequestDTO;
import com.prajeet.bankingsystem.dto.TransactionResponseDTO;
import com.prajeet.bankingsystem.dto.TransferRequestDTO;
import com.prajeet.bankingsystem.entity.Account;
import com.prajeet.bankingsystem.entity.BankTransaction;
import com.prajeet.bankingsystem.enums.AccountStatus;
import com.prajeet.bankingsystem.enums.TransactionType;
import com.prajeet.bankingsystem.exception.InsufficientBalanceException;
import com.prajeet.bankingsystem.exception.InvalidTransactionException;
import com.prajeet.bankingsystem.repository.AccountRepository;
import com.prajeet.bankingsystem.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransactionService(
            AccountRepository accountRepository,
            TransactionRepository transactionRepository) {

        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    // =========================
    // DEPOSIT
    // =========================

    @Transactional
    public TransactionResponseDTO deposit(
            String accountNumber,
            TransactionRequestDTO request) {

        Account account = getActiveAccount(accountNumber);

        BigDecimal amount = request.getAmount();

        account.setBalance(
                account.getBalance().add(amount)
        );

        account.setUpdatedAt(LocalDateTime.now());

        accountRepository.save(account);

        BankTransaction transaction = BankTransaction.builder()
                .type(TransactionType.DEPOSIT)
                .amount(amount)
                .destinationAccount(account)
                .createdAt(LocalDateTime.now())
                .build();

        BankTransaction savedTransaction =
                transactionRepository.save(transaction);

        return toResponseDTO(savedTransaction);
    }


    // =========================
    // WITHDRAW
    // =========================

    @Transactional
    public TransactionResponseDTO withdraw(
            String accountNumber,
            TransactionRequestDTO request) {

        Account account = getActiveAccount(accountNumber);

        BigDecimal amount = request.getAmount();

        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException(
                    "Insufficient balance"
            );
        }

        account.setBalance(
                account.getBalance().subtract(amount)
        );

        account.setUpdatedAt(LocalDateTime.now());

        accountRepository.save(account);

        BankTransaction transaction = BankTransaction.builder()
                .type(TransactionType.WITHDRAW)
                .amount(amount)
                .sourceAccount(account)
                .createdAt(LocalDateTime.now())
                .build();

        BankTransaction savedTransaction =
                transactionRepository.save(transaction);

        return toResponseDTO(savedTransaction);
    }


    // =========================
    // TRANSFER
    // =========================

    @Transactional
    public TransactionResponseDTO transfer(
            String sourceAccountNumber,
            TransferRequestDTO request) {

        Account sourceAccount =
                getActiveAccount(sourceAccountNumber);

        Account destinationAccount =
                getActiveAccount(request.getDestinationAccountNumber());

        if (sourceAccount.getId()
                .equals(destinationAccount.getId())) {

            throw new InvalidTransactionException(
                    "Source and destination accounts cannot be same"
            );
        }

        BigDecimal amount = request.getAmount();

        if (sourceAccount.getBalance().compareTo(amount) < 0) {

            throw new InsufficientBalanceException(
                    "Insufficient balance"
            );
        }

        // Deduct money from sender
        sourceAccount.setBalance(
                sourceAccount.getBalance().subtract(amount)
        );

        // Add money to receiver
        destinationAccount.setBalance(
                destinationAccount.getBalance().add(amount)
        );

        sourceAccount.setUpdatedAt(LocalDateTime.now());
        destinationAccount.setUpdatedAt(LocalDateTime.now());

        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);

        // Create transaction record
        BankTransaction transaction = BankTransaction.builder()
                .type(TransactionType.TRANSFER)
                .amount(amount)
                .sourceAccount(sourceAccount)
                .destinationAccount(destinationAccount)
                .createdAt(LocalDateTime.now())
                .build();

        BankTransaction savedTransaction =
                transactionRepository.save(transaction);

        return toResponseDTO(savedTransaction);
    }


    // =========================
    // TRANSACTION HISTORY
    // =========================

    @Transactional(readOnly = true)
    public List<TransactionResponseDTO> getTransactionHistory(
            String accountNumber) {

        Account account =
                accountRepository.findByAccountNumber(accountNumber)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Account not found"
                                ));

        List<BankTransaction> transactions =
                transactionRepository
                        .findBySourceAccount_IdOrDestinationAccount_IdOrderByCreatedAtDesc(
                                account.getId(),
                                account.getId()
                        );

        return transactions.stream()
                .map(this::toResponseDTO)
                .toList();
    }


    // =========================
    // MINI STATEMENT
    // =========================

    @Transactional(readOnly = true)
    public List<TransactionResponseDTO> getMiniStatement(
            String accountNumber) {

        return getTransactionHistory(accountNumber)
                .stream()
                .limit(10)
                .toList();
    }


    // =========================
    // BALANCE INQUIRY
    // =========================

    @Transactional(readOnly = true)
    public BigDecimal getBalance(String accountNumber) {

        Account account =
                getActiveAccount(accountNumber);

        return account.getBalance();
    }


    // =========================
    // HELPER
    // =========================

    private Account getActiveAccount(String accountNumber) {

        Account account =
                accountRepository.findByAccountNumber(accountNumber)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Account not found"
                                ));

        if (account.getStatus() != AccountStatus.ACTIVE) {

            throw new InvalidTransactionException(
                    "Account is not active"
            );
        }

        return account;
    }


    // =========================
    // ENTITY -> DTO
    // =========================

    private TransactionResponseDTO toResponseDTO(
            BankTransaction transaction) {

        return TransactionResponseDTO.builder()
                .id(transaction.getId())
                .type(transaction.getType())
                .amount(transaction.getAmount())
                .sourceAccountNumber(
                        transaction.getSourceAccount() != null
                                ? transaction.getSourceAccount()
                                .getAccountNumber()
                                : null
                )
                .destinationAccountNumber(
                        transaction.getDestinationAccount() != null
                                ? transaction.getDestinationAccount()
                                .getAccountNumber()
                                : null
                )
                .createdAt(transaction.getCreatedAt())
                .build();
    }
}