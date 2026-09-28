package com.prajeet.bankingsystem.repository;

import com.prajeet.bankingsystem.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepository
        extends JpaRepository<Card, Long> {

    List<Card> findByAccount_Id(Long accountId);

    boolean existsByAccount_IdAndCardType(
            Long accountId,
            com.prajeet.bankingsystem.enums.CardType cardType
    );
}