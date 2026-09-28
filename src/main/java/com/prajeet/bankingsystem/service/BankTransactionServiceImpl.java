package com.prajeet.bankingsystem.service.impl;

import com.prajeet.bankingsystem.dto.BankTransactionRequestDTO;
import com.prajeet.bankingsystem.dto.BankTransactionResponseDTO;
import com.prajeet.bankingsystem.entity.Account;
import com.prajeet.bankingsystem.entity.BankTransaction;
import com.prajeet.bankingsystem.enums.TransactionType;
import com.prajeet.bankingsystem.event.TransactionEvent;
import com.prajeet.bankingsystem.exception.ResourceNotFoundException;
import com.prajeet.bankingsystem.mapper.BankTransactionMapper;
import com.prajeet.bankingsystem.repository.AccountRepository;
import com.prajeet.bankingsystem.repository.BankTransactionRepository;
import com.prajeet.bankingsystem.service.BankTransactionService;
import com.prajeet.bankingsystem.service.KafkaTransactionProducer;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class BankTransactionServiceImpl implements BankTransactionService {

    private final BankTransactionRepository bankTransactionRepository;
    private final AccountRepository accountRepository;
    private final KafkaTransactionProducer kafkaTransactionProducer;

    public BankTransactionServiceImpl(
            BankTransactionRepository bankTransactionRepository,
            AccountRepository accountRepository,
            KafkaTransactionProducer kafkaTransactionProducer) {

        this.bankTransactionRepository = bankTransactionRepository;
        this.accountRepository = accountRepository;
        this.kafkaTransactionProducer = kafkaTransactionProducer;
    }

    @Override
    @Transactional
    public BankTransactionResponseDTO createTransaction(
            BankTransactionRequestDTO requestDTO) {

        TransactionType type = requestDTO.getType();
        BigDecimal amount = requestDTO.getAmount();

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Transaction amount must be greater than zero"
            );
        }

        Account sourceAccount = null;
        Account destinationAccount = null;

        // =========================
        // DEPOSIT
        // =========================

        if (type == TransactionType.DEPOSIT) {

            if (requestDTO.getDestinationAccountId() == null) {
                throw new IllegalArgumentException(
                        "Destination account is required for deposit"
                );
            }

            destinationAccount = accountRepository.findById(
                    requestDTO.getDestinationAccountId()
            ).orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Destination account not found"
                    )
            );

            destinationAccount.setBalance(
                    destinationAccount.getBalance().add(amount)
            );

            accountRepository.save(destinationAccount);
        }

        // =========================
        // WITHDRAW
        // =========================

        else if (type == TransactionType.WITHDRAW) {

            if (requestDTO.getSourceAccountId() == null) {
                throw new IllegalArgumentException(
                        "Source account is required for withdrawal"
                );
            }

            sourceAccount = accountRepository.findById(
                    requestDTO.getSourceAccountId()
            ).orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Source account not found"
                    )
            );

            if (sourceAccount.getBalance().compareTo(amount) < 0) {
                throw new IllegalArgumentException(
                        "Insufficient account balance"
                );
            }

            sourceAccount.setBalance(
                    sourceAccount.getBalance().subtract(amount)
            );

            accountRepository.save(sourceAccount);
        }

        // =========================
        // TRANSFER
        // =========================

        else if (type == TransactionType.TRANSFER) {

            if (requestDTO.getSourceAccountId() == null ||
                    requestDTO.getDestinationAccountId() == null) {

                throw new IllegalArgumentException(
                        "Source and destination accounts are required for transfer"
                );
            }

            sourceAccount = accountRepository.findById(
                    requestDTO.getSourceAccountId()
            ).orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Source account not found"
                    )
            );

            destinationAccount = accountRepository.findById(
                    requestDTO.getDestinationAccountId()
            ).orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Destination account not found"
                    )
            );

            if (sourceAccount.getId().equals(destinationAccount.getId())) {
                throw new IllegalArgumentException(
                        "Source and destination accounts cannot be the same"
                );
            }

            if (sourceAccount.getBalance().compareTo(amount) < 0) {
                throw new IllegalArgumentException(
                        "Insufficient account balance"
                );
            }

            sourceAccount.setBalance(
                    sourceAccount.getBalance().subtract(amount)
            );

            destinationAccount.setBalance(
                    destinationAccount.getBalance().add(amount)
            );

            accountRepository.save(sourceAccount);
            accountRepository.save(destinationAccount);
        }

        else {
            throw new IllegalArgumentException(
                    "Unsupported transaction type"
            );
        }

        // =========================
        // SAVE TRANSACTION
        // =========================

        BankTransaction transaction = BankTransaction.builder()
                .type(type)
                .amount(amount)
                .sourceAccount(sourceAccount)
                .destinationAccount(destinationAccount)
                .createdAt(LocalDateTime.now())
                .build();

        BankTransaction savedTransaction =
                bankTransactionRepository.save(transaction);

        // =========================
        // CREATE RESPONSE
        // =========================

        BankTransactionResponseDTO responseDTO =
                BankTransactionMapper.toResponseDTO(savedTransaction);

        // =========================
        // KAFKA EVENT
        // =========================

        TransactionEvent event = new TransactionEvent(
                savedTransaction.getId(),
                sourceAccount != null
                        ? sourceAccount.getId()
                        : null,
                destinationAccount != null
                        ? destinationAccount.getId()
                        : null,
                amount,
                type,
                savedTransaction.getCreatedAt()
        );

        kafkaTransactionProducer.sendTransactionEvent(event);

        return responseDTO;
    }

    @Override
    public Page<BankTransactionResponseDTO> searchTransactions(
            Long accountId,
            TransactionType transactionType,
            BigDecimal minAmount,
            BigDecimal maxAmount,
            Pageable pageable) {

        return bankTransactionRepository
                .searchTransactions(
                        accountId,
                        transactionType,
                        minAmount,
                        maxAmount,
                        pageable
                )
                .map(BankTransactionMapper::toResponseDTO);
    }
}