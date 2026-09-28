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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;


    @BeforeEach
    void setUp() {
        // Mockito automatically creates TransactionService
        // and injects the mocked repositories.
    }


    // =========================================================
    // DEPOSIT
    // =========================================================

    @Test
    void deposit_shouldIncreaseAccountBalance() {

        Account account = createActiveAccount(
                1L,
                "SB1234567890",
                "1000.00"
        );

        TransactionRequestDTO request = mock(TransactionRequestDTO.class);

        when(request.getAmount())
                .thenReturn(new BigDecimal("500.00"));

        when(accountRepository.findByAccountNumber("SB1234567890"))
                .thenReturn(Optional.of(account));

        BankTransaction savedTransaction =
                BankTransaction.builder()
                        .id(1L)
                        .type(TransactionType.DEPOSIT)
                        .amount(new BigDecimal("500.00"))
                        .destinationAccount(account)
                        .build();

        when(transactionRepository.save(any(BankTransaction.class)))
                .thenReturn(savedTransaction);

        TransactionResponseDTO response =
                transactionService.deposit(
                        "SB1234567890",
                        request
                );

        // Balance should become 1500
        assertEquals(
                new BigDecimal("1500.00"),
                account.getBalance()
        );

        // Transaction should be saved
        verify(accountRepository).save(account);
        verify(transactionRepository).save(any(BankTransaction.class));

        // Response should contain correct transaction information
        assertEquals(
                TransactionType.DEPOSIT,
                response.getType()
        );

        assertEquals(
                new BigDecimal("500.00"),
                response.getAmount()
        );
    }


    // =========================================================
    // WITHDRAW
    // =========================================================

    @Test
    void withdraw_shouldDecreaseAccountBalance() {

        Account account = createActiveAccount(
                1L,
                "SB1234567890",
                "2000.00"
        );

        TransactionRequestDTO request = mock(TransactionRequestDTO.class);

        when(request.getAmount())
                .thenReturn(new BigDecimal("500.00"));

        when(accountRepository.findByAccountNumber("SB1234567890"))
                .thenReturn(Optional.of(account));

        BankTransaction savedTransaction =
                BankTransaction.builder()
                        .id(2L)
                        .type(TransactionType.WITHDRAW)
                        .amount(new BigDecimal("500.00"))
                        .sourceAccount(account)
                        .build();

        when(transactionRepository.save(any(BankTransaction.class)))
                .thenReturn(savedTransaction);

        TransactionResponseDTO response =
                transactionService.withdraw(
                        "SB1234567890",
                        request
                );

        // 2000 - 500 = 1500
        assertEquals(
                new BigDecimal("1500.00"),
                account.getBalance()
        );

        verify(accountRepository).save(account);
        verify(transactionRepository).save(any(BankTransaction.class));

        assertEquals(
                TransactionType.WITHDRAW,
                response.getType()
        );

        assertEquals(
                new BigDecimal("500.00"),
                response.getAmount()
        );
    }


    // =========================================================
    // WITHDRAW - INSUFFICIENT BALANCE
    // =========================================================

    @Test
    void withdraw_shouldThrowException_whenBalanceIsInsufficient() {

        Account account = createActiveAccount(
                1L,
                "SB1234567890",
                "300.00"
        );

        TransactionRequestDTO request = mock(TransactionRequestDTO.class);

        when(request.getAmount())
                .thenReturn(new BigDecimal("500.00"));

        when(accountRepository.findByAccountNumber("SB1234567890"))
                .thenReturn(Optional.of(account));

        InsufficientBalanceException exception =
                assertThrows(
                        InsufficientBalanceException.class,
                        () -> transactionService.withdraw(
                                "SB1234567890",
                                request
                        )
                );

        assertEquals(
                "Insufficient balance",
                exception.getMessage()
        );

        // Account balance should remain unchanged
        assertEquals(
                new BigDecimal("300.00"),
                account.getBalance()
        );

        // No database save should happen
        verify(accountRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }


    // =========================================================
    // TRANSFER
    // =========================================================

    @Test
    void transfer_shouldMoveMoneyBetweenAccounts() {

        Account sourceAccount = createActiveAccount(
                1L,
                "SB1111111111",
                "2000.00"
        );

        Account destinationAccount = createActiveAccount(
                2L,
                "SB2222222222",
                "1000.00"
        );

        TransferRequestDTO request = mock(TransferRequestDTO.class);

        when(request.getDestinationAccountNumber())
                .thenReturn("SB2222222222");

        when(request.getAmount())
                .thenReturn(new BigDecimal("500.00"));

        when(accountRepository.findByAccountNumber("SB1111111111"))
                .thenReturn(Optional.of(sourceAccount));

        when(accountRepository.findByAccountNumber("SB2222222222"))
                .thenReturn(Optional.of(destinationAccount));

        BankTransaction savedTransaction =
                BankTransaction.builder()
                        .id(3L)
                        .type(TransactionType.TRANSFER)
                        .amount(new BigDecimal("500.00"))
                        .sourceAccount(sourceAccount)
                        .destinationAccount(destinationAccount)
                        .build();

        when(transactionRepository.save(any(BankTransaction.class)))
                .thenReturn(savedTransaction);

        TransactionResponseDTO response =
                transactionService.transfer(
                        "SB1111111111",
                        request
                );

        // Sender: 2000 - 500 = 1500
        assertEquals(
                new BigDecimal("1500.00"),
                sourceAccount.getBalance()
        );

        // Receiver: 1000 + 500 = 1500
        assertEquals(
                new BigDecimal("1500.00"),
                destinationAccount.getBalance()
        );

        // Both accounts must be saved
        verify(accountRepository).save(sourceAccount);
        verify(accountRepository).save(destinationAccount);

        // Transaction must be saved
        verify(transactionRepository)
                .save(any(BankTransaction.class));

        assertEquals(
                TransactionType.TRANSFER,
                response.getType()
        );

        assertEquals(
                new BigDecimal("500.00"),
                response.getAmount()
        );

        assertEquals(
                "SB1111111111",
                response.getSourceAccountNumber()
        );

        assertEquals(
                "SB2222222222",
                response.getDestinationAccountNumber()
        );
    }


    // =========================================================
    // TRANSFER - INSUFFICIENT BALANCE
    // =========================================================

    @Test
    void transfer_shouldThrowException_whenBalanceIsInsufficient() {

        Account sourceAccount = createActiveAccount(
                1L,
                "SB1111111111",
                "300.00"
        );

        Account destinationAccount = createActiveAccount(
                2L,
                "SB2222222222",
                "1000.00"
        );

        TransferRequestDTO request = mock(TransferRequestDTO.class);

        when(request.getDestinationAccountNumber())
                .thenReturn("SB2222222222");

        when(request.getAmount())
                .thenReturn(new BigDecimal("500.00"));

        when(accountRepository.findByAccountNumber("SB1111111111"))
                .thenReturn(Optional.of(sourceAccount));

        when(accountRepository.findByAccountNumber("SB2222222222"))
                .thenReturn(Optional.of(destinationAccount));

        assertThrows(
                InsufficientBalanceException.class,
                () -> transactionService.transfer(
                        "SB1111111111",
                        request
                )
        );

        // Money must not move
        assertEquals(
                new BigDecimal("300.00"),
                sourceAccount.getBalance()
        );

        assertEquals(
                new BigDecimal("1000.00"),
                destinationAccount.getBalance()
        );

        verify(accountRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }


    // =========================================================
    // TRANSFER - SAME ACCOUNT
    // =========================================================

    @Test
    void transfer_shouldThrowException_whenSourceAndDestinationAreSame() {

        Account account = createActiveAccount(
                1L,
                "SB1111111111",
                "2000.00"
        );

        TransferRequestDTO request = mock(TransferRequestDTO.class);

        when(request.getDestinationAccountNumber())
                .thenReturn("SB1111111111");

        when(request.getAmount())
                .thenReturn(new BigDecimal("500.00"));

        when(accountRepository.findByAccountNumber("SB1111111111"))
                .thenReturn(Optional.of(account));

        InvalidTransactionException exception =
                assertThrows(
                        InvalidTransactionException.class,
                        () -> transactionService.transfer(
                                "SB1111111111",
                                request
                        )
                );

        assertEquals(
                "Source and destination accounts cannot be same",
                exception.getMessage()
        );

        verify(accountRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }


    // =========================================================
    // GET BALANCE
    // =========================================================

    @Test
    void getBalance_shouldReturnCurrentBalance() {

        Account account = createActiveAccount(
                1L,
                "SB1234567890",
                "7500.00"
        );

        when(accountRepository.findByAccountNumber("SB1234567890"))
                .thenReturn(Optional.of(account));

        BigDecimal balance =
                transactionService.getBalance("SB1234567890");

        assertEquals(
                new BigDecimal("7500.00"),
                balance
        );

        verify(accountRepository)
                .findByAccountNumber("SB1234567890");
    }


    // =========================================================
    // TRANSACTION HISTORY
    // =========================================================

    @Test
    void getTransactionHistory_shouldReturnTransactions() {

        Account account = createActiveAccount(
                1L,
                "SB1234567890",
                "5000.00"
        );

        BankTransaction transaction1 =
                BankTransaction.builder()
                        .id(1L)
                        .type(TransactionType.DEPOSIT)
                        .amount(new BigDecimal("1000.00"))
                        .destinationAccount(account)
                        .build();

        BankTransaction transaction2 =
                BankTransaction.builder()
                        .id(2L)
                        .type(TransactionType.WITHDRAW)
                        .amount(new BigDecimal("500.00"))
                        .sourceAccount(account)
                        .build();

        when(accountRepository.findByAccountNumber("SB1234567890"))
                .thenReturn(Optional.of(account));

        when(transactionRepository
                .findBySourceAccount_IdOrDestinationAccount_IdOrderByCreatedAtDesc(
                        1L,
                        1L
                ))
                .thenReturn(List.of(transaction1, transaction2));

        List<TransactionResponseDTO> result =
                transactionService.getTransactionHistory(
                        "SB1234567890"
                );

        assertEquals(2, result.size());

        assertEquals(
                TransactionType.DEPOSIT,
                result.get(0).getType()
        );

        assertEquals(
                TransactionType.WITHDRAW,
                result.get(1).getType()
        );

        verify(transactionRepository)
                .findBySourceAccount_IdOrDestinationAccount_IdOrderByCreatedAtDesc(
                        1L,
                        1L
                );
    }


    // =========================================================
    // HELPER
    // =========================================================

    private Account createActiveAccount(
            Long id,
            String accountNumber,
            String balance) {

        Account account = new Account();

        account.setId(id);
        account.setAccountNumber(accountNumber);
        account.setBalance(new BigDecimal(balance));
        account.setStatus(AccountStatus.ACTIVE);

        return account;
    }
}