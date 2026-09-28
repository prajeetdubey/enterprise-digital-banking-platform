package com.prajeet.bankingsystem.mapper;

import com.prajeet.bankingsystem.dto.BranchResponseDTO;
import com.prajeet.bankingsystem.entity.Branch;

public class BranchMapper {

    public static BranchResponseDTO toResponseDTO(Branch branch) {

        BranchResponseDTO response = new BranchResponseDTO();

        response.setId(branch.getId());
        response.setName(branch.getName());
        response.setBranchCode(branch.getBranchCode());
        response.setCity(branch.getCity());
        response.setAddress(branch.getAddress());

        response.setBankId(branch.getBank().getId());
        response.setBankName(branch.getBank().getBankName());

        return response;
    }
}