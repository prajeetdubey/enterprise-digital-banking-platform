package com.prajeet.bankingsystem.dto;

import com.prajeet.bankingsystem.enums.CardType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardRequestDTO {

    @NotNull(message = "Account ID is required")
    private Long accountId;

    @NotNull(message = "Card type is required")
    private CardType cardType;

    @NotBlank(message = "PIN is required")
    @Pattern(
            regexp = "\\d{4}",
            message = "PIN must be exactly 4 digits"
    )
    private String pin;
}