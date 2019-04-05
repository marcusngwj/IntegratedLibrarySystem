/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.BookEntityControllerLocal;
import ejb.session.stateless.MemberEntityControllerLocal;
import ejb.session.stateless.StaffEntityControllerLocal;
import entity.BookEntity;
import entity.MemberEntity;
import entity.StaffEntity;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.BookEntityException;
import util.exception.BookNotFoundException;
import util.exception.MemberEntityException;
import util.exception.MemberExistsException;
import util.exception.MemberNotFoundException;
import util.exception.StaffEntityException;
import util.exception.StaffExistsException;
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
    @EJB
    private MemberEntityControllerLocal memberEntityControllerLocal;
    @EJB
    private BookEntityControllerLocal bookEntityControllerLocal;
        
    public DataInitializationSessionBean() {}
    
    @PostConstruct
    public void postConstruct() {               
        verifyStaffEntityTable();
        verifyMemberEntityTable();
        verifyBookEntityTable();
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
    
    private void verifyMemberEntityTable() {
        try {
            memberEntityControllerLocal.retrieveMemberById((long)1);
        }
        catch(MemberNotFoundException ex) {
            Logger.log(Logger.INFO, "DataInitializationSessionBean", "verifyMemberEntityTable", "MemberEntityTable is empty");
            initializeMemberEntityTable();
        }
    }
    
    private void verifyBookEntityTable() {
        try {
            bookEntityControllerLocal.retrieveBookById((long)1);
        }
        catch(BookNotFoundException ex) {
            Logger.log(Logger.INFO, "DataInitializationSessionBean", "verifyBookEntityTable", "BookEntityTable is empty");
            initializeBookEntityTable();
        }
    }
    
    private void initializeStaffEntityTable() {
        try {
            StaffEntity staffEntity = new StaffEntity("Linda", "Chua", "manager", "password");
            staffEntity = staffEntityControllerLocal.persistNewStaffEntity(staffEntity);
            staffEntity = new StaffEntity("Barbara", "Durham", "assistant", "password");
            staffEntity = staffEntityControllerLocal.persistNewStaffEntity(staffEntity);
        }
        catch (StaffExistsException | StaffEntityException ex) {
            Logger.log(Logger.SEVERE, "DataInitializationSessionBean", "initializeStaffEntityTable", ex.getMessage());
        }
    }
    
    private void initializeMemberEntityTable() {
        try {
            MemberEntity memberEntity = new MemberEntity("S7483027A", "987654", "Tony", "Teo", "Male", "44", "87297373", "11 Tampines Ave 3");
            memberEntity = memberEntityControllerLocal.persistNewMemberEntity(memberEntity);
            memberEntity = new MemberEntity("S8381028X", "456789", "Wendy", "Tan", "Female", "35", "97502837", "15 Computing Dr");
            memberEntity = memberEntityControllerLocal.persistNewMemberEntity(memberEntity);
        }
        catch (MemberExistsException | MemberEntityException ex) {
            Logger.log(Logger.SEVERE, "DataInitializationSessionBean", "initializeMemberEntityTable", ex.getMessage());
        }
    }
    
    private void initializeBookEntityTable() {
        try {
            BookEntity bookEntity = new BookEntity("The Lord of the Rings", "S18018", "1954");
            bookEntity = bookEntityControllerLocal.persistNewBookEntity(bookEntity);
            bookEntity = new BookEntity("Le Petit Prince", "S64921", "1943");
            bookEntity = bookEntityControllerLocal.persistNewBookEntity(bookEntity);
            bookEntity = new BookEntity("Harry Potter and the Philosopher's Stone", "S38101", "1997");
            bookEntity = bookEntityControllerLocal.persistNewBookEntity(bookEntity);
            bookEntity = new BookEntity("The Hobbit", "S19527", "1937");
            bookEntity = bookEntityControllerLocal.persistNewBookEntity(bookEntity);
            bookEntity = new BookEntity("And Then There Were None", "S63288", "1939");
            bookEntity = bookEntityControllerLocal.persistNewBookEntity(bookEntity);
            bookEntity = new BookEntity("Dream of the Red Chamber", "S32187", "1791");
            bookEntity = bookEntityControllerLocal.persistNewBookEntity(bookEntity);
            bookEntity = new BookEntity("The Lion, the Witch and the Wardrobe", "S74569", "1950");
            bookEntity = bookEntityControllerLocal.persistNewBookEntity(bookEntity);
        }
        catch (BookEntityException ex) {
            Logger.log(Logger.SEVERE, "DataInitializationSessionBean", "initializeBookEntityTable", ex.getMessage());
        }
        
    }
}
