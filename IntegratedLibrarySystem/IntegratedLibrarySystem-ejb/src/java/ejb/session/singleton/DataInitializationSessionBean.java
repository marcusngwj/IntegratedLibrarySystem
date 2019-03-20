/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.StaffEntityControllerLocal;
import entity.StaffEntity;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
//        try {
//            List<StaffEntity> staffList = staffEntityControllerLocal.retrieveAllStaffs();
//        }
//        catch(EntityManagerException ex) {
            initializeStaffEntityTable();
//        }
    }
    
    private void initializeStaffEntityTable() {
        StaffEntity staffEntity = new StaffEntity("Linda", "Chua", "manager", "password");
        staffEntity = staffEntityControllerLocal.persistNewStaffEntity(staffEntity);
        staffEntity = new StaffEntity("Barbara", "Durham", "assistant", "password");
        staffEntity = staffEntityControllerLocal.persistNewStaffEntity(staffEntity);
    }
}
