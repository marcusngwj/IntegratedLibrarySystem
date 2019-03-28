package ejb.session.stateless;

import entity.FineEntity;
import java.util.List;
import util.exception.FineNotFoundException;

public interface FineEntityControllerRemote {
    
    public FineEntity persistNewFineEntity(FineEntity newFine);
    
    public FineEntity retrieveFineByFindId(Long fineId) throws FineNotFoundException;
     
    public List<FineEntity> retrieveFinesByMemberIdentityNumber(String memberIdentityNumber);
    
    public void deleteFine(Long fineId) throws FineNotFoundException;
    
}
