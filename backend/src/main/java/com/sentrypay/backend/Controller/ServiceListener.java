package com.sentrypay.backend.Controller;


import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
public class ServiceListener {
    
    @JmsListener(destination = "sentrypay-service-queue")
    @SendTo("sentrypay-queue-response")

    // receives the message from the jmt listener function  and sends a response to the sentrypay-queue-response
    public String receiveServiceMessage(String message){
        System.out.println("Received message from SentryPay service transaction queue: " + message);
        String response  = "Service transaction message received successfully: " + message;

        String finalOutput = "990000000000";
        // example of how the message gonna look like: "0000010000SERVICE1 00001000"

        try {

            // Parse the fixed-length string message to extract userWalletBalance, serviceId, and servicePrice
            String userWalletBalanceString = message.substring(0, 9).trim();
            String serviceId = message.substring(9, 19).trim();
            String servicePriceString = message.substring(20, 26).trim();
            double servicePrice = Double.parseDouble(servicePriceString) / 100.0; // Convert to decimal format

            // Convert userWalletBalanceString to a double
            double userWalletBalance = Double.parseDouble(userWalletBalanceString) / 100.0; // Convert to decimal format


            // log the parsed values for debugging purposes
            System.out.println("   -> Parsed User Wallet Balance : " + userWalletBalance);
            System.out.println("   -> Parsed Service ID : " + serviceId);
            System.out.println("   -> Parsed Service Price : " + servicePrice);

            // Spawn the native COBOL engine process to handle the service transaction
            System.out.println("🚀 Spawning native COBOL engine process...");

            // explicitly define the path to the COBOL engine executable for service deduction
            String cobolCommand = "../core-transactions/payload/service_deduction.exe";

            // use processbuilder to run the cobol engine executable with the parsed parameters
            // since cobol engine requires only two arguments , which is userWalletBalance and servicePrice, 
            // we will pass only those two arguments to the processbuilder

            ProcessBuilder processBuilder = new ProcessBuilder(cobolCommand, userWalletBalanceString , String.valueOf(servicePrice));

            // set the error stream to be false by default, so that we can capture the output of the process
            processBuilder.redirectErrorStream(false); // Merge error stream with output stream

            // start the processbuilder and capture the output of the process
            Process process = processBuilder.start();

            // use bufferedreader to read the output of the process
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));


            String line;

            // log the output of the process for debugging purposes
            System.out.println("📜 COBOL Engine Output:");

            // read the output of the process line by line and log it to the console
            while((line = reader.readLine()) != null) {
                System.out.println("[COBOL STDOUT]" + line);
            }

            // wait for the process to complete and capture the exit code
            int exitCode = process.waitFor();


            System.out.println("COBOL engine process exited with code: " + exitCode);

            // if the exit code is 0, then the process completed successfully and we can capture the output of the process
            if(exitCode == 0 ){
                finalOutput = "000000000000";
                System.out.println("✅ COBOL Engine Response: " + finalOutput);
            } else {
                finalOutput = "990000000000";
                System.out.println("❌ COBOL Engine Error: " + finalOutput);
            }
        } catch (Exception e) {
            System.err.println("Error parsing message: " + e.getMessage());
            response = "Error processing service transaction message: " + e.getMessage();
        }

        
        return response;
    }



}
