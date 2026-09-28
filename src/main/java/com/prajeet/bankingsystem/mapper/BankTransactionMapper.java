package com.prajeet.bankingsystem.mapper;

import com.prajeet.bankingsystem.dto.BankTransactionResponseDTO;
import com.prajeet.bankingsystem.entity.BankTransaction;

public class BankTransactionMapper {

    private BankTransactionMapper() {
        // utility class, no instances
    }

    public static BankTransactionResponseDTO toResponseDTO(BankTransaction transaction) {

        if (transaction == null) {
            return null;
        }

        return new BankTransactionResponseDTO(
                transaction.getId(),
                transaction.getType(),
                transaction.getAmount(),
                transaction.getSourceAccount() != null
                        ? transaction.getSourceAccount().getAccountNumber()
                        : null,
                transaction.getDestinationAccount() != null
                        ? transaction.getDestinationAccount().getAccountNumber()
                        : null,
                transaction.getCreatedAt()
        );
    }
}