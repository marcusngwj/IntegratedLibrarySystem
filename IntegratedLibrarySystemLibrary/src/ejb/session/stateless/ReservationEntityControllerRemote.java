package ejb.session.stateless;

import entity.ReservationEntity;
import java.util.List;
import util.exception.ReservationNotFoundException;

public interface ReservationEntityControllerRemote {
    
    public ReservationEntity createNewReservationEntity(ReservationEntity newReservation);
    
    public ReservationEntity retrieveReservationById(Long reservationId) throws ReservationNotFoundException;
    
    public List<ReservationEntity> retrieveReservationsByBookId(Long bookId);
    
    public void deleteReservation(Long reservationId) throws ReservationNotFoundException;
    
}
