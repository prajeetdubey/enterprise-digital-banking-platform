package com.prajeet.bankingsystem.service;

import com.prajeet.bankingsystem.dto.BeneficiaryRequestDTO;
import com.prajeet.bankingsystem.dto.BeneficiaryResponseDTO;
import com.prajeet.bankingsystem.entity.Account;
import com.prajeet.bankingsystem.entity.Beneficiary;
import com.prajeet.bankingsystem.entity.User;
import com.prajeet.bankingsystem.enums.AccountStatus;
import com.prajeet.bankingsystem.enums.BeneficiaryStatus;
import com.prajeet.bankingsystem.mapper.BeneficiaryMapper;
import com.prajeet.bankingsystem.repository.AccountRepository;
import com.prajeet.bankingsystem.repository.BeneficiaryRepository;
import com.prajeet.bankingsystem.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BeneficiaryService {

    private final BeneficiaryRepository beneficiaryRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    public BeneficiaryService(
            BeneficiaryRepository beneficiaryRepository,
            UserRepository userRepository,
            AccountRepository accountRepository) {

        this.beneficiaryRepository = beneficiaryRepository;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }

    // =========================
    // ADD BENEFICIARY
    // =========================
    @Transactional
    public BeneficiaryResponseDTO addBeneficiary(
            BeneficiaryRequestDTO request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Account beneficiaryAccount =
                accountRepository.findById(
                        request.getBeneficiaryAccountId()
                ).orElseThrow(() ->
                        new RuntimeException(
                                "Beneficiary account not found"
                        ));

        if (beneficiaryAccount.getStatus() != AccountStatus.ACTIVE) {
            throw new RuntimeException(
                    "Beneficiary account is not active"
            );
        }

        // User cannot add his own account as beneficiary
        if (beneficiaryAccount.getUser().getId()
                .equals(user.getId())) {

            throw new RuntimeException(
                    "You cannot add your own account as beneficiary"
            );
        }

        boolean alreadyExists =
                beneficiaryRepository
                        .existsByUser_IdAndBeneficiaryAccount_Id(
                                user.getId(),
                                beneficiaryAccount.getId()
                        );

        if (alreadyExists) {
            throw new RuntimeException(
                    "Beneficiary already exists"
            );
        }

        Beneficiary beneficiary = Beneficiary.builder()
                .user(user)
                .beneficiaryAccount(beneficiaryAccount)
                .nickname(request.getNickname())
                .status(BeneficiaryStatus.PENDING)
                .build();

        Beneficiary saved =
                beneficiaryRepository.save(beneficiary);

        return BeneficiaryMapper.toResponseDTO(saved);
    }


    // =========================
    // GET USER BENEFICIARIES
    // =========================
    public List<BeneficiaryResponseDTO> getUserBeneficiaries(
            Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }

        return beneficiaryRepository
                .findByUser_Id(userId)
                .stream()
                .map(BeneficiaryMapper::toResponseDTO)
                .toList();
    }


    // =========================
    // VERIFY BENEFICIARY
    // =========================
    @Transactional
    public BeneficiaryResponseDTO verifyBeneficiary(Long id) {

        Beneficiary beneficiary =
                beneficiaryRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Beneficiary not found"
                                ));

        if (beneficiary.getStatus()
                == BeneficiaryStatus.VERIFIED) {

            throw new RuntimeException(
                    "Beneficiary is already verified"
            );
        }

        beneficiary.setStatus(
                BeneficiaryStatus.VERIFIED
        );

        beneficiary.setVerifiedAt(
                LocalDateTime.now()
        );

        Beneficiary updated =
                beneficiaryRepository.save(beneficiary);

        return BeneficiaryMapper.toResponseDTO(updated);
    }


    // =========================
    // REMOVE BENEFICIARY
    // =========================
    @Transactional
    public void removeBeneficiary(Long id) {

        Beneficiary beneficiary =
                beneficiaryRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Beneficiary not found"
                                ));

        beneficiaryRepository.delete(beneficiary);
    }
}