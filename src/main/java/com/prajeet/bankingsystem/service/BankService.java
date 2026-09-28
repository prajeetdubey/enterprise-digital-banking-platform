package com.prajeet.bankingsystem.service;

import com.prajeet.bankingsystem.dto.BankRequestDTO;
import com.prajeet.bankingsystem.dto.BankResponseDTO;
import com.prajeet.bankingsystem.entity.Bank;
import com.prajeet.bankingsystem.repository.BankRepository;
import org.springframework.stereotype.Service;

@Service
public class BankService {

    private final BankRepository bankRepository;

    public BankService(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    public BankResponseDTO createBank(BankRequestDTO request) {

        if (bankRepository.findByBankName(request.getBankName()).isPresent()) {
            throw new RuntimeException("Bank already exists.");
        }

        Bank bank = new Bank();

        bank.setBankName(request.getBankName());

        Bank savedBank = bankRepository.save(bank);

        return new BankResponseDTO(
                savedBank.getId(),
                savedBank.getBankName()
        );
    }

}