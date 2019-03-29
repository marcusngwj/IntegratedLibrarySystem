package ejb.session.stateless;

import entity.ReservationEntity;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.ReservationNotFoundException;
import util.logger.Logger;

@Stateless
@Local(ReservationEntityControllerLocal.class)
@Remote(ReservationEntityControllerRemote.class)
public class ReservationEntityController implements ReservationEntityControllerRemote, ReservationEntityControllerLocal {
    
    @PersistenceContext(unitName = "IntegratedLibrarySystem-ejbPU")
    private EntityManager em;
    
    @Override
    public ReservationEntity createNewReservationEntity(ReservationEntity newReservation) {
        Logger.log(Logger.INFO, "ReservationEntityController", "createNewReservationEntity");
        
        em.persist(newReservation);
        em.flush();
        em.refresh(newReservation);
        return newReservation;
    }
    
    @Override
    public ReservationEntity retrieveReservationById(Long reservationId) throws ReservationNotFoundException {
        Query query = em.createQuery("SELECT r FROM ReservationEntity r WHERE r.reservationId= :inReservationId");
        query.setParameter("inReservationId", reservationId);
        
        try {
            return (ReservationEntity)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex) {
            throw new ReservationNotFoundException("Reservation with reservation id " + reservationId + " does not exist!");
        }
    }
    
    @Override
    public List<ReservationEntity> retrieveReservationsByBookId(Long bookId) {
        Query query = em.createQuery("SELECT r FROM ReservationEntity r WHERE r.book.bookId= :inBookId");
        query.setParameter("inBookId", bookId);
        
        return query.getResultList();
    }
    
    @Override
    public ReservationEntity retrieveTopReservationOfBookId(Long bookId) throws ReservationNotFoundException {
        Query query = em.createQuery("SELECT r FROM ReservationEntity r WHERE r.book.bookId= :inBookId ORDER BY r.createdOn ASC");
        query.setParameter("inBookId", bookId);
        query.setMaxResults(1);
        
        try {
            return (ReservationEntity)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex) {
            throw new ReservationNotFoundException("Reservation does not exist!");
        }
    }
    
    @Override
    public void deleteReservation(Long reservationId) throws ReservationNotFoundException {
        ReservationEntity loan = retrieveReservationById(reservationId);
        em.remove(loan);
    }
    
    @Override
    public boolean hasOtherReservationsPriorToMember(Long memberId, Long bookId) throws ReservationNotFoundException {
        ReservationEntity topReservation = retrieveTopReservationOfBookId(bookId);
        
        if (topReservation!=null && !topReservation.getMember().getMemberId().equals(memberId)) {
            return true;
        }
        else {
            return false;
        }
    }

}
