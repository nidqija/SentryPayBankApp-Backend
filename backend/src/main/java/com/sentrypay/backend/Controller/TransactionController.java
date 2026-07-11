package com.sentrypay.backend.Controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
 
import com.sentrypay.backend.domain.user.repository.WalletRepository;
import com.sentrypay.backend.dto.TransactionRequest;

import jakarta.transaction.Transactional;





@RestController // declaration of the class as a REST controller
@RequestMapping("/api") // define the base URL for all endpoints in this controller
public class TransactionController {

    private final WalletRepository walletRepository; // declare a wallet repository for database operations


    public TransactionController(WalletRepository walletRepository) {
        this.walletRepository = walletRepository; // initialize the wallet repository
    }

    
    // autowired is used to inject the JmsTemplate bean into this controller
    // the jmstemplate wont be marked as null because it is autowired by spring, 
    // so we can use it to send messages to the queue
    // we are telling springboot that we want to use the jmstemplate by putting autowired above it, 
    // so springboot will inject the jmstemplate bean into this controller
    @Autowired 

    private JmsTemplate jmsTemplate; // declare a JMS template for sending messages to the queue

  

    // UNIT TEST TO RECEIVE TRANSACTION REQUEST FROM USER
    @PostMapping("/user-transactions/{senderId}") // define the endpoint for transaction requests
    @Transactional
    public ResponseEntity<String> sendTransactionToUser(@PathVariable Long senderId , @RequestBody TransactionRequest transactionRequest) {

        System.out.println("Received transaction request from user with ID: " + senderId);
        System.out.println("Receiver ID: " + transactionRequest.receiverId());
        System.out.println("Amount: " + transactionRequest.amount());

        
        // change receiverid from string to long
        long receiverId = Long.parseLong(transactionRequest.receiverId());

        // change the
        // amount from Bigdecimal to int and multiply by 100 to convert to cents
        BigDecimal amount = transactionRequest.amount();

        // retrieve the sender's wallet from the database using the senderId, and lock it for update to prevent concurrent modifications
        // we lock it because we want to make sure the balance is not changed by another user's transaction while we are processing this one
        // by doing this , user A cannot send money to user B while user B is sending money to user A, because the wallet is locked for update until the transaction is complete
        var senderWallet = walletRepository.findByUserIdWithLock(senderId).orElseThrow(() -> new RuntimeException("Sender wallet not found"));


        // convert the sender's balance and the transaction amount to cents (integer representation) for COBOL processing
        int currentBalanceCents = BigDecimal.valueOf(senderWallet.getBalance()).multiply(new BigDecimal("100")).intValue(); // convert balance to cents
        int deductionAmountCents = amount.multiply(new BigDecimal("100")).intValue(); // convert amount to cents


        // cobol only accepts fixed-length strings , so we need to format the senderId, receiverId, currentBalanceCents, and deductionAmountCents as fixed-length strings before sending them to the COBOL service
        // example: if senderId is 123, we need to format it as 0000000123 (10 chars) before sending it to the COBOL service
        String cobolResponse = callCobolService(senderId, receiverId, currentBalanceCents, deductionAmountCents);

        if (cobolResponse.contains("ERROR")){
            return ResponseEntity.badRequest().body("Transaction failed: " + cobolResponse);
        } else {
            

            int newBalanceCents = Integer.parseInt(cobolResponse); // parse the new balance from the COBOL response
            float newBalance = newBalanceCents / 100.0f;

            // update the sender's wallet balance in the database
            senderWallet.setBalance(newBalance);
            walletRepository.save(senderWallet); // save the updated wallet to the database
            System.out.println("✅ Updated sender's wallet balance to: " + newBalance);
        }
        


        return ResponseEntity.ok("Transaction request sent to user with ID: " + senderId + ". COBOL service response: " + cobolResponse);
    }


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



   // initialize the jms message template to send and receive messages from the queue, 
   // this is used to send the transaction request to the cobol service and receive the response from the cobol service
   @Autowired
   private JmsMessagingTemplate jmsMessagingTemplate;

   private String callCobolService(long senderId, long receiverId, int currentBalanceCents, int deductionAmountCents) {

        // Format the request as a fixed-length string
        // format the senderid , receiverid , currentbalancecents , and deductionamountcents as fixed-length strings before sending them to the COBOL service
        // this is called as raw data block, because we are sending the data as a raw string to the COBOL service, and 
        // the COBOL service will parse the string and extract the data from it
        String source = String.format("%010d", senderId); // 10 chars
        String destination = String.format("%010d", receiverId); // 10 chars
        String currentBalance = String.format("%08d", currentBalanceCents); // 8 chars
        String deductionAmount = String.format("%08d", deductionAmountCents); // 8 chars

        String rawCobolRequest = source + destination + currentBalance + deductionAmount;

        System.out.println("Raw COBOL Request: " + rawCobolRequest); // log the raw COBOL request

        // create a response object to receive the response from the COBOL service
        // send the raw COBOL request to the JMS queue and wait for a response
        // we use queue as the communication channel between the springboot application and the COBOL service, 
        // because the COBOL service is running on a different server and we want to decouple the two services
        Object response =  jmsMessagingTemplate.convertSendAndReceive("sentrypay-queue", rawCobolRequest , String.class); // send the raw COBOL request to the JMS queue


        return response != null ? response.toString() : "No response from COBOL service"; // return the response from the COBOL service
    }



}


