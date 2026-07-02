package com.sentrypay.backend.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sentrypay.backend.domain.user.entity.ServicesEntity;
import com.sentrypay.backend.domain.user.repository.ServicesRepository;
import com.sentrypay.backend.dto.ServicesResponse;


@RestController
@RequestMapping("/api")
public class ServicesController {


    // create an instance of the ServicesRepository to interact with the database
    private final ServicesRepository servicesRepository;


    // instantiate the service repo or any other repo
    public ServicesController(ServicesRepository servicesRepository) {
        this.servicesRepository = servicesRepository;
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

    
}