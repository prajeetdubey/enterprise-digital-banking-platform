package com.prajeet.bankingsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BeneficiaryRequestDTO {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Beneficiary account ID is required")
    private Long beneficiaryAccountId;

    @NotBlank(message = "Nickname is required")
    private String nickname;
}