package com.sentrypay.backend.domain.user.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sentrypay.backend.domain.user.entity.ServiceSubscriptionEntity;


@Repository
public interface ServiceSubscriptionRepository extends JpaRepository<ServiceSubscriptionEntity, Long> {
    

    // we use list because a user can have multiple service subscriptions, so we return a list of ServiceSubscriptionEntity objects that match the given user id
    // given this is a one to many relationship, we can use the findByUserId method to retrieve all service subscriptions associated with a specific user id
    List<ServiceSubscriptionEntity> findByUserId(Long id);

    // in the service subscription repository, we define a method to find a service subscription by user id and service id ( because we define userId as User and serviceId as Services in the service subscription entity )  , which returns an optional ServiceSubscriptionEntity object
    @Query("SELECT s FROM ServiceSubscriptionEntity s WHERE s.user.id = :userId AND s.service.id = :serviceId")
    Optional<ServiceSubscriptionEntity> findByUserIdAndServiceId(
        @Param("userId")Long userId, 
        @Param("serviceId")String serviceId
    );

}
