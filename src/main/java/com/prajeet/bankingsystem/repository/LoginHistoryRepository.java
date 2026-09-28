package com.prajeet.bankingsystem.repository;

import com.prajeet.bankingsystem.entity.LoginHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {

    Page<LoginHistory> findByEmailContainingIgnoreCase(String email, Pageable pageable);

    Page<LoginHistory> findBySuccess(boolean success, Pageable pageable);
}