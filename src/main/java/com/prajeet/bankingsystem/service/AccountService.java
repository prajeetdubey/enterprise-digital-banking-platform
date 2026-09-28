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
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.cache.annotation.Cacheable;
import java.util.List;
import java.util.UUID;
import com.prajeet.bankingsystem.exception.ResourceNotFoundException;
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final BankRepository bankRepository;
    private final BranchRepository branchRepository;

    public AccountService(AccountRepository accountRepository,
                          UserRepository userRepository,
                          BankRepository bankRepository,
                          BranchRepository branchRepository) {

        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.bankRepository = bankRepository;
        this.branchRepository = branchRepository;
    }

    // =========================
    // CREATE ACCOUNT
    // =========================
    public AccountResponseDTO createAccount(AccountRequestDTO request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Bank bank = bankRepository.findById(request.getBankId())
                .orElseThrow(() -> new RuntimeException("Bank not found"));

        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new RuntimeException("Branch not found"));

        Account account = new Account();

        account.setAccountNumber(
                generateAccountNumber(request.getAccountType())
        );

        account.setAccountType(request.getAccountType());

        account.setBalance(request.getInitialBalance());

        account.setUser(user);
        account.setBank(bank);
        account.setBranch(branch);

        // Account entity ke @PrePersist mein
        // status automatically ACTIVE ho jayega.

        Account saved = accountRepository.save(account);

        return AccountMapper.toResponseDTO(saved);
    }


    // =========================
    // GET ACCOUNT BY ID
    // =========================
//    public AccountResponseDTO getAccountById(Long id) {
//
//        Account account = accountRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Account not found"));
//
//        return AccountMapper.toResponseDTO(account);
//    }
    @Cacheable(value = "accounts", key = "#id")
    public AccountResponseDTO getAccountById(Long id) {

        System.out.println("Fetching account from MySQL...");

        Account account = accountRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account not found with id " + id));

        return AccountMapper.toResponseDTO(account);
    }


    // =========================
    // GET ALL ACCOUNTS
    // =========================
    public List<AccountResponseDTO> getAllAccounts() {

        return accountRepository.findAll()
                .stream()
                .map(AccountMapper::toResponseDTO)
                .toList();
    }


    // =========================
    // UPDATE ACCOUNT
    // =========================

    public AccountResponseDTO updateAccount(Long id,
                                            AccountRequestDTO request) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Bank bank = bankRepository.findById(request.getBankId())
                .orElseThrow(() -> new RuntimeException("Bank not found"));

        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new RuntimeException("Branch not found"));

        account.setAccountType(request.getAccountType());

        account.setBalance(request.getInitialBalance());

        account.setUser(user);

        account.setBank(bank);

        account.setBranch(branch);

        Account updated = accountRepository.save(account);

        return AccountMapper.toResponseDTO(updated);
    }


    // =========================
    // DELETE ACCOUNT
    // =========================
    public void deleteAccount(Long id) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        accountRepository.delete(account);
    }


    // =========================
    // ACCOUNT NUMBER GENERATOR
    // =========================
    private String generateAccountNumber(AccountType accountType) {

        String prefix;

        if (accountType == AccountType.SAVINGS) {
            prefix = "SB";
        } else {
            prefix = "CA";
        }

        return prefix +
                UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0, 10)
                        .toUpperCase();
    }

    public Page<AccountResponseDTO> searchAccounts(
            String keyword,
            Pageable pageable) {

        return accountRepository
                .searchAccounts(keyword, pageable)
                .map(AccountMapper::toResponseDTO);
    }
}