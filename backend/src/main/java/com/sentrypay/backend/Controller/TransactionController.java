package com.sentrypay.backend.Controller;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;



@RestController // declaration of the class as a REST controller
@RequestMapping("/api") // define the base URL for all endpoints in this controller
public class TransactionController {
    
    // autowired is used to inject the JmsTemplate bean into this controller
    // the jmstemplate wont be marked as null because it is autowired by spring, 
    // so we can use it to send messages to the queue
    // we are telling springboot that we want to use the jmstemplate by putting autowired above it, 
    // so springboot will inject the jmstemplate bean into this controller
    @Autowired 

    private JmsTemplate jmsTemplate; // declare a JMS template for sending messages to the queue

    @GetMapping("/transaction") // define the endpoint for transaction requests
    public String transaction() {
        
        String source = "1234567890";      // 10 chars
        String destination = "0987654321"; // 10 chars
        String amountCents = "00010000";   // 8 chars ($100.00 represented in cents)


        String rawCobolRequest = source + destination + amountCents;

        System.out.println("Raw COBOL Request: " + rawCobolRequest); // log the raw COBOL request

        jmsTemplate.convertAndSend("sentrypay-queue", rawCobolRequest); // send the raw COBOL request to the JMS queue

        
        return "Transaction request sent to COBOL queue.";
    }
}
