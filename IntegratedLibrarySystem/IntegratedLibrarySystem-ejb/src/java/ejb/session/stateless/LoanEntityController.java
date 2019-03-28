package ejb.session.stateless;

import entity.FineEntity;
import entity.LoanEntity;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
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
    
    @EJB
    private FineEntityControllerLocal fineEntityControllerLocal;
    
    @Override
    public LoanEntity createNewLoanEntity(LoanEntity newLoan) throws LoanException {
        Logger.log(Logger.INFO, "LoanEntityController", "persistNewLoanEntity");
        
        List<LoanEntity> loanList = retrieveLoansByMemberId(newLoan.getMember().getMemberId());
        if (loanList.size() >= 3) {
            throw new LoanException(LoanException.EXCEED_LOAN_LIMIT);
        }
        
        List<FineEntity> fineList = fineEntityControllerLocal.retrieveFinesByMemberIdentityNumber(newLoan.getMember().getIdentityNumber());
        if (fineList.size() > 0) {
            throw new LoanException(LoanException.UNPAID_FINE);
        }
        
        // TODO: Check for reservation
        
        loanList = retrieveAllLoans();
        for (LoanEntity loan : loanList) {
            if (loan.getBook().getBookId().equals(newLoan.getBook().getBookId())) {
                throw new LoanException(LoanException.BOOK_ON_LOAN);
            }
        }
        
        Date newDate = DateHelper.addDaysToToday(DateHelper.WEEK_OF_MONTH, DateHelper.WEEKS_FOR_LOAN);
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
    public LoanEntity extendLoan(LoanEntity loanToUpdate) throws LoanException {
        if (isLoanOverdue(loanToUpdate)) {
            throw new LoanException(LoanException.BOOK_IS_OVERDUE);
        }
        
        List<FineEntity> fineList = fineEntityControllerLocal.retrieveFinesByMemberIdentityNumber(loanToUpdate.getMember().getIdentityNumber());
        if (fineList.size() > 0) {
            throw new LoanException(LoanException.UNPAID_FINE);
        }
        
        // TODO: Check reservation
        
        Date newDueDate = DateHelper.addDaystoDate(loanToUpdate.getEndDate(), DateHelper.WEEK_OF_MONTH, DateHelper.WEEKS_FOR_LOAN);
        loanToUpdate.setEndDate(newDueDate);
        
        em.merge(loanToUpdate);
        
        return loanToUpdate;
    }
    
    @Override
    public void deleteLoan(Long bookId) throws LoanNotFoundException {
        LoanEntity loan = retrieveLoanByBookId(bookId);
        
        if (isLoanOverdue(loan)) {
            fineEntityControllerLocal.createNewFineEntity(loan, loan.getMember());
        }
        
        em.remove(loan);
    }
    
    private static boolean isLoanOverdue(LoanEntity loan) {
        return DateHelper.isDateAfterToday(loan.getEndDate());
    }

}
