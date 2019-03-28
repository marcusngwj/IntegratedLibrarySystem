package ejb.session.stateless;

import entity.FineEntity;
import entity.LoanEntity;
import entity.MemberEntity;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.FineNotFoundException;
import util.helper.DateHelper;
import util.logger.Logger;

@Stateless
@Local(FineEntityControllerLocal.class)
@Remote(FineEntityControllerRemote.class)
public class FineEntityController implements FineEntityControllerRemote, FineEntityControllerLocal {
    private static final long FIX_FINE_RATE = 1;
    
    @PersistenceContext(unitName = "IntegratedLibrarySystem-ejbPU")
    private EntityManager em;
    
    @Override
    public FineEntity createNewFineEntity(LoanEntity loan, MemberEntity member) {
        Logger.log(Logger.INFO, "FineEntityController", "persistNewFineEntity");
        
        long fineAmt = calculateFine(loan);
        FineEntity newFine = new FineEntity(fineAmt, member);
        
        em.persist(newFine);
        em.flush();
        em.refresh(newFine);
        return newFine;
    }
    
    @Override
    public FineEntity retrieveFineByFindId(Long fineId) throws FineNotFoundException {
        Query query = em.createQuery("SELECT f FROM FineEntity f WHERE f.fineId = :inFineId");
        query.setParameter("inFineId", fineId);
        
        try {
            return (FineEntity)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex) {
            throw new FineNotFoundException("Fine with fine id " + fineId + " does not exist!");
        }
    }
    
    @Override
    public List<FineEntity> retrieveFinesByMemberIdentityNumber(String memberIdentityNumber) {
        Query query = em.createQuery("SELECT f FROM FineEntity f WHERE f.member.identityNumber = :inIdentityNumber");
        query.setParameter("inIdentityNumber", memberIdentityNumber);
        
        return query.getResultList();
    }
    
    @Override
    public void deleteFine(Long fineId) throws FineNotFoundException {
        FineEntity fineToRemove = retrieveFineByFindId(fineId);
        em.remove(fineToRemove);
    }
    
    private static long calculateFine(LoanEntity loan) {
        long daysOverdue = DateHelper.getNumDaysFromDate(loan.getEndDate());
        return daysOverdue * FIX_FINE_RATE;
    }
    
}
