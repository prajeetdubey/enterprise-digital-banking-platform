package com.prajeet.bankingsystem.entity;

import com.prajeet.bankingsystem.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    /*
     * Account from which money is deducted.
     *
     * For:
     * DEPOSIT  -> null
     * WITHDRAW -> account
     * TRANSFER -> sender
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_account_id")
    private Account sourceAccount;

    /*
     * Account to which money is added.
     *
     * For:
     * DEPOSIT  -> account
     * WITHDRAW -> null
     * TRANSFER -> receiver
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_account_id")
    private Account destinationAccount;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}