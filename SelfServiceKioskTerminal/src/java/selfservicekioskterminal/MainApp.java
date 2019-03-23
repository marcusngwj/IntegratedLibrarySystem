/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package selfservicekioskterminal;

import ejb.session.stateless.MemberEntityControllerRemote;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import selfservicekioskterminal.module.RegisterationKioskOperationModule;

/**
 *
 * @author limwe
 */
public class MainApp {

    private MemberEntityControllerRemote memberEntityControllerRemote;
    private RegisterationKioskOperationModule registerationKioskOperationModule;

    MainApp(MemberEntityControllerRemote memberEntityControllerRemote) {
        this.memberEntityControllerRemote = memberEntityControllerRemote;
    }

    void runApp() {
        final int REGISTER_NUMBER = 1;
        final int LOGIN_NUMBER = 2;
        final int EXIT_NUMBER = 3;

        while (true) {
            int response = 0;
            printKioskWelcomeMessage();
            response = getUserResponse();
            
            if(response == REGISTER_NUMBER) {
                registerationKioskOperationModule.doRegisterMember();
            } else if(response == LOGIN_NUMBER) {
                
            } else if (response == EXIT_NUMBER) {
                
            } else {
                printInvalidResponseMessage();
            }
        }
    }
   
   
    
    private void printInvalidResponseMessage() {
        System.out.println("Invalid Option Chosen.");
    }
    private void printKioskWelcomeMessage() {
        System.out.println("*** Welcome to Self-Service Kiosk ***\n"
                + "1: Register\n"
                + "2: Login\n"
                + "3: Exit\n");
    }

    private void printKioskRegisterMessage() {
        System.out.println("*** Self-Service Kiosk :: Register ***\n");
    }

    private int getUserResponse() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("> ");
        return scanner.nextInt();
    }
}
