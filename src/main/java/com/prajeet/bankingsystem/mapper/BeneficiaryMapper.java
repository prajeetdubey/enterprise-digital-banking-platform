package com.prajeet.bankingsystem.mapper;

import com.prajeet.bankingsystem.dto.BeneficiaryResponseDTO;
import com.prajeet.bankingsystem.entity.Beneficiary;

public class BeneficiaryMapper {

    private BeneficiaryMapper() {
    }

    public static BeneficiaryResponseDTO toResponseDTO(
            Beneficiary beneficiary) {

        return BeneficiaryResponseDTO.builder()
                .id(beneficiary.getId())

                .userId(
                        beneficiary.getUser().getId()
                )

                .beneficiaryAccountId(
                        beneficiary.getBeneficiaryAccount().getId()
                )

                .beneficiaryAccountNumber(
                        beneficiary.getBeneficiaryAccount()
                                .getAccountNumber()
                )

                .nickname(
                        beneficiary.getNickname()
                )

                .status(
                        beneficiary.getStatus()
                )

                .createdAt(
                        beneficiary.getCreatedAt()
                )

                .verifiedAt(
                        beneficiary.getVerifiedAt()
                )

                .build();
    }
}