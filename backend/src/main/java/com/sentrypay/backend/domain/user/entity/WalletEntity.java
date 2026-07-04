package com.sentrypay.backend.domain.user.entity;



import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



// The steps of wallet creation are as follows:
// 1. Wallet Entity
// 2. Wallet Repository
// 3. Wallet Response ( DTO )
// 4. Wallet Controller
// 5. API Endpoint to create a wallet for a user
// 6. Kotlin 


@Entity // declares that this class is a JPA entity and will be mapped to a database table
@Table(name = "wallets") // define the table name
@Data // generates getters, setters, toString, equals, and hashCode methods
@NoArgsConstructor // generates a no-argument constructor
@AllArgsConstructor // generates a constructor with all fields as parameters

public class WalletEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long walletId;

    @Column(nullable = false, unique = true)
    private float balance;

    @Column(nullable = false)
    private String currency = "USD"; // default currency set to USD

    @Column(nullable = false)
    private LocalDateTime createdAt;

    // create a one to one relationship between wallet entity and user entity
    // uses join column to specify the foreign key column in the wallet table
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id" , referencedColumnName = "id" , nullable = false , unique = true)
    private UserEntity user;


    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

}
