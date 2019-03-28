package ejb.session.stateless;

import entity.FineEntity;

public interface FineEntityControllerRemote {
    
     public FineEntity persistNewFineEntity(FineEntity newFine);
    
}
