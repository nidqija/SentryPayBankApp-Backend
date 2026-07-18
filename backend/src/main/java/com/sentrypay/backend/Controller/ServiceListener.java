package com.sentrypay.backend.Controller;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class ServiceListener {
    
    @JmsListener(destination = "sentrypay-service-queue")
    @SendTo("sentrypay-queue-response")

    public String receiveServiceMessage(String message){
        System.out.println("Received message from SentryPay service transaction queue: " + message);

        String response  = "Service transaction message received successfully: " + message;

        
        return response;
    }



}
