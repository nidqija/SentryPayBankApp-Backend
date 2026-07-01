package com.sentrypay.backend.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sentrypay.backend.domain.user.entity.ServicesEntity;


@Repository
public interface ServicesRepository extends JpaRepository<ServicesEntity, Long> {
    // custom query method to find a service by its name, 
    // returning an Optional to handle the case where the service may not exist
     
}
