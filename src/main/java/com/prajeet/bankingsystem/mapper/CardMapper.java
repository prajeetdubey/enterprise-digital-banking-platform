package com.prajeet.bankingsystem.mapper;

import com.prajeet.bankingsystem.dto.CardResponseDTO;
import com.prajeet.bankingsystem.entity.Card;

public class CardMapper {

    private CardMapper() {
    }

    public static CardResponseDTO toResponseDTO(Card card) {

        String maskedCardNumber =
                "**** **** **** "
                        + card.getCardNumber()
                        .substring(
                                card.getCardNumber().length() - 4
                        );

        return CardResponseDTO.builder()
                .id(card.getId())
                .cardNumber(maskedCardNumber)
                .cardType(card.getCardType())
                .status(card.getStatus())
                .expiryDate(card.getExpiryDate())
                .accountId(card.getAccount().getId())
                .accountNumber(
                        card.getAccount().getAccountNumber()
                )
                .build();
    }
}