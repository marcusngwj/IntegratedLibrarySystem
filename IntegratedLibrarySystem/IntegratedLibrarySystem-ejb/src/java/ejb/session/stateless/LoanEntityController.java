package ejb.session.stateless;

import entity.LoanEntity;
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
import util.helper.DateHelper;
import util.logger.Logger;

@Stateless
@Local(LoanEntityControllerLocal.class)
@Remote(LoanEntityControllerRemote.class)
public class LoanEntityController implements LoanEntityControllerRemote, LoanEntityControllerLocal {
    
    @PersistenceContext(unitName = "IntegratedLibrarySystem-ejbPU")
    private EntityManager em;
    
    @Override
    public LoanEntity persistNewLoanEntity(LoanEntity newLoan) throws LoanException {
        Logger.log(Logger.INFO, "LoanEntityController", "persistNewLoanEntity");
        
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
        
        Date newDate = DateHelper.addDaysToToday(DateHelper.WEEK_OF_MONTH, DateHelper.LOAN_DURATION);
        newLoan.setEndDate(newDate);

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
        if (isLoanOverdue(loanToUpdate)) {
            throw new LoanException(LoanException.BOOK_IS_OVERDUE);
        }
        
        // TODO: Check unpaid fines
        
        // TODO: Check reservation
        
        Date newDueDate = DateHelper.addDaystoDate(loanToUpdate.getEndDate(), DateHelper.WEEK_OF_MONTH, DateHelper.LOAN_DURATION);
        loanToUpdate.setEndDate(newDueDate);
        
        em.merge(loanToUpdate);
        
        return loanToUpdate;
    }
    
    @Override
    public void deleteLoan(Long bookId) throws LoanNotFoundException {
        LoanEntity loanToRemove = retrieveLoanByBookId(bookId);
        em.remove(loanToRemove);
    }
    
    private static boolean isLoanOverdue(LoanEntity loan) {
        return DateHelper.isDateAfterToday(loan.getEndDate());
    }

}
