       IDENTIFICATION DIVISION.

       PROGRAM-ID. TRANSACTION.


       DATA DIVISION.
       WORKING-STORAGE SECTION.

       *> DEFINE VARIABLES FOR COBOL TRANSACTION DATA

       01  WS-SRC-ACCOUNT     PIC X(20).
       01  WS-TGT-ACCOUNT     PIC X(20).
       01  WS-AMOUNT          PIC 9(10).
       01  WS-INCOMING-DATA-BLOCK PIC X(100).


       PROCEDURE DIVISION.
       MAIN-PROCEDURE.

           DISPLAY "=======================================".
           DISPLAY " NATIVE COBOL TRANSACTION CORE ONLINE  ".
           DISPLAY "=======================================".

           *> ACCEPT INCOMING DATA BLOCK FROM COMMAND LINE
           ACCEPT WS-INCOMING-DATA-BLOCK FROM COMMAND-LINE.

           *> IF INCOMING DATA BLOCK IS EMPTY , SET DEFAULT VALUES
           IF WS-INCOMING-DATA-BLOCK = SPACES
               MOVE "NO DATA PROVIDED" TO WS-INCOMING-DATA-BLOCK
               MOVE "NO DATA PROVIDED" TO WS-SRC-ACCOUNT
               MOVE 0 TO WS-AMOUNT
           ELSE 
           *> if incoming data is provided , parse the data block into source 
           *> account, target account, and amount
               MOVE WS-INCOMING-DATA-BLOCK(1:10)  TO WS-SRC-ACCOUNT
               MOVE WS-INCOMING-DATA-BLOCK(11:10) TO WS-TGT-ACCOUNT
               MOVE WS-INCOMING-DATA-BLOCK(21:8)  TO WS-AMOUNT

           END-IF.
       
           *> 3. Display the native extraction results
           DISPLAY " COBOL PARSED DATA EXTRACT:".
           DISPLAY " -> Source Account : [" WS-SRC-ACCOUNT "]"
           DISPLAY " -> Target Account : [" WS-TGT-ACCOUNT "]"
           DISPLAY " -> Amount In Cents: [" WS-AMOUNT "]"
           DISPLAY "---------------------------------------".

           *> 4. Output a response string back to Spring Boot's listener
           DISPLAY "0000054073551".

           STOP RUN.
           









           


           

       