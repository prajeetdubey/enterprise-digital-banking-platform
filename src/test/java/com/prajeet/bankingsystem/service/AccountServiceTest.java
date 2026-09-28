package com.prajeet.bankingsystem.service;

import com.prajeet.bankingsystem.dto.AccountRequestDTO;
import com.prajeet.bankingsystem.dto.AccountResponseDTO;
import com.prajeet.bankingsystem.entity.Account;
import com.prajeet.bankingsystem.entity.Bank;
import com.prajeet.bankingsystem.entity.Branch;
import com.prajeet.bankingsystem.entity.User;
import com.prajeet.bankingsystem.enums.AccountType;
import com.prajeet.bankingsystem.mapper.AccountMapper;
import com.prajeet.bankingsystem.repository.AccountRepository;
import com.prajeet.bankingsystem.repository.BankRepository;
import com.prajeet.bankingsystem.repository.BranchRepository;
import com.prajeet.bankingsystem.repository.UserRepository;
import com.prajeet.bankingsystem.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BankRepository bankRepository;

    @Mock
    private BranchRepository branchRepository;

    @InjectMocks
    private AccountService accountService;


    // =========================================================
    // CREATE ACCOUNT
    // =========================================================

    @Test
    void createAccount_shouldCreateAndSaveAccount() {

        AccountRequestDTO request =
                mock(AccountRequestDTO.class);

        when(request.getUserId())
                .thenReturn(1L);

        when(request.getBankId())
                .thenReturn(1L);

        when(request.getBranchId())
                .thenReturn(1L);

        when(request.getAccountType())
                .thenReturn(AccountType.SAVINGS);

        when(request.getInitialBalance())
                .thenReturn(new BigDecimal("5000.00"));


        User user = new User();
        user.setId(1L);

        Bank bank = new Bank();
        bank.setId(1L);

        Branch branch = new Branch();
        branch.setId(1L);


        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(bankRepository.findById(1L))
                .thenReturn(Optional.of(bank));

        when(branchRepository.findById(1L))
                .thenReturn(Optional.of(branch));


        Account savedAccount = new Account();

        savedAccount.setId(10L);
        savedAccount.setAccountNumber("SB1234567890");
        savedAccount.setAccountType(AccountType.SAVINGS);
        savedAccount.setBalance(new BigDecimal("5000.00"));
        savedAccount.setUser(user);
        savedAccount.setBank(bank);
        savedAccount.setBranch(branch);


        when(accountRepository.save(any(Account.class)))
                .thenReturn(savedAccount);


        AccountResponseDTO expectedResponse =
                mock(AccountResponseDTO.class);


        try (MockedStatic<AccountMapper> mapper =
                     mockStatic(AccountMapper.class)) {

            mapper.when(() ->
                    AccountMapper.toResponseDTO(savedAccount)
            ).thenReturn(expectedResponse);


            AccountResponseDTO result =
                    accountService.createAccount(request);


            assertSame(
                    expectedResponse,
                    result
            );

            verify(userRepository)
                    .findById(1L);

            verify(bankRepository)
                    .findById(1L);

            verify(branchRepository)
                    .findById(1L);

            verify(accountRepository)
                    .save(any(Account.class));
        }
    }


    // =========================================================
    // GET ACCOUNT BY ID
    // =========================================================

    @Test
    void getAccountById_shouldReturnAccount() {

        Account account = new Account();

        account.setId(1L);
        account.setAccountNumber("SB1234567890");
        account.setAccountType(AccountType.SAVINGS);
        account.setBalance(new BigDecimal("5000.00"));


        when(accountRepository.findById(1L))
                .thenReturn(Optional.of(account));


        AccountResponseDTO expectedResponse =
                mock(AccountResponseDTO.class);


        try (MockedStatic<AccountMapper> mapper =
                     mockStatic(AccountMapper.class)) {

            mapper.when(() ->
                    AccountMapper.toResponseDTO(account)
            ).thenReturn(expectedResponse);


            AccountResponseDTO result =
                    accountService.getAccountById(1L);


            assertSame(
                    expectedResponse,
                    result
            );

            verify(accountRepository)
                    .findById(1L);
        }
    }


    // =========================================================
    // GET ACCOUNT BY ID - NOT FOUND
    // =========================================================

    @Test
    void getAccountById_shouldThrowException_whenAccountNotFound() {

        when(accountRepository.findById(99L))
                .thenReturn(Optional.empty());


        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> accountService.getAccountById(99L)
                );


        assertEquals(
                "Account not found with id 99",
                exception.getMessage()
        );


        verify(accountRepository)
                .findById(99L);
    }


    // =========================================================
    // DELETE ACCOUNT
    // =========================================================

    @Test
    void deleteAccount_shouldDeleteExistingAccount() {

        Account account = new Account();

        account.setId(1L);
        account.setAccountNumber("SB1234567890");


        when(accountRepository.findById(1L))
                .thenReturn(Optional.of(account));


        accountService.deleteAccount(1L);


        verify(accountRepository)
                .findById(1L);

        verify(accountRepository)
                .delete(account);
    }


    // =========================================================
    // DELETE ACCOUNT - NOT FOUND
    // =========================================================

    @Test
    void deleteAccount_shouldThrowException_whenAccountNotFound() {

        when(accountRepository.findById(99L))
                .thenReturn(Optional.empty());


        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> accountService.deleteAccount(99L)
                );


        assertEquals(
                "Account not found",
                exception.getMessage()
        );


        verify(accountRepository)
                .findById(99L);

        verify(accountRepository, never())
                .delete(any());
    }


    // =========================================================
    // SEARCH ACCOUNTS
    // =========================================================

    @Test
    void searchAccounts_shouldReturnMappedPage() {

        Account account = new Account();

        account.setId(1L);
        account.setAccountNumber("SB1234567890");
        account.setAccountType(AccountType.SAVINGS);
        account.setBalance(new BigDecimal("5000.00"));


        PageRequest pageable =
                PageRequest.of(0, 10);


        Page<Account> accountPage =
                new PageImpl<>(
                        List.of(account),
                        pageable,
                        1
                );


        when(accountRepository.searchAccounts(
                "Prajeet",
                pageable
        )).thenReturn(accountPage);


        AccountResponseDTO response =
                mock(AccountResponseDTO.class);


        try (MockedStatic<AccountMapper> mapper =
                     mockStatic(AccountMapper.class)) {

            mapper.when(() ->
                    AccountMapper.toResponseDTO(account)
            ).thenReturn(response);


            Page<AccountResponseDTO> result =
                    accountService.searchAccounts(
                            "Prajeet",
                            pageable
                    );


            assertEquals(
                    1,
                    result.getTotalElements()
            );

            assertEquals(
                    1,
                    result.getContent().size()
            );

            assertSame(
                    response,
                    result.getContent().get(0)
            );


            verify(accountRepository)
                    .searchAccounts(
                            "Prajeet",
                            pageable
                    );
        }
    }
}