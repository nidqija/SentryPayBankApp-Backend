package com.sentrypay.backend.domain.user.repository;


import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sentrypay.backend.domain.user.entity.TransactionEntity;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
import java.util.List;

@Repository
public interface TransactionHistoryRepository extends JpaRepository<TransactionEntity, Long> {
    // custom query method to find a service by its name, 
    // returning an Optional to handle the case where the service may not exist
    @Query("SELECT t FROM TransactionEntity t WHERE t.sender.id = :senderId OR t.receiver.id = :receiverId ORDER BY t.createdAt DESC")
    List<TransactionEntity> findTransactionHistoryBySenderOrReceiverId(
        @Param("senderId") Long senderId, 
        @Param("receiverId") Long receiverId
    );

}