package com.sentrypay.backend.domain.user.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.sentrypay.backend.domain.user.entity.WalletEntity;
import jakarta.persistence.LockModeType;
import java.util.Optional;


@Repository
public interface WalletRepository extends JpaRepository<WalletEntity, Long> {


    Optional<WalletEntity>findByUserId(Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w FROM WalletEntity w WHERE w.user.id = :userId")
    Optional<WalletEntity> findByUserIdWithLock(Long userId);
    
}
