package com.sentrypay.backend.Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sentrypay.backend.domain.user.entity.ServiceSubscriptionEntity;
import com.sentrypay.backend.domain.user.entity.ServicesEntity;
import com.sentrypay.backend.domain.user.repository.ServiceSubscriptionRepository;
import com.sentrypay.backend.domain.user.repository.ServicesRepository;
import com.sentrypay.backend.dto.ServiceSubscriptionResponse;
import com.sentrypay.backend.dto.ServicesResponse;


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
    public ResponseEntity<String> processSubscriptionPayment(@PathVariable Long userId, @PathVariable String serviceId) {
        
        var userWalletBalance = 0.0;
        var servicePrice = 0.0;

         List<ServiceSubscriptionEntity> serviceSubscriptions = serviceSubscriptionRepository.findByUserId(userId);


        if(serviceSubscriptions.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ServiceSubscriptionEntity subscription = serviceSubscriptions.stream()
            .filter(sub -> String.valueOf(sub.getService().getServicesId()).equals(serviceId))
            .findFirst()
            .orElse(null);

        if (subscription == null) {
            return ResponseEntity.notFound().build();
        }

       if (subscription.getEndDate() != LocalDateTime.now()){
            
          var newEndDate = LocalDateTime.now().plusMonths(1);

           var userWallet = subscription.getUser().getWallet();


           // convert wallet balance to double , convert to string first then to double
           userWalletBalance = Double.parseDouble(String.valueOf(userWallet.getBalance()));

           servicePrice = subscription.getService().getServicePrice();

           if (userWallet.getBalance() < servicePrice){
             return ResponseEntity.badRequest().body("Insufficient balance in user wallet to process subscription payment.");
           }


        } else {
            return ResponseEntity.badRequest().body("Subscription is still active. No payment required.");
        }
    
    /*    double servicePrice = 10.0; // Replace with actual service price

        double userWalletBalance = 100.0; */ 

        // send the params to jmt function 
        sendMessageToQueue(userWalletBalance , serviceId , servicePrice);

        return ResponseEntity.ok("Subscription payment processing initiated for user " + userId + " and service " + serviceId); 

    }


    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    // receives the params parsed by function above
    private String sendMessageToQueue(double userWalletBalance , String serviceId , double servicePrice) {
      
        // convert params to a fixed length string message to send to the queue
        String userWalletBalanceString = String.format("%010.2f", userWalletBalance).replace(".", ""); // Pad userWalletBalance to 10 digits and remove decimal point
        String serviceIdString = String.format("%-10s", serviceId); // Pad serviceId to 10 digits
        String servicePriceString = String.format("%08.2f", servicePrice).replace(".", ""); // Pad servicePrice to 8 digits and remove decimal point
    
        // merge the strings to a single message string argument
        // example of how the message gonna look like: "0000010000SERVICE1 00001000"
        String message = userWalletBalanceString + serviceIdString + servicePriceString;

        // log for debugging purposes
        System.out.println("Sending message to SentryPay service transaction queue: " + message);

        // send the message to sentry pay service transaction queue and wait for a response
        Object response = jmsMessagingTemplate.convertSendAndReceive("sentrypay-service-queue", message , String.class);

        // return to terminal for debugging purposes
        return "Response from SentryPay service transaction queue: " + response;
    }



        @PostMapping("/users/{userId}/cancel-subscription-payment/{serviceId}")
        public ResponseEntity<String> cancelSubscriptionPayment(@PathVariable Long userId , @PathVariable String serviceId){
            

           // retrieve the subscription status for the given userId and serviceId
         /*  String userSubscriptionStatus = serviceSubscriptionRepository.findByUserIdAndServiceId(userId ,serviceId)
            .map(subscription -> subscription.getStatus())
            .orElse(null);  */ 
            

           String userSubscriptionName = serviceSubscriptionRepository.findByUserIdAndServiceId(userId ,serviceId)
            .map(subscription -> 
                subscription.getService().getServiceName())
            .orElse(null);

            String getUserName = serviceSubscriptionRepository.findByUserIdAndServiceId(userId ,serviceId)
            .map(subscription -> subscription.getUser().getFullname())
            .orElse(null);

            
                // log for debugging purposes
               System.out.println("Attempting to cancel subscription payment for user " + userId + " and service " + serviceId );



                // update the subscription status to cancelled in the database
                serviceSubscriptionRepository.findByUserIdAndServiceId(userId ,serviceId)
                .ifPresent(subscription -> {
                    subscription.setStatus("not active");
                    serviceSubscriptionRepository.save(subscription);
                });

              String userSubscriptionStatus = serviceSubscriptionRepository.findByUserIdAndServiceId(userId ,serviceId)
                .map(subscription -> subscription.getStatus())
                .orElse(null);



                

                System.out.println("User subscription status: " + userSubscriptionStatus);
                System.out.println("User subscription name: " + userSubscriptionName);
                System.out.println("User name: " + getUserName);

            

                


            return ResponseEntity.ok("Subscription payment cancellation initiated for user " + userId + " and service " + serviceId + ". Current subscription status: " + userSubscriptionStatus);

            
        }


    

}
