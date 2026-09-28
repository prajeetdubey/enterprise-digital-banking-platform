package com.prajeet.bankingsystem.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankRequestDTO {

    @NotBlank(message = "Bank name is required")
    private String bankName;

}