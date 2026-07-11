       IDENTIFICATION DIVISION.
       PROGRAM-ID. TRANSACTION.

       DATA DIVISION.
       WORKING-STORAGE SECTION.

       *> Define exact lengths for fields to prevent extra whitespace issues
       01  WS-SRC-ACCOUNT         PIC X(10).
       01  WS-TGT-ACCOUNT         PIC X(10).

       01  WS-INCOMING-DATA-BLOCK PIC X(100).

       01  WS-DEDUCTED-AMOUNT     PIC 9(8).
       01  WS-CURRENT-BALANCE     PIC 9(8).
       01  WS-NEW-BALANCE         PIC 9(8).

       
       01  WS-AMOUNT-STR             PIC X(8).
       01  WS-CURRENT-BAL-STR        PIC X(8).
      
       PROCEDURE DIVISION.
       MAIN-PROCEDURE.

           *> Accept the space-separated string from the process builder 
           *> arguments
           ACCEPT WS-INCOMING-DATA-BLOCK FROM COMMAND-LINE.

           *> move the incoming data block to the working storage fields

           IF WS-INCOMING-DATA-BLOCK = SPACES
               MOVE "0" TO WS-SRC-ACCOUNT
               MOVE "0" TO WS-TGT-ACCOUNT
               MOVE 0   TO WS-DEDUCTED-AMOUNT
               MOVE 0   TO WS-CURRENT-BALANCE
               MOVE 0   TO WS-NEW-BALANCE
               


           ELSE 
               *> UNSTRING automatically parses space-separated arguments into 
               *> defined variables
               UNSTRING WS-INCOMING-DATA-BLOCK DELIMITED BY ALL SPACES
                   INTO WS-SRC-ACCOUNT
                        WS-TGT-ACCOUNT
                        WS-CURRENT-BAL-STR
                        WS-AMOUNT-STR
               
               *> Convert the string amount back into a numeric picture type
               MOVE FUNCTION NUMVAL(WS-AMOUNT-STR) TO WS-DEDUCTED-AMOUNT
               MOVE FUNCTION NUMVAL(WS-CURRENT-BAL-STR) TO 
               WS-CURRENT-BALANCE

               IF WS-CURRENT-BALANCE < WS-DEDUCTED-AMOUNT
                   DISPLAY "ERROR: Insufficient funds for transaction."
                   UPON SYSERR
                   MOVE 0 TO WS-NEW-BALANCE
               ELSE
                   SUBTRACT WS-DEDUCTED-AMOUNT FROM WS-CURRENT-BALANCE
                   GIVING WS-NEW-BALANCE
               END-IF
           END-IF.         
       
           *> CRITICAL FIX FOR LOGGING:
           *> Print debug information to STDERR (System.err) so Java's 
           *> getInputStream() 
           *> reads ONLY the final response code.
           DISPLAY "=======================================" UPON SYSERR.
           DISPLAY " NATIVE COBOL TRANSACTION CORE ONLINE  " UPON SYSERR.
           DISPLAY "=======================================" UPON SYSERR.
           DISPLAY " COBOL PARSED DATA EXTRACT:"             UPON SYSERR.
           DISPLAY " -> Source Account : [" WS-SRC-ACCOUNT "]" UPON 
           SYSERR.
           DISPLAY " -> Target Account : [" WS-TGT-ACCOUNT "]" UPON 
           SYSERR.
           DISPLAY " -> Deducted Amount: [" WS-DEDUCTED-AMOUNT "]" UPON 
           SYSERR.
           DISPLAY " -> Current Balance: [" WS-CURRENT-BALANCE "]" UPON 
           SYSERR.
           DISPLAY " -> New Balance    : [" WS-NEW-BALANCE "]" UPON 
           SYSERR.
           DISPLAY "---------------------------------------" UPON SYSERR.
           

           *> The ONLY output on standard STDOUT stream. Java will capture 
           *> this perfectly.
           DISPLAY "0000054073551".

           STOP RUN.
