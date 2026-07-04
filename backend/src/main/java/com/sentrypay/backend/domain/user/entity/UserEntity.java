package com.sentrypay.backend.domain.user.entity;


import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;




@Entity // declares that this class is a JPA entity and will be mapped to a database table
@Table(name = "users") // define the table name
@Data // generates getters, setters, toString, equals, and hashCode methods
@NoArgsConstructor // generates a no-argument constructor
@AllArgsConstructor // generates a constructor with all fields as parameters

public class UserEntity { // defines the UserEntity class, which represents a user in the system
    
    // declare the data type of the field and its attributes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String fullname;


    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private int phoneNumber;

    @Column(nullable = false, unique = true)
    private String dateofBirth;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String antiPhishingName;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    // create a one to one relationship between user entity and wallet entity , 
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private WalletEntity wallet;


    // create one to many relationship between user entity and service subscription entity,
    // one user can have many service subscriptions, but each service subscription belongs to one user
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServiceSubscriptionEntity> serviceSubscriptions;

    // pre presist method is used for setting the createdAt field to the current date 
    // and time before the entity is persisted to the database
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
