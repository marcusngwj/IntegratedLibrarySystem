package libraryadminterminal;

import ejb.session.stateless.StaffEntityControllerRemote;
import javax.ejb.EJB;

public class Main {
    @EJB
    private static StaffEntityControllerRemote staffEntityControllerRemote;

    public static void main(String[] args) {
        MainApp mainApp = new MainApp(staffEntityControllerRemote);
        mainApp.runApp();
    }
    
}
