/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package selfservicekioskterminal;

import ejb.session.stateless.BookEntityControllerRemote;
import ejb.session.stateless.LoanEntityControllerRemote;
import ejb.session.stateless.MemberEntityControllerRemote;
import javax.ejb.EJB;

/**
 *
 * @author
 */
public class Main {
    @EJB
    private static MemberEntityControllerRemote memberEntityControllerRemote;
    @EJB
    private static BookEntityControllerRemote bookEntityControllerRemote;
    @EJB
    private static LoanEntityControllerRemote loanEntityControllerRemote;
    
    /**
     *  
    
   
    @EJB
    private static ReservationControllerRemote reservationControllerRemote;
    @EJB
    private static FineControllerRemote fineControllerRemote;
   

    */
    public static void main(String[] args) {
//        MainApp mainApp = new MainApp(bookControllerRemote, loanControllerRemote, reservationControllerRemote, fineControllerRemote, memberEntityControllerRemote);
        MainApp mainApp = new MainApp(memberEntityControllerRemote, bookEntityControllerRemote, loanEntityControllerRemote);
        mainApp.runApp();
    }
    
}
