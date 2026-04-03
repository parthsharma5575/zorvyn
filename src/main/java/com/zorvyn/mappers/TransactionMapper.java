package com.zorvyn.mappers;

import com.zorvyn.dto.request.TransactionRequestDto;
import com.zorvyn.dto.response.TransactionResponseDto;
import com.zorvyn.model.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {
    public TransactionResponseDto toResponseDto(Transaction transaction) {
        return new TransactionResponseDto(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getCategory(),
                transaction.getDescription(),
                transaction.getDate(),
                transaction.getUser().getName(),
                transaction.getCreatedAt()
        );

    }
    public Transaction toEntity(TransactionRequestDto transactionRequestDto) {
        Transaction transaction = new Transaction();
            transaction.setAmount(transactionRequestDto.amount());
            transaction.setType(transactionRequestDto.type());
            transaction.setCategory(transactionRequestDto.category());
            transaction.setDescription(transactionRequestDto.description());
            transaction.setDate(transactionRequestDto.date());
            return transaction;
    }
}
