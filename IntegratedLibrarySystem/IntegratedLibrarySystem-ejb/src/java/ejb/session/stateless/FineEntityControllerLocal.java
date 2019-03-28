package ejb.session.stateless;

import entity.FineEntity;
import javax.ejb.Local;

public interface FineEntityControllerLocal {

    public FineEntity persistNewFineEntity(FineEntity newFine);
    
}
