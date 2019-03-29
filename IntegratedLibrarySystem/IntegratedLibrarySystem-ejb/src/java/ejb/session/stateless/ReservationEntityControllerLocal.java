package ejb.session.stateless;

import entity.ReservationEntity;
import java.util.List;
import util.exception.LoanException;
import util.exception.LoanNotFoundException;
import util.exception.MultipleReservationException;
import util.exception.ReservationNotFoundException;

public interface ReservationEntityControllerLocal {

    public ReservationEntity createNewReservationEntity(ReservationEntity newReservation) throws LoanException, MultipleReservationException, LoanNotFoundException;
    
    public ReservationEntity retrieveReservationById(Long reservationId) throws ReservationNotFoundException;

    public List<ReservationEntity> retrieveReservationsByBookId(Long bookId);
    
    public ReservationEntity retrieveTopReservationOfBookId(Long bookId) throws ReservationNotFoundException;
    
    public void deleteReservation(Long reservationId) throws ReservationNotFoundException;

    public boolean hasOtherReservationsPriorToMember(Long memberId, Long bookId) throws ReservationNotFoundException;

}
