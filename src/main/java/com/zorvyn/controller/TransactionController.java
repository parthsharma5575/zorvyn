package com.zorvyn.controller;

import com.zorvyn.dto.request.TransactionRequestDto;
import com.zorvyn.dto.response.TransactionResponseDto;
import com.zorvyn.model.enums.TransactionType;
import com.zorvyn.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
        private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }


    @PostMapping("/")
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
    public ResponseEntity<TransactionResponseDto> createTransaction(@Valid @RequestBody TransactionRequestDto transactionRequestDto){
        TransactionResponseDto dto = transactionService.createTransaction(transactionRequestDto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN','ANALYST','VIEWER')")
    @GetMapping("/")
    public ResponseEntity<List<TransactionResponseDto>> getAllTransactions(){
        List<TransactionResponseDto> list = transactionService.getAllTransactions();
        return new ResponseEntity<>(list, HttpStatus.OK);

    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST','VIEWER')")
    public ResponseEntity<TransactionResponseDto> getTransactionById(@PathVariable UUID id){
        TransactionResponseDto transaction = transactionService.getTransactionById(id);
        return new ResponseEntity<>(transaction,HttpStatus.OK);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponseDto> updateTransaction(@PathVariable UUID id, @Valid @RequestBody TransactionRequestDto transactionRequestDto){
        TransactionResponseDto transaction = transactionService.updateTransaction(id, transactionRequestDto);
        return new ResponseEntity<>(transaction,HttpStatus.OK);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable UUID id){
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','ANALYST','VIEWER')")
    @GetMapping("/filter")
    public ResponseEntity<List<TransactionResponseDto>> getTransactionByFilter(@RequestParam Optional<TransactionType>type , @RequestParam Optional<String>category, @RequestParam Optional<LocalDate>startDate, @RequestParam Optional<LocalDate>endDate){
        List<TransactionResponseDto> list = transactionService.getTransactionByFilter(type, category, startDate, endDate);
        return new ResponseEntity<>(list,HttpStatus.OK);
    }
}
