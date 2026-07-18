/*package com.sentrypay.backend.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.format.DateTimeFormatter;
import com.sentrypay.backend.domain.user.entity.ServicesEntity;
import com.sentrypay.backend.domain.user.entity.ServiceSubscriptionEntity;
import com.sentrypay.backend.domain.user.repository.ServicesRepository;
import com.sentrypay.backend.domain.user.repository.ServiceSubscriptionRepository;
import com.sentrypay.backend.dto.ServicesResponse;
import com.sentrypay.backend.dto.ServiceSubscriptionResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/api")
public class ServicesController {


    // create an instance of the ServicesRepository to interact with the database
    private final ServicesRepository servicesRepository;
    private final ServiceSubscriptionRepository serviceSubscriptionRepository;


    // instantiate the service repo or any other repo
    public ServicesController(ServicesRepository servicesRepository , ServiceSubscriptionRepository serviceSubscriptionRepository) {
        this.servicesRepository = servicesRepository;
        this.serviceSubscriptionRepository = serviceSubscriptionRepository;
    }

    


    @GetMapping("/services")
    public ResponseEntity<ServicesResponse> getAllServices(){

        List<ServicesEntity> services = servicesRepository.findAll();

        if (services.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // log the number of services retrieved for debugging purposes
        System.out.println("Services retrieved: " + services.size());

        // log the details of each service retrieved for debugging purposes
        for (ServicesEntity service : services) {
            System.out.println("Service ID: " + service.getServicesId() + ", Service Name: " + service.getServiceName());
        }

        // return the list of services in the response body with a 200 OK status to the client
        // we first create a new instance of the ServicesResponse class and pass the list of services to its constructor.
        // the list will then be serialized into JSON format and sent back to the client in the response body.
        return ResponseEntity.ok(new ServicesResponse(services));

    }


    @GetMapping("/users/{userId}/user-services")
    public ResponseEntity<ServiceSubscriptionResponse> getServicesByUserId(@PathVariable Long userId) {

        
        List<ServiceSubscriptionEntity> serviceSubscriptions = serviceSubscriptionRepository.findByUserId(userId);

        if(serviceSubscriptions.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

       DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


        List<ServiceSubscriptionResponse.SubscriptionDetails> subscriptionDetailsList = serviceSubscriptions.stream()
        .map(subscription -> {
            String formattedStartDate = subscription.getStartDate().format(formatter);
            String formattedEndDate = subscription.getEndDate() != null ? subscription.getEndDate().format(formatter) : null;
          return  new ServiceSubscriptionResponse.SubscriptionDetails(
                
                subscription.getId(),
                subscription.getService().getServiceName(),
                subscription.getStatus(),
                subscription.getService().getServiceType(),
                formattedStartDate,
                formattedEndDate
        );
    })
        .toList();


        return ResponseEntity.ok(new ServiceSubscriptionResponse(subscriptionDetailsList));


    };


    @PostMapping("/users/{userId}/subscription-payment/{serviceId}")
    public ResponseEntity<String> processSubscriptionPayment(@PathVariable Long userId, @PathVariable Long serviceId) {
        


        List<ServiceSubscriptionEntity> serviceSubscriptions = serviceSubscriptionRepository.findByUserId(userId);


        if(serviceSubscriptions.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ServiceSubscriptionEntity subscription = serviceSubscriptions.stream()
            .filter(sub -> sub.getService().getServicesId().equals(serviceId))
            .findFirst()
            .orElse(null);

        if (subscription == null) {
            return ResponseEntity.notFound().build();
        }

       if (subscription.getEndDate() == DateTime.now()){
            
           var newEndDate = DateTime.now().plusMonths(1);

           var userWallet = subscription.getUser().getWallet();

           var servicePrice = subscription.getService().getServicePrice();

           if (userWallet.getBalance() < servicePrice){
             return ResponseEntity.badRequest().body("Insufficient balance in user wallet to process subscription payment.");
           }

           



           
       }


        return ResponseEntity.ok("Subscription payment processed for user " + userId + " and service " + serviceId);
    }

}*/