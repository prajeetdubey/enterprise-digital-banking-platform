package com.prajeet.bankingsystem.entity;

import com.prajeet.bankingsystem.enums.BeneficiaryStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "beneficiaries",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"user_id", "beneficiary_account_id"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Beneficiary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // User who added the beneficiary
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Account which belongs to the beneficiary
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "beneficiary_account_id", nullable = false)
    private Account beneficiaryAccount;

    @Column(nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BeneficiaryStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime verifiedAt;

    @PrePersist
    public void onCreate() {

        this.createdAt = LocalDateTime.now();

        if (this.status == null) {
            this.status = BeneficiaryStatus.PENDING;
        }
    }
}