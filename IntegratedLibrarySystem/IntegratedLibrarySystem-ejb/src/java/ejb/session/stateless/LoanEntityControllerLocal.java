package ejb.session.stateless;

import entity.LoanEntity;
import java.util.List;
import util.exception.LoanException;
import util.exception.LoanNotFoundException;

public interface LoanEntityControllerLocal {
    public LoanEntity persistNewLoanEntity(LoanEntity newLoan) throws LoanException;

    public List<LoanEntity> retrieveAllLoans();

    public List<LoanEntity> retrieveLoansByMemberId(Long memberId);
    
    public LoanEntity retrieveLoanByBookId(Long bookId) throws LoanNotFoundException;

    public LoanEntity updateLoan(LoanEntity loanToUpdate) throws LoanException;
}
