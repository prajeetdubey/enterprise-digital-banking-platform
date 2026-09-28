package com.prajeet.bankingsystem.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BranchResponseDTO {

    private Long id;

    private String name;

    private String branchCode;

    private String city;

    private String address;

    private Long bankId;

    private String bankName;
}