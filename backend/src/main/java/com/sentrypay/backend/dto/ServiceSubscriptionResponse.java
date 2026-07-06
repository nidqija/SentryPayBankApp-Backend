package com.sentrypay.backend.dto;


import com.sentrypay.backend.domain.user.entity.ServiceSubscriptionEntity;
import java.util.List;





public record ServiceSubscriptionResponse(

    // takes a list of ServiceSubscriptionEntity objects and maps them to a list of SubscriptionDetails objects
    // we use list because a user can have multiple service subscriptions, so we return a list of SubscriptionDetails objects that match the given user id
    List<SubscriptionDetails> serviceSubscriptions
) {


    // create a new record class called SubscriptionDetails that represents the details of a service subscription
    public record SubscriptionDetails(
        Long subscriptionId,
        String serviceName,
        String subscriptionStatus,
        String subscriptionType,
        String subscriptionStartDate,
        String subscriptionEndDate
    ) {}
}
