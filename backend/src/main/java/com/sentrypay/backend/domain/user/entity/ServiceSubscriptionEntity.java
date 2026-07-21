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
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



// The steps of user service subscription creation are as follows:
// 1. ServiceSubscription Entity
// 2. ServiceSubscription Repository
// 3. ServiceSubscription Response ( DTO )
// 4. ServiceSubscription Controller
// 5. API Endpoint to create a service subscription for a user
// 6. Kotlin 
// 7. Kotlin ServiceSubscription Repository
// 8. Kotlin ServiceSubscription Response ( DTO )
// 9. Kotlin ViewModel

@Entity // declares that this class is a JPA entity and will be mapped to a database table
@Table(
    name = "user_service_subscriptions" ,
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "service_id"}) // add unique constraint to ensure that a user can only have one subscription to a specific service , in case of race conditions
    }
) // define the table name
@Data // generates getters, setters, toString, equals, and hashCode methods
@NoArgsConstructor // generates a no-argument constructor
@AllArgsConstructor // generates a constructor with all fields as parameters



// defines the ServiceSubscriptionEntity class, which represents a service subscription for a user in the system
// takes user and service as parameters and creates a new service subscription for the user
public class ServiceSubscriptionEntity {
    

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private ServicesEntity service;

    @Column(nullable = false)
    private LocalDateTime startDate = LocalDateTime.now();

    @Column(nullable = true)
    private LocalDateTime endDate = startDate.plusMonths(1); // default end date set to one month after start date;

    @Column(nullable = false)
    private String status = "active"; // default status set to active


}
