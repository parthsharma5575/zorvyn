package com.zorvyn.dto.response;

import com.zorvyn.model.enums.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionResponseDto (
     UUID id,
     BigDecimal amount,
     TransactionType type,
     String category,
     String description,
     LocalDate date,
     String username,
     LocalDateTime createdAt
){}