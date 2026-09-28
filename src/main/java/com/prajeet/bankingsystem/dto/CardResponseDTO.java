package com.prajeet.bankingsystem.dto;

import com.prajeet.bankingsystem.enums.CardStatus;
import com.prajeet.bankingsystem.enums.CardType;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardResponseDTO {

    private Long id;

    private String cardNumber;

    private CardType cardType;

    private CardStatus status;

    private LocalDate expiryDate;

    private Long accountId;

    private String accountNumber;
}