package com.sentrypay.backend.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sentrypay.backend.domain.user.entity.ServiceSubscriptionEntity;


@Repository
public interface ServiceSubscriptionRepository extends JpaRepository<ServiceSubscriptionEntity, Long> {
    
    
}
