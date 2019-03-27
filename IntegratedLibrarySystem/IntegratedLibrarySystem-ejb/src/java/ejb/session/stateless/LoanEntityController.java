package ejb.session.stateless;

import entity.LoanEntity;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.LoanException;
import util.exception.LoanNotFoundException;
import util.logger.Logger;

@Stateless
@Local(LoanEntityControllerLocal.class)
@Remote(LoanEntityControllerRemote.class)
public class LoanEntityController implements LoanEntityControllerRemote, LoanEntityControllerLocal {
    
    private static DateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
    private static int LOAN_DURATION = 2;

    @PersistenceContext(unitName = "IntegratedLibrarySystem-ejbPU")
    private EntityManager em;
    
    @Override
    public LoanEntity persistNewLoanEntity(LoanEntity newLoan) throws LoanException {
        Logger.log(Logger.SEVERE, "LoanEntityController", "persistNewLoanEntity");
        
        List<LoanEntity> loanList = retrieveLoansByMemberId(newLoan.getMember().getMemberId());
        if (loanList.size() >= 3) {
            throw new LoanException(LoanException.EXCEED_LOAN_LIMIT);
        }
        
        // TODO: Check for fine
        
        // TODO: Check for reservation
        
        loanList = retrieveAllLoans();
        for (LoanEntity loan : loanList) {
            if (loan.getBook().getBookId().equals(newLoan.getBook().getBookId())) {
                throw new LoanException(LoanException.BOOK_ON_LOAN);
            }
        }
        
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.WEEK_OF_MONTH, LOAN_DURATION);
        newLoan.setEndDate(cal.getTime());

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
    
    @Override
    public LoanEntity retrieveLoanByBookId(Long bookId) throws LoanNotFoundException {
        Query query = em.createQuery("SELECT l FROM LoanEntity l WHERE l.book.bookId = :inBookId");
        query.setParameter("inBookId", bookId);
        
        try {
            return (LoanEntity)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex) {
            throw new LoanNotFoundException("Loan with book id " + bookId + " does not exist!");
        }
    }
    
    @Override
    public LoanEntity updateLoan(LoanEntity loanToUpdate) throws LoanException {
        Date today = Calendar.getInstance().getTime();
        
        if (today.after(loanToUpdate.getEndDate())) {
            throw new LoanException(LoanException.BOOK_IS_OVERDUE);
        }
        
        // TODO: Check unpaid fines
        
        // TODO: Check reservation
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(loanToUpdate.getEndDate());
        cal.add(Calendar.WEEK_OF_MONTH, LOAN_DURATION);
        loanToUpdate.setEndDate(cal.getTime());
        
        em.merge(loanToUpdate);
        
        return loanToUpdate;
    }

}
