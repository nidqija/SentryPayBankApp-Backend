package com.sentrypay.backend.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sentrypay.backend.domain.user.entity.ServicesEntity;
import com.sentrypay.backend.domain.user.repository.ServicesRepository;


@RestController
@RequestMapping("/api")
public class ServicesController {


    private final ServicesRepository servicesRepository;

    public ServicesController(ServicesRepository servicesRepository) {
        this.servicesRepository = servicesRepository;
    }


    @GetMapping("/services")
    public ResponseEntity<List<ServicesEntity>> getAllServices(){

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

        return ResponseEntity.ok(services);

    }

    
}