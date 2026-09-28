package com.prajeet.bankingsystem.mapper;

import com.prajeet.bankingsystem.dto.AccountResponseDTO;
import com.prajeet.bankingsystem.entity.Account;

public class AccountMapper {

    public static AccountResponseDTO toResponseDTO(Account account) {

        AccountResponseDTO response = new AccountResponseDTO();

        response.setId(account.getId());
        response.setAccountNumber(account.getAccountNumber());
        response.setAccountType(account.getAccountType());
        response.setBalance(account.getBalance());
        response.setStatus(account.getStatus());

        response.setUserId(account.getUser().getId());
        response.setUserName(
                account.getUser().getFirstName() + " " +
                        account.getUser().getLastName()
        );

        response.setBankId(account.getBank().getId());
        response.setBankName(account.getBank().getBankName());

        response.setBranchId(account.getBranch().getId());
        response.setBranchName(account.getBranch().getName());

        return response;
    }

}