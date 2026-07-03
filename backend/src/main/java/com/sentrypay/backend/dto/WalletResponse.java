package com.sentrypay.backend.dto;

import java.util.List;
import com.sentrypay.backend.domain.user.entity.WalletEntity;
import java.time.LocalDateTime;


// record class is used for creating immutable data transfer objects (DTOs) in Java. 
// It is a concise way to define a class that holds data without having to write boilerplate code for getters, setters, equals, hashCode, and toString methods. 
// In this case, the WalletResponse record class is used to encapsulate a list of WalletEntity objects and provide a simple way to return this data from an API endpoint.
public record WalletResponse(
    Long walletId,
    float balance,
    String currency,
    LocalDateTime createdAt,
    Long userId,
    String userFullname
){}