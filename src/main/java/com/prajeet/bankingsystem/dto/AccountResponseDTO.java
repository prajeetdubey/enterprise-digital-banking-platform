package com.prajeet.bankingsystem.dto;

import com.prajeet.bankingsystem.enums.AccountStatus;
import com.prajeet.bankingsystem.enums.AccountType;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountResponseDTO {

    private Long id;

    private String accountNumber;

    private AccountType accountType;

    private BigDecimal balance;

    private AccountStatus status;

    private Long userId;

    private String userName;

    private Long bankId;

    private String bankName;

    private Long branchId;

    private String branchName;
}