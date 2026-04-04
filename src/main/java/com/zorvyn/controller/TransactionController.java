package com.zorvyn.controller;

import com.zorvyn.dto.request.TransactionRequestDto;
import com.zorvyn.dto.response.TransactionResponseDto;
import com.zorvyn.model.enums.TransactionType;
import com.zorvyn.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@Tag(name = "Transactions", description = "Transaction management")
@RequestMapping("/api/transactions")
public class TransactionController {
        private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }


    @PostMapping("/")
    @Operation(summary = "Create a new transaction")
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
    public ResponseEntity<TransactionResponseDto> createTransaction(@Valid @RequestBody TransactionRequestDto transactionRequestDto){
        TransactionResponseDto dto = transactionService.createTransaction(transactionRequestDto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN','ANALYST','VIEWER')")
    @Operation(summary = "Get all transactions")
    @GetMapping("/")
    public ResponseEntity<List<TransactionResponseDto>> getAllTransactions(){
        List<TransactionResponseDto> list = transactionService.getAllTransactions();
        return new ResponseEntity<>(list, HttpStatus.OK);

    }

    @GetMapping("/{id}")
    @Operation(summary = "Get transaction by id")
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST','VIEWER')")
    public ResponseEntity<TransactionResponseDto> getTransactionById(@PathVariable UUID id){
        TransactionResponseDto transaction = transactionService.getTransactionById(id);
        return new ResponseEntity<>(transaction,HttpStatus.OK);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Update transaction by id")
    public ResponseEntity<TransactionResponseDto> updateTransaction(@PathVariable UUID id, @Valid @RequestBody TransactionRequestDto transactionRequestDto){
        TransactionResponseDto transaction = transactionService.updateTransaction(id, transactionRequestDto);
        return new ResponseEntity<>(transaction,HttpStatus.OK);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete transaction by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable UUID id){
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','ANALYST','VIEWER')")
    @Operation(summary = "Get transactions by filter")
    @GetMapping("/filter")
    public ResponseEntity<List<TransactionResponseDto>> getTransactionByFilter(
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<TransactionResponseDto> list = transactionService.getTransactionByFilter(type, category, startDate, endDate);
        return ResponseEntity.ok(list);
    }
}
