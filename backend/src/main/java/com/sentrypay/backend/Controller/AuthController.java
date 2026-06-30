package com.sentrypay.backend.Controller;


import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sentrypay.backend.domain.user.entity.UserEntity;



@RestController // declaration of the class as a REST controller

@RequestMapping("/api") // define the base URL for all endpoints in this controller
public class AuthController {

    // create a new object of the entity class
    UserEntity user = new UserEntity();

    private void logError(String endpoint, Map<String, String> requestBody) {
        
        System.out.println("\n========================================");
        System.out.println("❌ SENTRY PAY " + endpoint.toUpperCase() + " ERROR!");
        System.out.println("Payload Data: " + requestBody);
        System.out.println("========================================\n");
    }

    private void logRequest(String endpoint, Map<String, String> requestBody) {
        System.out.println("\n========================================");
        System.out.println("📥 INCOMING SENTRY PAY " + endpoint.toUpperCase() + " REQUEST!");
        System.out.println("Payload Data: " + requestBody);
        System.out.println("========================================\n");
    }

    private void logSuccess(String endpoint, Map<String, String> requestBody) {
        System.out.println("\n========================================");
        System.out.println("✅ SENTRY PAY " + endpoint.toUpperCase() + " SUCCESS!");
        System.out.println("Payload Data: " + requestBody);
        System.out.println("========================================\n");
    }

    private String returnPhishingName(){
        
        String testPhishingName = "SentryPay"; // hardcoded phishing name for testing purposes
        // return the hardcoded phishing name
        return testPhishingName; 
    }
    

    @PostMapping("/login") // define the endpoint for login requests , this is a post request to the /login endpoint
    public ResponseEntity<Map<String, String>> Login(@RequestBody Map<String , String> LoginRequest) {
        
        logRequest("login", LoginRequest);

        // extract the username and password from the request body
        // request body is a map of key value pairs that was send in the request body of the post request
        // from the user's request , extract the username and password from the request body and store them in variables
        String username = LoginRequest.get("username");
        String password = LoginRequest.get("password");


        

        
        // set the username and password of the user object to 
        // the values extracted from the request body
        user.setEmail(username);
        user.setPassword(password);


        // if checker if the parameters are null or empty
        // log the error and return a bad request response with an error message
        if (user.getEmail() == null || user.getPassword() == null) {
            logError("login", LoginRequest);
            return ResponseEntity.badRequest().body(Map.of("error", "Username and password are required"));
        }

        
        // if the parameters match the hardcoded values , generate a sample JWT token and return it in the response body
        if (user.getEmail().equals(username) && user.getPassword().equals(password)) {
            var keyString  = "dummy-jwt-token"; // generate a JWT token for the user
            logSuccess("login", LoginRequest);

            // return the hardcoded phishing name in the response body
            String phishingName = returnPhishingName();

            // create a map to hold the response body with the token and phishing name
            Map <String, String> responseBody = Map.of("token", keyString, "antiPhishingName", phishingName);
            

            // pass the response body to the response entity
            return ResponseEntity.ok(responseBody);

        } else {
            logError("login", LoginRequest);
            return ResponseEntity.status(401).body(Map.of("error", "Invalid username or password"));
        }

    }


    
}
