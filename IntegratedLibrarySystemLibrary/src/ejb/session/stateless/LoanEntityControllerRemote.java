package ejb.session.stateless;

import entity.LoanEntity;
import java.util.List;
import util.exception.LoanException;
import util.exception.LoanNotFoundException;

public interface LoanEntityControllerRemote {

    public LoanEntity createNewLoanEntity(LoanEntity newLoan) throws LoanException;

    public List<LoanEntity> retrieveAllLoans();
    
    public List<LoanEntity> retrieveLoansByMemberId(Long memberId);
    
    public LoanEntity retrieveLoanByBookId(Long bookId) throws LoanNotFoundException;
    
    public LoanEntity extendLoan(Long bookId, Long memberId) throws LoanNotFoundException, LoanException;
    
    public void removeLoan(Long bookId, Long memberId) throws LoanNotFoundException;
    
}
