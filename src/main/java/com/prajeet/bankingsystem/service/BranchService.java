package com.prajeet.bankingsystem.service;

import com.prajeet.bankingsystem.dto.BranchRequestDTO;
import com.prajeet.bankingsystem.dto.BranchResponseDTO;
import com.prajeet.bankingsystem.entity.Bank;
import com.prajeet.bankingsystem.entity.Branch;
import com.prajeet.bankingsystem.mapper.BranchMapper;
import com.prajeet.bankingsystem.repository.BankRepository;
import com.prajeet.bankingsystem.repository.BranchRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BranchService {

    private final BranchRepository branchRepository;
    private final BankRepository bankRepository;

    public BranchService(BranchRepository branchRepository,
                         BankRepository bankRepository) {

        this.branchRepository = branchRepository;
        this.bankRepository = bankRepository;
    }

    // CREATE BRANCH
    public BranchResponseDTO createBranch(BranchRequestDTO request) {

        // Find Bank
        Bank bank = bankRepository.findById(request.getBankId())
                .orElseThrow(() ->
                        new RuntimeException("Bank not found"));

        // Create Branch
        Branch branch = new Branch();

        branch.setName(request.getName());
        branch.setBranchCode(request.getBranchCode());
        branch.setCity(request.getCity());
        branch.setAddress(request.getAddress());

        // Relationship
        branch.setBank(bank);

        // Save
        Branch savedBranch = branchRepository.save(branch);

        // Response
        return BranchMapper.toResponseDTO(savedBranch);
    }
    public BranchResponseDTO getBranchById(Long id) {

        Branch branch = branchRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Branch not found"));

        return BranchMapper.toResponseDTO(branch);
    }
    public List<BranchResponseDTO> getAllBranches() {

        return branchRepository.findAll()
                .stream()
                .map(BranchMapper::toResponseDTO)
                .toList();
    }
    public BranchResponseDTO updateBranch(Long id,
                                          BranchRequestDTO request) {

        Branch branch = branchRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Branch not found"));

        Bank bank = bankRepository.findById(request.getBankId())
                .orElseThrow(() ->
                        new RuntimeException("Bank not found"));

        branch.setName(request.getName());
        branch.setBranchCode(request.getBranchCode());
        branch.setCity(request.getCity());
        branch.setAddress(request.getAddress());

        branch.setBank(bank);

        Branch updatedBranch = branchRepository.save(branch);

        return BranchMapper.toResponseDTO(updatedBranch);
    }
    public void deleteBranch(Long id) {

        Branch branch = branchRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Branch not found"));

        branchRepository.delete(branch);
    }
}