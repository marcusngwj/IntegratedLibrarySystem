/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package selfservicekioskterminal.module;

import ejb.session.stateless.MemberEntityControllerRemote;
import entity.MemberEntity;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import util.exception.MemberExistsException;

/**
 *
 * @author limwe
 */
public class KioskRegistrationOperationModule {
    MemberEntityControllerRemote memberEntityControllerRemote;
    private final String REGISTER_SUCCESS = "You have been registered successfully!";
    private final String INVALID_ID = "Invalid ID or Duplicate Identity Number! Please try again.";
    private final String INVALID_CODE = "Invalid Security Code. Please try again.";
    private final String INVALID_NAME = "Invalid Name. Please try again.";
    private final String INVALID_GENDER = "Invalid Gender Format. Please try again.";
    private final String INVALID_AGE = "Invalid Age Format. Please try again.";
    private final String INVALID_PHONE = "Invalid Phone Format. Please try again";
    private final String INVALID_ADDRESS = "Invalid Address Format. Please try again";
    
    public KioskRegistrationOperationModule() {
    }
    public KioskRegistrationOperationModule(MemberEntityControllerRemote memberEntityControllerRemote) {
          this.memberEntityControllerRemote = memberEntityControllerRemote;
    }

    public void doRegisterMember() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Identity Number> ");
        String identityNumber = scanner.nextLine().trim();
        System.out.print("Enter Security Code> ");
        String securityCode = scanner.nextLine().trim();
        System.out.print("Enter First Name> ");
        String firstName = scanner.nextLine().trim();
        System.out.print("Enter Last Name> ");
        String lastName = scanner.nextLine().trim();
        System.out.print("Enter Gender> ");
        String gender = scanner.nextLine().trim();
        System.out.print("Enter Age> ");
        Integer age = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Phone> ");
        String phone = scanner.nextLine().trim();
        System.out.print("Enter Address> ");
        String address = scanner.nextLine().trim();

        //Check for identityNumber as it has to be unique
        boolean idValid = checkIdValidity(identityNumber);
        boolean securityCodeValid = checkSecurityCodeValidity(securityCode);
        boolean nameValid = checkNameValidity(firstName, lastName);
        boolean genderValid = checkGenderValidity(gender);
        boolean ageValid = checkAgeValidity(age);
        boolean phoneValid = checkPhoneValidity(phone);
        boolean addressValid = checkAddressValidity(address);
        
        
        if(idValid && securityCodeValid && nameValid && genderValid && ageValid && phoneValid && addressValid) {
            try {
                MemberEntity newMember = new MemberEntity(identityNumber, securityCode, firstName, lastName, gender, age, phone, address);
                newMember = memberEntityControllerRemote.createNewMember(newMember);
                System.out.println(REGISTER_SUCCESS);
            } catch (MemberExistsException ex) {
                Logger.getLogger(KioskRegistrationOperationModule.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        printInvalidMessages(idValid, securityCodeValid, nameValid, genderValid , ageValid, phoneValid, addressValid);
      
    }
    private boolean checkAddressValidity(String address) {
        //TO-DO
        return true;
    }
    
    private boolean checkPhoneValidity(String phone) {
        //TO-DO
        return true;
    }
    private boolean checkAgeValidity(Integer age) {
        //TO-DO
        return true;
    }
    
    private boolean checkGenderValidity(String gender) {
        //TO-DO
        return true;
    }
    private boolean checkNameValidity(String firstName, String lastName) {
        //TO-DO
        return true;
    }
    private boolean checkSecurityCodeValidity(String securityCode) {
        final String SECURITY_PATTERN =  "[0-9]{6}";
        Pattern securityPattern = Pattern.compile(SECURITY_PATTERN);
        Matcher securityMatcher = securityPattern.matcher(securityCode);
        
        if(!securityMatcher.matches()) {
            return false; 
        } else {
            return true;
        }
    }
    private boolean checkIdValidity(String identityNumber) {
        //Does initial regex check
        final String ID_PATTERN = "^[a-zA-Z]{1}?[0-9]{7}[a-zA-Z]{1}?";
        Pattern idPattern = Pattern.compile(ID_PATTERN);
        Matcher idMatcher = idPattern.matcher(identityNumber);

        if (!idMatcher.matches()) {
            return false;
        }
        //Next check the validity by checking if there is an existing idNumber
        //To-DO
        return true;
    }

    private void printInvalidMessages(boolean idValid, boolean securityCodeValid, boolean nameValid, boolean genderValid, boolean ageValid, boolean phoneValid, boolean addressValid) {
        if(!idValid) {
            System.out.println(INVALID_ID);
        }
        if(!securityCodeValid) {
            System.out.println(INVALID_CODE);
        }
        if(!nameValid) {
            System.out.println(INVALID_NAME);
        }
        if(!genderValid) {
            System.out.println(INVALID_GENDER);
        }
        if(!phoneValid) {
            System.out.println(INVALID_PHONE);
        }
        
        if(!addressValid) {
            System.out.println(INVALID_ADDRESS);
        }
    }
}
