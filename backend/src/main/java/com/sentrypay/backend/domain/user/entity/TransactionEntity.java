package com.sentrypay.backend.domain.user.entity;


import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


// 1. Kotlin ViewModel
// 2. Kotlin Transaction Response ( DTO )
// 3. Kotlin Transaction Repository
// 4. API Endpoint to create a transaction for a user
// 5. Transaction Response ( DTO )
// 6. Transaction Controller
// 7. COBOL transaction 
// 8. Transaction Controller 
// 9. Update Wallet Balance


@Entity // declares that this class is a JPA entity and will be mapped to a database table
@Table(name = "user_transactions") // define the table name
@Data // generates getters, setters, toString, equals, and hashCode methods
@NoArgsConstructor // generates a no-argument constructor
@AllArgsConstructor // generates a constructor with all fields as parameters


public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private UserEntity sender;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private UserEntity receiver;


    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }


}
