package com.prajeet.bankingsystem.repository;

import com.prajeet.bankingsystem.entity.Beneficiary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BeneficiaryRepository
        extends JpaRepository<Beneficiary, Long> {

    List<Beneficiary> findByUser_Id(Long userId);

    boolean existsByUser_IdAndBeneficiaryAccount_Id(
            Long userId,
            Long beneficiaryAccountId
    );
}