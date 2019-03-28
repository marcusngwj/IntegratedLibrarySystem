package ejb.session.stateless;

import entity.LoanEntity;
import java.util.List;
import util.exception.LoanException;
import util.exception.LoanNotFoundException;

public interface LoanEntityControllerLocal {
    public LoanEntity createNewLoanEntity(LoanEntity newLoan) throws LoanException;

    public List<LoanEntity> retrieveAllLoans();

    public List<LoanEntity> retrieveLoansByMemberId(Long memberId);
    
    public LoanEntity retrieveLoanByBookId(Long bookId) throws LoanNotFoundException;

    public LoanEntity extendLoan(LoanEntity loanToUpdate) throws LoanException;

    public void deleteLoan(Long bookId) throws LoanNotFoundException;
}
