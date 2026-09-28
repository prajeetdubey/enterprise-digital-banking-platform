package com.prajeet.bankingsystem.service;

import com.prajeet.bankingsystem.dto.UserRequestDTO;
import com.prajeet.bankingsystem.dto.UserResponseDTO;
import com.prajeet.bankingsystem.entity.User;
import com.prajeet.bankingsystem.enums.Role;
import com.prajeet.bankingsystem.exception.ResourceNotFoundException;
import com.prajeet.bankingsystem.mapper.UserMapper;
import com.prajeet.bankingsystem.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.CacheEvict;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {

        User user = UserMapper.toEntity(userRequestDTO);

        // Encrypt Password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Default Role
        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }

        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        return UserMapper.toResponseDTO(savedUser);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(UserMapper::toResponseDTO)
                .toList();
    }

//    @Override
//    public UserResponseDTO getUserById(Long id) {
//
//        User user = userRepository.findById(id)
//                .orElseThrow(() ->
//                        new ResourceNotFoundException(
//                                "User not found with id " + id
//                        ));
//
//        return UserMapper.toResponseDTO(user);
//    }
@Override
@Cacheable(value = "users", key = "#id")
public UserResponseDTO getUserById(Long id) {

    System.out.println("Fetching user from MySQL...");

    User user = userRepository.findById(id)
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "User not found with id " + id));

    return UserMapper.toResponseDTO(user);
}

//    @Override
//    public UserResponseDTO updateUser(Long id,
//                                      UserRequestDTO userRequestDTO) {
@Override
@CachePut(value = "users", key = "#id")
public UserResponseDTO updateUser(
        Long id,
        UserRequestDTO userRequestDTO) {

        User existingUser = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id " + id
                        ));

        existingUser.setFirstName(userRequestDTO.getFirstName());
        existingUser.setLastName(userRequestDTO.getLastName());
        existingUser.setEmail(userRequestDTO.getEmail());
        existingUser.setPhoneNumber(userRequestDTO.getPhoneNumber());
        existingUser.setDateOfBirth(userRequestDTO.getDateOfBirth());
        existingUser.setAddress(userRequestDTO.getAddress());
        existingUser.setRole(userRequestDTO.getRole());

        // Encrypt password only if provided
        if (userRequestDTO.getPassword() != null &&
                !userRequestDTO.getPassword().isBlank()) {

            existingUser.setPassword(
                    passwordEncoder.encode(userRequestDTO.getPassword())
            );
        }

        existingUser.setUpdatedAt(LocalDateTime.now());

        User updatedUser = userRepository.save(existingUser);

        return UserMapper.toResponseDTO(updatedUser);
    }

//    @Override
//    public void deleteUser(Long id) {
@Override
@CacheEvict(value = "users", key = "#id")
public void deleteUser(Long id) {

        User existingUser = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id " + id
                        ));

        userRepository.delete(existingUser);
    }

    // =========================================================
    // Phase 6 - Pagination
    // =========================================================

    @Override
    public Page<UserResponseDTO> getUsersPaginated(Pageable pageable) {

        return userRepository.findAll(pageable)
                .map(UserMapper::toResponseDTO);
    }
    @Override
    public Page<UserResponseDTO> getUsersSorted(
            int page,
            int size,
            String sortBy,
            String direction) {

        Sort.Direction sortDirection =
                direction.equalsIgnoreCase("desc")
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sortDirection, sortBy)
        );

        return userRepository.findAll(pageable)
                .map(UserMapper::toResponseDTO);
    }
    @Override
    public Page<UserResponseDTO> searchUsers(
            String keyword,
            Pageable pageable) {

        return userRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                        keyword,
                        keyword,
                        keyword,
                        pageable
                )
                .map(UserMapper::toResponseDTO);
    }
}