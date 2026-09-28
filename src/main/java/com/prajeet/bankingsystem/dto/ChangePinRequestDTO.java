package com.prajeet.bankingsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePinRequestDTO {

    @NotBlank(message = "Old PIN is required")
    @Pattern(
            regexp = "\\d{4}",
            message = "Old PIN must be exactly 4 digits"
    )
    private String oldPin;

    @NotBlank(message = "New PIN is required")
    @Pattern(
            regexp = "\\d{4}",
            message = "New PIN must be exactly 4 digits"
    )
    private String newPin;
}