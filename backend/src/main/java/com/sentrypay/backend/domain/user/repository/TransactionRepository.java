package com.sentrypay.backend.domain.user.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.sentrypay.backend.domain.user.entity.TransactionEntity;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    // Custom query methods can be defined here if needed
    
}
