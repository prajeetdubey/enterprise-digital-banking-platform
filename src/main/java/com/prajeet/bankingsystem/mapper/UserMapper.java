//package com.prajeet.bankingsystem.mapper;
//
//import com.prajeet.bankingsystem.dto.UserRequestDTO;
//import com.prajeet.bankingsystem.dto.UserResponseDTO;
//import com.prajeet.bankingsystem.entity.User;
//import com.prajeet.bankingsystem.enums.Role;
//
//public class UserMapper {
//
//    public static User toEntity(UserRequestDTO dto) {
//
//        User user = new User();
//
//        user.setFirstName(dto.getFirstName());
//        user.setLastName(dto.getLastName());
//        user.setEmail(dto.getEmail());
//        user.setPhoneNumber(dto.getPhoneNumber());
//        user.setDateOfBirth(dto.getDateOfBirth());
//        user.setAddress(dto.getAddress());
//        user.setPassword(dto.getPassword());
//
//        // Default Role
//        if (dto.getRole() == null) {
//            user.setRole(Role.USER);
//        } else {
//            user.setRole(dto.getRole());
//        }
//
//        return user;
//    }
//
//    public static UserResponseDTO toResponseDTO(User user) {
//
//        UserResponseDTO dto = new UserResponseDTO();
//
//        dto.setId(user.getId());
//        dto.setFirstName(user.getFirstName());
//        dto.setLastName(user.getLastName());
//        dto.setEmail(user.getEmail());
//        dto.setPhoneNumber(user.getPhoneNumber());
//        dto.setDateOfBirth(user.getDateOfBirth());
//        dto.setAddress(user.getAddress());
//        dto.setRole(user.getRole());
//        dto.setCreatedAt(user.getCreatedAt());
//        dto.setUpdatedAt(user.getUpdatedAt());
//
//        // Never expose password
//        return dto;
//    }
//}
package com.prajeet.bankingsystem.mapper;

import com.prajeet.bankingsystem.dto.UserRequestDTO;
import com.prajeet.bankingsystem.dto.UserResponseDTO;
import com.prajeet.bankingsystem.entity.User;
import com.prajeet.bankingsystem.enums.Role;

import java.time.format.DateTimeFormatter;

public class UserMapper {

    // ⚠️ NAYA — date/time ko String mein format karne ke liye
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final DateTimeFormatter DATE_TIME_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static User toEntity(UserRequestDTO dto) {

        User user = new User();

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setDateOfBirth(dto.getDateOfBirth());
        user.setAddress(dto.getAddress());
        user.setPassword(dto.getPassword());

        // Default Role
        if (dto.getRole() == null) {
            user.setRole(Role.USER);
        } else {
            user.setRole(dto.getRole());
        }

        return user;
    }

    public static UserResponseDTO toResponseDTO(User user) {

        UserResponseDTO dto = new UserResponseDTO();

        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setAddress(user.getAddress());
        dto.setRole(user.getRole());

        // ⚠️ CHANGE — LocalDate/LocalDateTime ko String mein convert kar rahe hain
        dto.setDateOfBirth(
                user.getDateOfBirth() != null
                        ? user.getDateOfBirth().format(DATE_FORMAT)
                        : null
        );

        dto.setCreatedAt(
                user.getCreatedAt() != null
                        ? user.getCreatedAt().format(DATE_TIME_FORMAT)
                        : null
        );

        dto.setUpdatedAt(
                user.getUpdatedAt() != null
                        ? user.getUpdatedAt().format(DATE_TIME_FORMAT)
                        : null
        );

        // Never expose password
        return dto;
    }
}