//package com.prajeet.bankingsystem.dto;
//
//import lombok.Data;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//
//import com.prajeet.bankingsystem.enums.Role;
//
//@Data
//public class UserResponseDTO {
//
//    private Long id;
//
//    private String firstName;
//
//    private String lastName;
//
//    private String email;
//
//    private String phoneNumber;
//
//    private LocalDate dateOfBirth;
//
//    private String address;
//
//    private LocalDateTime createdAt;
//
//    private LocalDateTime updatedAt;
//
//    private Role role;
//}
package com.prajeet.bankingsystem.dto;

import lombok.Data;

import com.prajeet.bankingsystem.enums.Role;

@Data
public class UserResponseDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    // ⚠️ CHANGE: LocalDate ki jagah String — Jackson version conflict avoid karne ke liye
    private String dateOfBirth;

    private String address;

    // ⚠️ CHANGE: LocalDateTime ki jagah String
    private String createdAt;

    // ⚠️ CHANGE: LocalDateTime ki jagah String
    private String updatedAt;

    private Role role;
}