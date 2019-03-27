package libraryadminterminal;

import ejb.session.stateless.BookEntityControllerRemote;
import ejb.session.stateless.LoanEntityControllerRemote;
import ejb.session.stateless.MemberEntityControllerRemote;
import ejb.session.stateless.StaffEntityControllerRemote;
import javax.ejb.EJB;

public class Main {
    @EJB
    private static StaffEntityControllerRemote staffEntityControllerRemote;
    @EJB
    private static MemberEntityControllerRemote memberEntityControllerRemote;
    @EJB
    private static BookEntityControllerRemote bookEntityControllerRemote;
    @EJB
    private static LoanEntityControllerRemote loanEntityControllerRemote;

    public static void main(String[] args) {
        MainApp mainApp = new MainApp(staffEntityControllerRemote, memberEntityControllerRemote, bookEntityControllerRemote, loanEntityControllerRemote);
        mainApp.runApp();
    }
    
}
