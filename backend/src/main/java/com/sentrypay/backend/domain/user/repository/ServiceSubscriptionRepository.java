package com.sentrypay.backend.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sentrypay.backend.domain.user.entity.ServiceSubscriptionEntity;
import java.util.List;


@Repository
public interface ServiceSubscriptionRepository extends JpaRepository<ServiceSubscriptionEntity, Long> {
    

    // we use list because a user can have multiple service subscriptions, so we return a list of ServiceSubscriptionEntity objects that match the given user id
    // given this is a one to many relationship, we can use the findByUserId method to retrieve all service subscriptions associated with a specific user id
    List<ServiceSubscriptionEntity> findByUserId(Long id);

}
