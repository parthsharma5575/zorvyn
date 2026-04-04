package com.zorvyn.repository;

import com.zorvyn.model.Transaction;
import com.zorvyn.model.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findByUserId(UUID userId);
    List<Transaction> findByUserIdAndType(UUID userId, TransactionType type);
    List<Transaction> findByUserIdAndCategory(UUID userId, String category);
    List<Transaction> findByUserIdAndDateBetween(UUID userId, LocalDate startDate, LocalDate endDate);
    Page<Transaction> findByUserId(UUID userId, Pageable pageable);
}
