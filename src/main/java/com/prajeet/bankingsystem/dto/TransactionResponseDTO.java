package com.prajeet.bankingsystem.dto;

import com.prajeet.bankingsystem.enums.TransactionType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class TransactionResponseDTO {

    private Long id;

    private TransactionType type;

    private BigDecimal amount;

    private String sourceAccountNumber;

    private String destinationAccountNumber;

    private LocalDateTime createdAt;
}