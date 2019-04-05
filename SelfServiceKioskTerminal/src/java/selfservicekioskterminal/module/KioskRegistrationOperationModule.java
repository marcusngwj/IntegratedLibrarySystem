package selfservicekioskterminal.module;

import ejb.session.stateless.MemberEntityControllerRemote;
import entity.MemberEntity;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.exception.MemberExistsException;
import util.helper.CredentialFormatHelper;

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
        String age = scanner.nextLine().trim();
        System.out.print("Enter Phone> ");
        String phone = scanner.nextLine().trim();
        System.out.print("Enter Address> ");
        String address = scanner.nextLine().trim();

        //Check for identityNumber as it has to be unique
        boolean idValid = isIdentityNumberValid(identityNumber);
        boolean securityCodeValid = CredentialFormatHelper.isValidSecurityCodeFormat(securityCode);
        boolean nameValid = isNameValid(firstName, lastName);
        boolean genderValid = CredentialFormatHelper.isValidGenderFormat(gender);
        boolean ageValid = CredentialFormatHelper.isValidAgeFormat(age);
        boolean phoneValid = CredentialFormatHelper.isValidPhoneFormat(phone);
        boolean addressValid = CredentialFormatHelper.isValidAddressFormat(address);
        
        if (idValid && securityCodeValid && nameValid && genderValid && ageValid && phoneValid && addressValid) {
            try {
                gender = CredentialFormatHelper.convertToStandardGenderFormat(gender);
                MemberEntity newMember = new MemberEntity(identityNumber, securityCode, firstName, lastName, gender, Integer.valueOf(age), phone, address);
                newMember = memberEntityControllerRemote.createNewMember(newMember);
                System.out.println(REGISTER_SUCCESS);
            } catch (MemberExistsException ex) {
                Logger.getLogger(KioskRegistrationOperationModule.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        printInvalidMessages(idValid, securityCodeValid, nameValid, genderValid, ageValid, phoneValid, addressValid);

    }
    
    private boolean isIdentityNumberValid(String identityNumber) {
        boolean isValid = CredentialFormatHelper.isValidMemberIdentityNumberFormat(identityNumber);
        
        //To-DO: check the validity by checking if there is an existing idNumber
        
        return isValid;
    }
    
    private boolean isNameValid(String firstName, String lastName) {
        return CredentialFormatHelper.isValidNameFormat(firstName) && CredentialFormatHelper.isValidNameFormat(lastName);
    }
    
    private void printInvalidMessages(boolean idValid, boolean securityCodeValid, boolean nameValid, boolean genderValid, boolean ageValid, boolean phoneValid, boolean addressValid) {
        if (!idValid) {
            System.out.println(INVALID_ID);
        }
        if (!securityCodeValid) {
            System.out.println(INVALID_CODE);
        }
        if (!nameValid) {
            System.out.println(INVALID_NAME);
        }
        if (!genderValid) {
            System.out.println(INVALID_GENDER);
        }
        if (!ageValid) {
            System.out.println(INVALID_AGE);
        }
        if (!phoneValid) {
            System.out.println(INVALID_PHONE);
        }
        if (!addressValid) {
            System.out.println(INVALID_ADDRESS);
        }
    }
}
