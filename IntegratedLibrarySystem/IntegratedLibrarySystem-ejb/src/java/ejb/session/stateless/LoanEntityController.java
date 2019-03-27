package ejb.session.stateless;

import entity.LoanEntity;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.UnsuccessfulLoanException;
import util.logger.Logger;

@Stateless
@Local(LoanEntityControllerLocal.class)
@Remote(LoanEntityControllerRemote.class)
public class LoanEntityController implements LoanEntityControllerRemote, LoanEntityControllerLocal {

    @PersistenceContext(unitName = "IntegratedLibrarySystem-ejbPU")
    private EntityManager em;
    
    @Override
    public LoanEntity persistNewLoanEntity(LoanEntity newLoan) throws UnsuccessfulLoanException {
        Logger.log(Logger.SEVERE, "LoanEntityController", "persistNewLoanEntity");
        
        List<LoanEntity> loanList = retrieveLoansByMemberId(newLoan.getMember().getMemberId());
        if (loanList.size() >= 3) {
            throw new UnsuccessfulLoanException(UnsuccessfulLoanException.EXCEED_LOAN_LIMIT);
        }
        
        // TODO: Check for fine
        
        // TODO: Check for reservation
        
        loanList = retrieveAllLoans();
        for (LoanEntity loan : loanList) {
            if (loan.getBook().getBookId().equals(newLoan.getBook().getBookId())) {
                throw new UnsuccessfulLoanException(UnsuccessfulLoanException.BOOK_ON_LOAN);
            }
        }

        em.persist(newLoan);
        em.flush();
        em.refresh(newLoan);
        return newLoan;
    }
    
    @Override
    public List<LoanEntity> retrieveAllLoans() {
        Query query = em.createQuery("SELECT l FROM LoanEntity l");
        return query.getResultList();
    }
    
    @Override
    public List<LoanEntity> retrieveLoansByMemberId(Long memberId) {
        Query query = em.createQuery("SELECT l FROM LoanEntity l WHERE l.member.memberId = :inMemberId");
        query.setParameter("inMemberId", memberId);
        
        return query.getResultList();
    }

}
