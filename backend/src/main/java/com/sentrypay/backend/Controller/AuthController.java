package com.sentrypay.backend.Controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sentrypay.backend.domain.user.entity.UserEntity;
import com.sentrypay.backend.domain.user.repository.UserRepository;

@RestController 
@RequestMapping("/api") 
public class AuthController {

    // create a user repository instance to use custom database queries
    private final UserRepository userRepository;
    

    // constructor injection for the UserRepository dependency
    // this sets up the controller to use the repository for database operations in that session
    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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

    // uses for post requests
    @PostMapping("/login") 
    public ResponseEntity<Map<String, String>> Login(@RequestBody Map<String, String> LoginRequest) {
        
        logRequest("login", LoginRequest);

        String username = LoginRequest.get("username");
        String password = LoginRequest.get("password");

        // check if username or password is null, if so return a bad request response
        if (username == null || password == null) {
            logError("login", LoginRequest);
            return ResponseEntity.badRequest().body(Map.of("error", "Username and password are required"));
        }

        // fetch user info from user table using email
        Optional<UserEntity> userOptional = userRepository.findByEmail(username);

        // 4. Verify user exists and check password matching
        if (userOptional.isPresent()) {
            UserEntity dbUser = userOptional.get();

            if (dbUser.getPassword().equals(password)) {
                var keyString = "dummy-jwt-token"; 
                logSuccess("login", LoginRequest);

                // Fetching the dynamic string phrase directly from the verified entity
                String phishingName = dbUser.getAntiPhishingName();
                if (phishingName == null) {
                    phishingName = "Default Guard"; // fallback if the anti-phishing name is not set in the database
                }

                // map the key value pairs to return the token  and phishing name to response body
                Map<String, String> responseBody = Map.of(
                    "token", keyString, 
                    "antiPhishingName", phishingName
                );
                
                // return the response body with the token and anti-phishing name
                return ResponseEntity.ok(responseBody);
            }
        }

        // Catch-all fall through logic handles incorrect usernames or bad passwords
        logError("login", LoginRequest);
        return ResponseEntity.status(401).body(Map.of("error", "Invalid username or password"));
    }
}