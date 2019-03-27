package ejb.session.stateless;

import entity.LoanEntity;
import java.util.List;
import util.exception.UnsuccessfulLoanException;

public interface LoanEntityControllerRemote {

    public LoanEntity persistNewLoanEntity(LoanEntity newLoan) throws UnsuccessfulLoanException;

    public List<LoanEntity> retrieveAllLoans();
    
    public List<LoanEntity> retrieveLoansByMemberId(Long memberId);
}
