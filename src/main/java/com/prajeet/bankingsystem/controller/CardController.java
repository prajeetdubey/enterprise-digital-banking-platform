package com.prajeet.bankingsystem.controller;

import com.prajeet.bankingsystem.dto.CardRequestDTO;
import com.prajeet.bankingsystem.dto.CardResponseDTO;
import com.prajeet.bankingsystem.dto.ChangePinRequestDTO;
import com.prajeet.bankingsystem.service.CardService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }


    // =========================
    // CREATE CARD
    // =========================
    @PostMapping
    public ResponseEntity<CardResponseDTO> createCard(
            @Valid @RequestBody CardRequestDTO request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(cardService.createCard(request));
    }


    // =========================
    // GET ACCOUNT CARDS
    // =========================
    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<CardResponseDTO>>
    getCardsByAccount(
            @PathVariable Long accountId) {

        return ResponseEntity.ok(
                cardService.getCardsByAccount(accountId)
        );
    }


    // =========================
    // BLOCK CARD
    // =========================
    @PutMapping("/{id}/block")
    public ResponseEntity<CardResponseDTO>
    blockCard(@PathVariable Long id) {

        return ResponseEntity.ok(
                cardService.blockCard(id)
        );
    }


    // =========================
    // UNBLOCK CARD
    // =========================
    @PutMapping("/{id}/unblock")
    public ResponseEntity<CardResponseDTO>
    unblockCard(@PathVariable Long id) {

        return ResponseEntity.ok(
                cardService.unblockCard(id)
        );
    }


    // =========================
    // CHANGE PIN
    // =========================
    @PutMapping("/{id}/pin")
    public ResponseEntity<String> changePin(
            @PathVariable Long id,
            @Valid @RequestBody ChangePinRequestDTO request) {

        cardService.changePin(
                id,
                request.getOldPin(),
                request.getNewPin()
        );

        return ResponseEntity.ok(
                "PIN changed successfully"
        );
    }
}