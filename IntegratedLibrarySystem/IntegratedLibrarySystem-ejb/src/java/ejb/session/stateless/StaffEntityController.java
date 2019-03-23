package ejb.session.stateless;

import entity.StaffEntity;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.InvalidLoginException;
import util.exception.StaffExistsException;
import util.exception.StaffNotFoundException;
import util.logger.Logger;

@Stateless
@Local(StaffEntityControllerLocal.class)
@Remote(StaffEntityControllerRemote.class)
public class StaffEntityController implements StaffEntityControllerRemote, StaffEntityControllerLocal {

    @PersistenceContext(unitName = "IntegratedLibrarySystem-ejbPU")
    private EntityManager em;
    
    public StaffEntityController() {}
    
    @Override
    public StaffEntity persistNewStaffEntity(StaffEntity newStaff) throws StaffExistsException {
        List<StaffEntity> staffList = retrieveAllStaffs();
        for (StaffEntity staff : staffList) {
            if (!staff.getStaffId().equals(newStaff.getStaffId()) && staff.getUsername().equals(newStaff.getUsername())) {
                throw new StaffExistsException("The username has already been taken. Please try again.");
            }
        }
        
        em.persist(newStaff);
        em.flush();
        em.refresh(newStaff);
        return newStaff;
    }
    
    @Override
    public List<StaffEntity> retrieveAllStaffs() {
        Query query = em.createQuery("SELECT s FROM StaffEntity s");
        return query.getResultList();
    }
    
    @Override
    public StaffEntity retrieveStaffById(Long staffId) throws StaffNotFoundException {
        Query query = em.createQuery("SELECT s FROM StaffEntity s WHERE s.staffId = :inStaffId");
        query.setParameter("inStaffId", staffId);
        
        try {
            return (StaffEntity)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex) {
            throw new StaffNotFoundException("Staff Id " + staffId + " does not exist!");
        }
    }
    
    @Override
    public StaffEntity retrieveStaffByUsername(String username) throws StaffNotFoundException {
        Query query = em.createQuery("SELECT s FROM StaffEntity s WHERE s.username = :inUsername");
        query.setParameter("inUsername", username);
        
        try {
            return (StaffEntity)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex) {
            throw new StaffNotFoundException("Staff Username " + username + " does not exist!");
        }
    }
    
    @Override
    public void updateStaff(StaffEntity staffToUpdate) throws StaffExistsException {
        List<StaffEntity> staffList = retrieveAllStaffs();
        for (StaffEntity staff : staffList) {
            if (!staff.getStaffId().equals(staffToUpdate.getStaffId()) && staff.getUsername().equals(staffToUpdate.getUsername())) {
                throw new StaffExistsException("The username has already been taken. Please try again.");
            }
        }
        
        em.merge(staffToUpdate);
    }
    
    @Override
    public void deleteStaff(Long staffId) throws StaffNotFoundException {
        StaffEntity staffToRemove = retrieveStaffById(staffId);
        em.remove(staffToRemove);
    }
    
    @Override
    public StaffEntity staffLogin(String username, String password) throws InvalidLoginException {
        Logger.log(Logger.INFO, "StaffEntityController", "staffLogin", username + " || " + password);
        Query query = em.createQuery("SELECT s FROM StaffEntity s WHERE s.username = :inUsername AND s.password = :inPassword");
        query.setParameter("inUsername", username);
        query.setParameter("inPassword", password);
        
        try {
            return (StaffEntity)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex) {
            throw new InvalidLoginException(InvalidLoginException.INVALID_CREDENTIALS);
        }
    }
}
