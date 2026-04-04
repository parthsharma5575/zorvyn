package com.zorvyn.service;

import com.zorvyn.dto.request.TransactionRequestDto;
import com.zorvyn.dto.response.TransactionResponseDto;
import com.zorvyn.exception.ResourceNotFoundException;
import com.zorvyn.mappers.TransactionMapper;
import com.zorvyn.model.Transaction;
import com.zorvyn.model.User;
import com.zorvyn.model.enums.Role;
import com.zorvyn.model.enums.TransactionType;
import com.zorvyn.repository.TransactionRepository;
import com.zorvyn.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final TransactionMapper transactionMapper;
    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository, TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.transactionMapper = transactionMapper;
    }


    public TransactionResponseDto createTransaction(TransactionRequestDto transactionRequestDto){
        User user = getCurrentUser();
        String email = user.getEmail();
        Transaction transaction=new Transaction();
        transaction.setAmount(transactionRequestDto.amount());
        transaction.setType(transactionRequestDto.type());
        transaction.setCategory(transactionRequestDto.category());
        transaction.setDate(transactionRequestDto.date());
        transaction.setDescription(transactionRequestDto.description());
        transaction.setUser(user);
        transactionRepository.save(transaction);
        return transactionMapper.toResponseDto(transaction);
    }


    public List<TransactionResponseDto> getAllTransactions(){
        User user = getCurrentUser();
        Role role=user.getRole();
        if(role.equals(Role.ADMIN)){
            List<Transaction> list = transactionRepository.findAll();
            return list.stream().map(transactionMapper::toResponseDto).toList();
        } else{
            List<Transaction> transactionList=transactionRepository.findByUserId(user.getId());

            return transactionList.stream().map(transactionMapper::toResponseDto).toList();
        }


    }


    public TransactionResponseDto getTransactionById(UUID id){
        Optional<Transaction> transactionList =transactionRepository.findById(id);
        if(transactionList.isEmpty()){
            throw new ResourceNotFoundException("Transaction not found");
        }
        return transactionMapper.toResponseDto(transactionList.get());
    }


    public TransactionResponseDto updateTransaction(UUID id,TransactionRequestDto transactionRequestDto){
        Optional<Transaction> transactionList =transactionRepository.findById(id);
        if(transactionList.isEmpty()){
            throw new ResourceNotFoundException("Transaction not found");
        }
        Transaction transaction=transactionList.get();
        transaction.setAmount(transactionRequestDto.amount());
        transaction.setType(transactionRequestDto.type());
        transaction.setCategory(transactionRequestDto.category());
        transaction.setDescription(transactionRequestDto.description());
        transaction.setDate(transactionRequestDto.date());
        transactionRepository.save(transaction);
        return transactionMapper.toResponseDto(transaction);
    }


    public void deleteTransaction(UUID id){
        Optional<Transaction> transactionList =transactionRepository.findById(id);
        if(transactionList.isEmpty()){
            throw new ResourceNotFoundException("Transaction not found");
        }
        transactionRepository.deleteById(id);
    }


    public List<TransactionResponseDto> getTransactionByFilter(Optional<TransactionType>type , Optional<String>category, Optional<LocalDate>startDate, Optional<LocalDate>endDate ){
        User user = getCurrentUser();
        List<Transaction> transactions = transactionRepository.findByUserId(user.getId());

        return transactions.stream()
                .filter(t -> type.isEmpty() || t.getType().equals(type.get()))
                .filter(t -> category.isEmpty() || t.getCategory().equalsIgnoreCase(category.get()))
                .filter(t -> startDate.isEmpty() || !t.getDate().isBefore(startDate.get()))
                .filter(t -> endDate.isEmpty() || !t.getDate().isAfter(endDate.get()))
                .map(transactionMapper::toResponseDto)
                .toList();


    }

    private User getCurrentUser() {
        String email = Objects.requireNonNull(SecurityContextHolder.getContext()
                        .getAuthentication())
                .getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

}
