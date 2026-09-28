package com.prajeet.bankingsystem.dto;

import com.prajeet.bankingsystem.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BankTransactionResponseDTO {

    private Long id;
    private TransactionType type;
    private BigDecimal amount;
    private String sourceAccountNumber;
    private String destinationAccountNumber;
    private LocalDateTime createdAt;

    public BankTransactionResponseDTO() {
    }

    public BankTransactionResponseDTO(Long id,
                                      TransactionType type,
                                      BigDecimal amount,
                                      String sourceAccountNumber,
                                      String destinationAccountNumber,
                                      LocalDateTime createdAt) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.sourceAccountNumber = sourceAccountNumber;
        this.destinationAccountNumber = destinationAccountNumber;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getSourceAccountNumber() {
        return sourceAccountNumber;
    }

    public void setSourceAccountNumber(String sourceAccountNumber) {
        this.sourceAccountNumber = sourceAccountNumber;
    }

    public String getDestinationAccountNumber() {
        return destinationAccountNumber;
    }

    public void setDestinationAccountNumber(String destinationAccountNumber) {
        this.destinationAccountNumber = destinationAccountNumber;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}