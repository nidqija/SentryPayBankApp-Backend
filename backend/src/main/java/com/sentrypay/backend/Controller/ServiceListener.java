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

    // receives the message from the jmt listener function  and sends a response to the sentrypay-queue-response
    public String receiveServiceMessage(String message){
        System.out.println("Received message from SentryPay service transaction queue: " + message);
        String response  = "Service transaction message received successfully: " + message;


        String finalOutput = "990000000000";
        // String message = "Processing subscription payment for user " + userId + " and service " + serviceId + " with amount " + servicePrice;


        try {
            // Parse the fixed-length string message to extract userId, serviceId, and servicePrice
            String userIdString = message.substring(0, 10).trim();
            String serviceId = message.substring(10, 20).trim();
            String servicePriceString = message.substring(20, 27).trim();
            double servicePrice = Double.parseDouble(servicePriceString) / 100.0; // Convert to decimal format


            // log the parsed values for debugging purposes
            System.out.println("   -> Parsed User ID : " + userIdString);
            System.out.println("   -> Parsed Service ID : " + serviceId);
            System.out.println("   -> Parsed Service Price : " + servicePrice);

            // Spawn the native COBOL engine process to handle the service transaction
            System.out.println("🚀 Spawning native COBOL engine process...");

            String cobolCommand = "../core-transactions/payload/service_deduction.exe";

            // use processbuilder to run the cobol engine executable with the parsed parameters
            ProcessBuilder processBuilder = new ProcessBuilder(cobolCommand, userIdString , serviceId , String.valueOf(servicePrice));

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
            if(exitCode == 0 && line != null && !line.isEmpty()) {
                finalOutput = line.trim();
                System.out.println("Final output from COBOL engine: " + finalOutput);
            } else {

                // if the exit code is not 0, then the process failed and we can log an error message
                System.err.println("COBOL engine did not produce a valid output.");
                response = "Error processing service transaction message: COBOL engine did not produce a valid output.";
            }


        } catch (Exception e) {
            System.err.println("Error parsing message: " + e.getMessage());
            response = "Error processing service transaction message: " + e.getMessage();
        }

        
        return response;
    }



}
