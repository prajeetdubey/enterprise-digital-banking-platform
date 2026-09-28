package com.prajeet.bankingsystem.repository;

import com.prajeet.bankingsystem.entity.BankTransaction;
import com.prajeet.bankingsystem.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface BankTransactionRepository
        extends JpaRepository<BankTransaction, Long> {

    // ⚠️ Yahan apni existing repository methods (agar koi hain) waise hi rehne dena.
    // Sirf neeche wala naya method add karna hai.

    @Query("""
            SELECT t
            FROM BankTransaction t
            WHERE
                (
                    :accountId IS NULL
                    OR t.sourceAccount.id = :accountId
                    OR t.destinationAccount.id = :accountId
                )
            AND (
                    :transactionType IS NULL
                    OR t.type = :transactionType
                )
            AND (
                    :minAmount IS NULL
                    OR t.amount >= :minAmount
                )
            AND (
                    :maxAmount IS NULL
                    OR t.amount <= :maxAmount
                )
            """)
    Page<BankTransaction> searchTransactions(
            @Param("accountId") Long accountId,
            @Param("transactionType") TransactionType transactionType,
            @Param("minAmount") BigDecimal minAmount,
            @Param("maxAmount") BigDecimal maxAmount,
            Pageable pageable
    );
}