package com.sentrypay.backend.dto;

import java.util.List;

import com.sentrypay.backend.domain.user.entity.ServicesEntity;


// create a transfer object to return a list of services
public class ServicesResponse {
    
    // create a list of services 
    private List<ServicesEntity> services;

    // create a constructor to initialize the list of services
    // this time , we are taking service list as parameter
    public ServicesResponse(List<ServicesEntity> services) {
        this.services = services;
    }

    // create getter for the list of services
    public List<ServicesEntity> getServices() {
        return services;
    }
    
    // create setter for the list of services
    public void setServices(List<ServicesEntity> services) {
        this.services = services;
    }
}
