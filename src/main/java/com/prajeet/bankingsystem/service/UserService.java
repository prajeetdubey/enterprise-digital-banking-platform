package com.prajeet.bankingsystem.service;

import com.prajeet.bankingsystem.dto.UserRequestDTO;
import com.prajeet.bankingsystem.dto.UserResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    UserResponseDTO createUser(UserRequestDTO userRequestDTO);

    List<UserResponseDTO> getAllUsers();

    UserResponseDTO getUserById(Long id);

    UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO);

    void deleteUser(Long id);

    // Phase 6 - Pagination
    Page<UserResponseDTO> getUsersPaginated(Pageable pageable);
    Page<UserResponseDTO> getUsersSorted(
            int page,
            int size,
            String sortBy,
            String direction
    );
    Page<UserResponseDTO> searchUsers(
            String keyword,
            Pageable pageable
    );
}