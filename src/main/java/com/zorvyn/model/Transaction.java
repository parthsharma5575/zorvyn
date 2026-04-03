package com.zorvyn.model;

import com.zorvyn.model.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType type;
    @Column(name = "category", nullable = false)
    private String category;
    @Column(name = "description")
    private String description;
    @Column(name = "date", nullable = false)
    private LocalDate date;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
