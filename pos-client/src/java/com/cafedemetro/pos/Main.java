package com.cafedemetro.pos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;
import javax.ejb.EJB;

public class Main {
    
    //1. dependency inject the EJB
    @EJB
    private static OrderMgtRemote ejb;
    
    private static BufferedReader in;

    public static void main(String[] args) {
        in = new BufferedReader(new InputStreamReader(System.in));
        
        String cmd = null;

        try {
            // Process the command
            outerloop: while (true) {
                /* Main menu */
                String instr = "Please input a command : \t\t(C)reate Order\t\t(U)pate Order\t\t(P)rint Order\t\t(B)ill Order\t\tE(x)it: ";
                String pattern = "[C|U|P|B|X|c|u|p|b|x]";

                cmd = getUserInput(instr, pattern);

                // Check if cmd is empty or not. If empty, break the while loop
                if (cmd.isEmpty()) {
                    CloseConn();
                    break;
                } else {
                    cmd = cmd.toUpperCase();
                }

                // Triage the logic
                switch (cmd) {
                    case "C":
                        CreateOrder();
                        break;
                    case "U":
                        UpdateOrder();
                        break;
                    case "P":
                        PrintOrder();
                        break;    
                    case "B":
                        BillOrder();
                        break;                         
                    case "X":
                        CloseConn();
                        break outerloop;
                    default:
                        System.out.println("Please input a valid command.");
                        break;
                }
            } // end of triage command
        } catch (Exception e) {
            System.out.println("Error occurred : " + e.getMessage());
            e.printStackTrace();
        } // end of try-catch
    }//end of main thread
    
    private static void CreateOrder(){
        try{
            String branchCode = getUserInput("Branch Code : ", "^(?!\\s*$).+");
            String sNumOfSeat = getUserInput("Num of Seat : ", "^[1-9]$");
            int numOfSeat = Integer.parseInt(sNumOfSeat);
            String qrCode = ejb.CreateOrder(branchCode, numOfSeat);
            System.out.println("QR Code : " + qrCode);
        } catch (Exception e) {
            System.out.println("Error occurred : " + e.getMessage());
            e.printStackTrace();
        } // end of try-catch            
    }
    
    private static void UpdateOrder(){
        try{
            String qrCode = getUserInput("QR Code : ", "^(?!\\s*$).+");
            String status = getUserInput("Target Status (A)ctive\t\t(P)ending\t\t(C)lose: ", "[A|P|C|a|p|c]");
            ejb.UpdateOrderInDB(qrCode, status);
        } catch (Exception e) {
            System.out.println("Error occurred : " + e.getMessage());
            e.printStackTrace();
        } // end of try-catch        
    }

    private static void PrintOrder(){
        try{
            String qrCode = getUserInput("QR Code : ", "^(?!\\s*$).+");
            System.out.println(ejb.PrintOrder(qrCode));
        } catch (Exception e) {
            System.out.println("Error occurred : " + e.getMessage());
            e.printStackTrace();
        } // end of try-catch        
    }    
    
    private static void BillOrder(){
        try{
            String qrCode = getUserInput("QR Code : ", "^(?!\\s*$).+");
            System.out.println(ejb.CloseOrder(qrCode));
        } catch (Exception e) {
            System.out.println("Error occurred : " + e.getMessage());
            e.printStackTrace();
        } // end of try-catch        
    }    
    
    private static String getUserInput(String _msg, String possibleValueRegx) throws IOException {
        String _cmd = "";

        do {
            // Send the variable _msg to client and collect user response
            System.out.println(_msg);
            _cmd = ReadMsgFromClient();

            if (_cmd.isEmpty()
                    || possibleValueRegx.isEmpty()
                    || Pattern.compile(possibleValueRegx, Pattern.CASE_INSENSITIVE).matcher(_cmd).matches()) {
                break;
            }

            // Send the error message to client.
            System.out.println("Possible Value : [" + possibleValueRegx + "]. Please try again.");
        } while (true);

        return _cmd;
    }// end of getUserInput(String _msg, String possibleValueRegx)  
    
    private static String ReadMsgFromClient() {
        String input = "";
        try {
            // Question 6. get the user input and assign to _cmd
            input = in.readLine();
            input = (input == null) ? "" : input;
        } catch (Exception e) {
            System.out.println("Error occurred : " + e.getMessage());
            e.printStackTrace();
        }
        return input;
    }// end of ReadMsgFromClient(String msg)   
    
    private static void CloseConn() {
        System.out.println("Goodbye!\r\nPress any key to continue..");
    }// end of CloseConn()    
}//end of class Main