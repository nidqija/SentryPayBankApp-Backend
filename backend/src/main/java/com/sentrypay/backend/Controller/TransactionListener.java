package com.sentrypay.backend.Controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionListener {
    
    @JmsListener(destination = "sentrypay-queue")
	@SendTo("sentrypay-queue-response")
	public String receiveTestMessage(String message) {
		System.out.println("Received message from SentryPay queue: " + message);
		StringBuilder responseBuilder = new StringBuilder();
		String finalOutput = "990000000000";
		String detectedNewBalance = null;

	
		try {
			String sourceAccount = message.substring(0, 10).trim();
			String destinationAccount = message.substring(10, 20).trim();
			String currentBalanceString = message.substring(20, 28).trim();
			String deductionAmountString = message.substring(28, 36).trim();
			int deductedamount = Integer.parseInt(deductionAmountString);


			System.out.println("   -> Parsed Source Acc : " + sourceAccount);
			System.out.println("   -> Parsed Target Acc : " + destinationAccount);
			System.out.println("   -> Parsed Current Balance : " + currentBalanceString);
			System.out.println("   -> Parsed Deduction Amount : " + deductionAmountString);

			System.out.println("🚀 Spawning native COBOL engine process...");

			// we use this folder path to run the COBOL engine executable, which is located 
			// in the core-transactions/payload directory
			String cobolCommand = "../core-transactions/payload/bank_processor.exe" ;

			ProcessBuilder processBuilder = new ProcessBuilder(cobolCommand , sourceAccount, destinationAccount, currentBalanceString, String.valueOf(deductedamount));

			processBuilder.redirectErrorStream(false); // Merge error stream with output stream

			Process process = processBuilder.start();

			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String line;

			/*System.out.println("📜 COBOL Engine Output:");

			while ((line = reader.readLine()) != null) {
				System.out.println(line);
				responseBuilder.append(line).append("\n");
			} */

			System.out.println("📜 COBOL Engine Output:");
			while((line = reader.readLine()) != null) {
				System.out.println("[COBOL STDOUT]" + line);

				if(line.trim().matches("\\d{8,13}")) { // check if the line contains only digits and has a length of 8 to 13 characters
					detectedNewBalance = line.trim();
					System.out.println("✅ Detected new balance: " + detectedNewBalance);

				} else if (line.contains("ERROR")){
					detectedNewBalance = line.trim();
					System.out.println("❌ Error detected in COBOL engine output: " + detectedNewBalance);
				}
			}

			int exitCode = process.waitFor();

			System.out.println("COBOL engine process exited with code: " + exitCode);

			if(exitCode == 0 && detectedNewBalance != null && detectedNewBalance.matches("\\d{8,13}")) {
				finalOutput = detectedNewBalance;
				System.out.println("✅ COBOL Engine Response: " + finalOutput);
			}






			
		} catch (Exception e) {
			System.err.println("Error processing transaction: " + e.getMessage());
			e.printStackTrace();
		}


	return finalOutput;	

	
	}


	
	

}
