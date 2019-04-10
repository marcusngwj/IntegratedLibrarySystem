package selfservicekioskterminal.module;

import ejb.session.stateless.MemberEntityControllerRemote;
import entity.MemberEntity;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.exception.MemberEntityException;
import util.exception.MemberExistsException;

public class KioskRegistrationOperationModule {

    MemberEntityControllerRemote memberEntityControllerRemote;
    private final String REGISTER_SUCCESS = "You have been registered successfully!";

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

        try {
            MemberEntity newMember = new MemberEntity(identityNumber, securityCode, firstName, lastName, gender, age, phone, address);
            newMember = memberEntityControllerRemote.createNewMember(newMember);
            System.out.println(REGISTER_SUCCESS);
            System.out.println();
        } catch (MemberExistsException | MemberEntityException ex) {
            //Logger.getLogger(KioskRegistrationOperationModule.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
        }
    }
}
