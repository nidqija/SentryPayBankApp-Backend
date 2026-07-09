package com.sentrypay.backend.dto;

import java.util.List;

public record TransactionResponse (
    
        List<TransactionDetails> transactionsList

){
    public record TransactionDetails(
            Long Id,
            String senderFullname,
            String receiverFullname,
            Double amount,
            String createdAt
    ) {}
}
