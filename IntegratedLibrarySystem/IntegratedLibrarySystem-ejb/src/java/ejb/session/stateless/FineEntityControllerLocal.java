package ejb.session.stateless;

import entity.FineEntity;
import entity.LoanEntity;
import entity.MemberEntity;
import java.util.List;
import util.exception.FineNotFoundException;

public interface FineEntityControllerLocal {

    public FineEntity createNewFineEntity(LoanEntity loan, MemberEntity member);
    
    public FineEntity retrieveFineByFineId(Long fineId) throws FineNotFoundException;

    public List<FineEntity> retrieveFinesByMemberIdentityNumber(String memberIdentityNumber);

    public void deleteFine(Long fineId) throws FineNotFoundException;

}
