package com.prajeet.bankingsystem.repository;

import com.prajeet.bankingsystem.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepository
        extends JpaRepository<Account, Long> {

    Optional<Account> findByAccountNumber(String accountNumber);

    @Query("""
            SELECT a
            FROM Account a
            JOIN a.user u
            WHERE LOWER(a.accountNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(u.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    Page<Account> searchAccounts(
            @Param("keyword") String keyword,
            Pageable pageable
    );
}