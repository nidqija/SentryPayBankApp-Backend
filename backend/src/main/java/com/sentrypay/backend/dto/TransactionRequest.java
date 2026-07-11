package com.sentrypay.backend.dto;

import java.math.BigDecimal;

public record TransactionRequest(
    String receiverId,
    BigDecimal amount
) {}

