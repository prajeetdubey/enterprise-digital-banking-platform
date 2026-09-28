package com.prajeet.bankingsystem.dto;

import com.prajeet.bankingsystem.enums.BeneficiaryStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeneficiaryResponseDTO {

    private Long id;

    private Long userId;

    private Long beneficiaryAccountId;

    private String beneficiaryAccountNumber;

    private String nickname;

    private BeneficiaryStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime verifiedAt;
}