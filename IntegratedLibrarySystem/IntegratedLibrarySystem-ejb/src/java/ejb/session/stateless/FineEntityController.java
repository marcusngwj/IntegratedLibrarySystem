package ejb.session.stateless;

import entity.FineEntity;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.FineNotFoundException;
import util.logger.Logger;

@Stateless
@Local(FineEntityControllerLocal.class)
@Remote(FineEntityControllerRemote.class)
public class FineEntityController implements FineEntityControllerRemote, FineEntityControllerLocal {

    @PersistenceContext(unitName = "IntegratedLibrarySystem-ejbPU")
    private EntityManager em;
    
    @Override
    public FineEntity persistNewFineEntity(FineEntity newFine) {
        Logger.log(Logger.INFO, "FineEntityController", "persistNewFineEntity");
        
        em.persist(newFine);
        em.flush();
        em.refresh(newFine);
        return newFine;
    }
    
    public List<FineEntity> retrieveFinesByMemberIdentityNumber(String memberIdentityNumber) throws FineNotFoundException {
        Query query = em.createQuery("SELECT f FROM FineEntity f WHERE f.member.identityNumber = :inIdentityNumber");
        query.setParameter("inIdentityNumber", memberIdentityNumber);
        
        return query.getResultList();
    }
    
}
