package com.prajeet.bankingsystem.service;

import com.prajeet.bankingsystem.dto.CardRequestDTO;
import com.prajeet.bankingsystem.dto.CardResponseDTO;
import com.prajeet.bankingsystem.entity.Account;
import com.prajeet.bankingsystem.entity.Card;
import com.prajeet.bankingsystem.enums.AccountStatus;
import com.prajeet.bankingsystem.enums.CardStatus;
import com.prajeet.bankingsystem.enums.CardType;
import com.prajeet.bankingsystem.mapper.CardMapper;
import com.prajeet.bankingsystem.repository.AccountRepository;
import com.prajeet.bankingsystem.repository.CardRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class CardService {

    private final CardRepository cardRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public CardService(
            CardRepository cardRepository,
            AccountRepository accountRepository,
            PasswordEncoder passwordEncoder) {

        this.cardRepository = cardRepository;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }


    // =========================
    // CREATE CARD
    // =========================
    @Transactional
    public CardResponseDTO createCard(
            CardRequestDTO request) {

        Account account =
                accountRepository.findById(
                        request.getAccountId()
                ).orElseThrow(() ->
                        new RuntimeException(
                                "Account not found"
                        ));

        if (account.getStatus() != AccountStatus.ACTIVE) {

            throw new RuntimeException(
                    "Cannot create card for inactive account"
            );
        }

        boolean cardAlreadyExists =
                cardRepository
                        .existsByAccount_IdAndCardType(
                                account.getId(),
                                request.getCardType()
                        );

        if (cardAlreadyExists) {

            throw new RuntimeException(
                    request.getCardType()
                            + " card already exists for this account"
            );
        }

        Card card = Card.builder()
                .cardNumber(generateCardNumber())
                .cardType(request.getCardType())
                .status(CardStatus.ACTIVE)
                .expiryDate(
                        LocalDate.now().plusYears(5)
                )
                .pinHash(
                        passwordEncoder.encode(
                                request.getPin()
                        )
                )
                .account(account)
                .build();

        Card saved = cardRepository.save(card);

        return CardMapper.toResponseDTO(saved);
    }


    // =========================
    // GET CARDS OF ACCOUNT
    // =========================
    public List<CardResponseDTO> getCardsByAccount(
            Long accountId) {

        if (!accountRepository.existsById(accountId)) {

            throw new RuntimeException(
                    "Account not found"
            );
        }

        return cardRepository
                .findByAccount_Id(accountId)
                .stream()
                .map(CardMapper::toResponseDTO)
                .toList();
    }


    // =========================
    // BLOCK CARD
    // =========================
    @Transactional
    public CardResponseDTO blockCard(Long id) {

        Card card =
                cardRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Card not found"
                                ));

        if (card.getStatus() == CardStatus.BLOCKED) {

            throw new RuntimeException(
                    "Card is already blocked"
            );
        }

        card.setStatus(CardStatus.BLOCKED);

        Card updated =
                cardRepository.save(card);

        return CardMapper.toResponseDTO(updated);
    }


    // =========================
    // UNBLOCK CARD
    // =========================
    @Transactional
    public CardResponseDTO unblockCard(Long id) {

        Card card =
                cardRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Card not found"
                                ));

        if (card.getStatus() == CardStatus.ACTIVE) {

            throw new RuntimeException(
                    "Card is already active"
            );
        }

        card.setStatus(CardStatus.ACTIVE);

        Card updated =
                cardRepository.save(card);

        return CardMapper.toResponseDTO(updated);
    }


    // =========================
    // CHANGE PIN
    // =========================
    @Transactional
    public void changePin(
            Long id,
            String oldPin,
            String newPin) {

        Card card =
                cardRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Card not found"
                                ));

        if (card.getStatus() == CardStatus.BLOCKED) {

            throw new RuntimeException(
                    "Cannot change PIN of blocked card"
            );
        }

        boolean oldPinCorrect =
                passwordEncoder.matches(
                        oldPin,
                        card.getPinHash()
                );

        if (!oldPinCorrect) {

            throw new RuntimeException(
                    "Old PIN is incorrect"
            );
        }

        if (!newPin.matches("\\d{4}")) {

            throw new RuntimeException(
                    "New PIN must be exactly 4 digits"
            );
        }

        card.setPinHash(
                passwordEncoder.encode(newPin)
        );

        cardRepository.save(card);
    }


    // =========================
    // CARD NUMBER GENERATOR
    // =========================
    private String generateCardNumber() {

        String uuid =
                UUID.randomUUID()
                        .toString()
                        .replace("-", "");

        return uuid
                .substring(0, 16)
                .toUpperCase();
    }
}