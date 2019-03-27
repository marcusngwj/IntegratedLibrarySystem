/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.LoanEntity;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.StillLoaningException;

/**
 *
 * @author limwe
 */
@Stateless
@Local(LoanEntityControllerLocal.class)
@Remote(LoanEntityControllerRemote.class)
public class LoanEntityController implements LoanEntityControllerRemote, LoanEntityControllerLocal {

    @PersistenceContext(unitName = "IntegratedLibrarySystem-ejbPU")
    private EntityManager em;

    public LoanEntityController() {}
    
   
    @Override
    public LoanEntity persistNewLoanEntity(LoanEntity newLoan) throws StillLoaningException{
        List<LoanEntity> loanList = retrieveAllLoans();
        //Check through all the loan, obtain loans with same bookId, compare existing loan endDate
        //with current date
        for (LoanEntity loan : loanList) {
            if (loan.getBook().getBookId() == newLoan.getBook().getBookId() 
               //     && checkLoaned(loan)
                    ) {
                throw new StillLoaningException(StillLoaningException.BOOK_STILL_LOANING);
            }
        }

        em.persist(newLoan);
        em.flush();
        em.refresh(newLoan);
        return newLoan;
    }
    
    private boolean checkLoaned(LoanEntity loan) {
        Date currDate = new Date();
        Date loanedDate = loan.getEndDate();
        if(currDate.compareTo(loanedDate) > 0) {
            return false;
        } else {
            return true;
        }
    }
    @Override
    public List<LoanEntity> retrieveAllLoans() {
        Query query = em.createQuery("SELECT l FROM LoanEntity l");
        return query.getResultList();
    }

}
