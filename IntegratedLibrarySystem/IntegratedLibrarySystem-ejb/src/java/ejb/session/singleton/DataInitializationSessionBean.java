/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.StaffEntityControllerLocal;
import entity.StaffEntity;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.StaffNotFoundException;
import util.logger.Logger;

/**
 *
 * @author Marcus
 */

@Singleton
@LocalBean
@Startup

public class DataInitializationSessionBean {   
    @PersistenceContext(unitName = "IntegratedLibrarySystem-ejbPU")
    private EntityManager entityManager;
    
    @EJB
    private StaffEntityControllerLocal staffEntityControllerLocal;
        
    public DataInitializationSessionBean() {}
    
    @PostConstruct
    public void postConstruct() {               
        verifyStaffEntityTable();
    }
    
    private void verifyStaffEntityTable() {
        try {
            staffEntityControllerLocal.retrieveStaffByUsername("manager");
        }
        catch(StaffNotFoundException ex) {
            Logger.log(Logger.INFO, "DataInitializationSessionBean", "verifyStaffEntityTable", "StaffEntityTable is empty");
            initializeStaffEntityTable();
        }
    }
    
    private void initializeStaffEntityTable() {
        StaffEntity staffEntity = new StaffEntity("Linda", "Chua", "manager", "password");
        staffEntity = staffEntityControllerLocal.persistNewStaffEntity(staffEntity);
        staffEntity = new StaffEntity("Barbara", "Durham", "assistant", "password");
        staffEntity = staffEntityControllerLocal.persistNewStaffEntity(staffEntity);
    }
}
