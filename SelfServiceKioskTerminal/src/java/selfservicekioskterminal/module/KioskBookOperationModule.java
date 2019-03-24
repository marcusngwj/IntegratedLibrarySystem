/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package selfservicekioskterminal.module;

/**
 *
 * @author limwe
 */
public class KioskBookOperationModule {

    public void enterSearchBook() {
         printSearchBookMain();
    }

    private void printSearchBookMain() {
        System.out.println("*** Self-Service Kiosk :: Search Book ***\n");

        System.out.println("Enter Title to Search> ");
    }

}
