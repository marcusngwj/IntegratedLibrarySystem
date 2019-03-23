package libraryadminterminal;

import ejb.session.stateless.BookEntityControllerRemote;
import ejb.session.stateless.StaffEntityControllerRemote;
import javax.ejb.EJB;

public class Main {
    @EJB
    private static StaffEntityControllerRemote staffEntityControllerRemote;
    @EJB
    private static BookEntityControllerRemote bookEntityControllerRemote;

    public static void main(String[] args) {
        MainApp mainApp = new MainApp(staffEntityControllerRemote, bookEntityControllerRemote);
        mainApp.runApp();
    }
    
}
