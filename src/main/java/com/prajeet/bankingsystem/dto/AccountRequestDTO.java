package com.prajeet.bankingsystem.dto;

import com.prajeet.bankingsystem.enums.AccountType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AccountRequestDTO {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Bank ID is required")
    private Long bankId;

    @NotNull(message = "Branch ID is required")
    private Long branchId;

    @NotNull(message = "Account type is required")
    private AccountType accountType;

    @NotNull(message = "Initial balance is required")
    @DecimalMin(value = "0.0", inclusive = true,
            message = "Initial balance cannot be negative")
    private BigDecimal initialBalance;
}