package com.sentrypay.backend.dto;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionHistoryResponse(
    String transactionId,
    String senderId,
    String receiverId,
    BigDecimal amount,
    LocalDateTime createdAt
) {}