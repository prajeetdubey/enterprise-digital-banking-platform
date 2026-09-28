package com.prajeet.bankingsystem.repository;

import com.prajeet.bankingsystem.entity.BankTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository
        extends JpaRepository<BankTransaction, Long> {

    List<BankTransaction>
    findBySourceAccount_IdOrDestinationAccount_IdOrderByCreatedAtDesc(
            Long sourceAccountId,
            Long destinationAccountId
    );
}