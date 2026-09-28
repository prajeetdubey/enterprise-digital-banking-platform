package com.prajeet.bankingsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RefreshTokenResponseDTO {

    private String accessToken;
    private String refreshToken;
}