package com.zorvyn.service;

import com.zorvyn.dto.response.DashboardSummaryDto;
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

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    public DashboardService(UserRepository userRepository, TransactionRepository transactionRepository, TransactionMapper transactionMapper) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }
    private User getCurrentUser(){
        String email= Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        return userRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("User not found"));
    }
    public List<Transaction> getTransactions(){
        User user=getCurrentUser();
        Role role=user.getRole();
        if(role.equals(Role.ADMIN)||role.equals(Role.ANALYST)){
            return transactionRepository.findAll();
        }else{
            return transactionRepository.findByUserId(user.getId());
        }
    }
    public DashboardSummaryDto getSummary(){
        List<Transaction> transactions=getTransactions();
        BigDecimal totalIncome=transactions.stream().filter(
                t->t.getType().equals(TransactionType.INCOME)
        ).map(Transaction::getAmount).reduce(BigDecimal.ZERO,BigDecimal::add);

        BigDecimal totalExpense=transactions.stream().filter(t->t.getType().equals(TransactionType.EXPENSE)).map(Transaction::getAmount).reduce(BigDecimal.ZERO,BigDecimal::add);
        BigDecimal netBalance=totalIncome.subtract(totalExpense);
        Map<String,BigDecimal> categoryTotals=transactions.stream().collect(Collectors.groupingBy(
                Transaction::getCategory,
                Collectors.mapping(Transaction::getAmount,Collectors.reducing(BigDecimal.ZERO,BigDecimal::add))
        ));
        List<TransactionResponseDto>recentTransactions=transactions.stream().sorted(Comparator.comparing(Transaction::getDate).reversed()).limit(5)
                .map(transactionMapper::toResponseDto).toList();
        Map<String,BigDecimal>monthlyTotals=transactions.stream().collect(
                Collectors.groupingBy(
                        t->t.getDate().getMonth().name()+" "+t.getDate().getYear(),
                        Collectors.mapping(Transaction::getAmount,Collectors.reducing(BigDecimal.ZERO,BigDecimal::add))
                )
        );

        return new DashboardSummaryDto(totalIncome,totalExpense,netBalance,categoryTotals,recentTransactions,monthlyTotals);

    }
}
