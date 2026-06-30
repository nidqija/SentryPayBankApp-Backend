package com.sentrypay.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.JmsListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}


	
	@JmsListener(destination = "sentrypay-queue")
	public void receiveTestMessage(String message) {
		System.out.println("Received message from SentryPay queue: " + message);

	


		try {
			String sourceAccount = message.substring(0, 10).trim();
			String destinationAccount = message.substring(10, 20).trim();
			String amountString = message.substring(20, 28).trim();
			int amount = Integer.parseInt(amountString);


			System.out.println("   -> Parsed Source Acc : " + sourceAccount);
			System.out.println("   -> Parsed Target Acc : " + destinationAccount);
			System.out.println("   -> Parsed Amount Cents: " + amount);

			System.out.println("🚀 Spawning native COBOL engine process...");

			String cobolCommand = "../core-transactions/payload/transaction.exe" ;

			ProcessBuilder processBuilder = new ProcessBuilder(cobolCommand , sourceAccount, destinationAccount, String.valueOf(amount));

			processBuilder.redirectErrorStream(true); // Merge error stream with output stream

			Process process = processBuilder.start();

			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String line;

			System.out.println("📜 COBOL Engine Output:");

			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}


			int exitCode = process.waitFor();

			System.out.println("COBOL engine process exited with code: " + exitCode);






			
		} catch (Exception e) {
			System.err.println("Error processing transaction: " + e.getMessage());
			e.printStackTrace();
		}


	}

	

	

	

}
