package com.zorvyn.dto.request;

import com.zorvyn.model.enums.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionRequestDto(
        @NotNull @Positive BigDecimal amount,
        @NotNull TransactionType type,
        @NotBlank String category,
        @NotNull LocalDate date,
        String description
) {}