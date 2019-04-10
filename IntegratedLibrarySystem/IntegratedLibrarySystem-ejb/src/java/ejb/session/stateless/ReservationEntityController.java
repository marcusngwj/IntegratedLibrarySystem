package ejb.session.stateless;

import entity.BookEntity;
import entity.FineEntity;
import entity.LoanEntity;
import entity.MemberEntity;
import entity.ReservationEntity;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.LoanException;
import util.exception.LoanNotFoundException;
import util.exception.ReservationException;
import util.exception.ReservationNotFoundException;
import util.helper.DateHelper;
import util.logger.Logger;

@Stateless
@Local(ReservationEntityControllerLocal.class)
@Remote(ReservationEntityControllerRemote.class)
public class ReservationEntityController implements ReservationEntityControllerRemote, ReservationEntityControllerLocal {

    @PersistenceContext(unitName = "IntegratedLibrarySystem-ejbPU")
    private EntityManager em;

    @EJB
    private FineEntityControllerLocal fineEntityControllerLocal;

    @EJB
    private BookEntityControllerLocal bookEntityControllerLocal;

    @EJB
    private MemberEntityControllerLocal memberEntityControllerLocal;

    @EJB
    private LoanEntityControllerLocal loanEntityControllerLocal;

    @Override
    public ReservationEntity createNewReservationEntity(ReservationEntity newReservation) throws LoanException, ReservationException, LoanNotFoundException {
        Logger.log(Logger.INFO, "ReservationEntityController", "createNewReservationEntity");
        /*
        • Member can reserve books that are already lent or on with other reservations using his/her member identity number.
        • Member cannot reserve books that are currently available in the library without any reservations.
        • Member cannot make multiple reservations on the same book.
        • Member cannot reserve books currently loaned by him/her.
        • Members with unpaid fines cannot reserve books.
         */
        MemberEntity currMember = newReservation.getMember();
        BookEntity currBook = newReservation.getBook();

        boolean isAvailableForReservation = isBookOnLoan(currBook) || isBookOnReservation(currBook);
        
        if (isAvailableForReservation) {
            //Members with unpaid fines cannot reserve books.
            boolean hasUnpaidFines = checkUnpaidFines(currMember);
            if (hasUnpaidFines) {
                throw new LoanException(LoanException.UNPAID_FINE);
            }
            
            //Member cannot reserve books currently loaned by him/her.
            boolean hasAlreadyLoanedBook = checkLoaningSameBook(currMember, currBook);
            if (hasAlreadyLoanedBook) {
                throw new LoanException(LoanException.BOOK_LOANED_NO_RESERVED);
            }
            
            //Member cannot make multiple reservations on the same book.
            boolean hasAlreadyReservedBook = checkMultipleReserved(currMember, currBook);
            if (hasAlreadyReservedBook) {
                throw new ReservationException(ReservationException.MULTIPLE_RESERVATION);
            }
            
            Date newDueDate = DateHelper.getCurrentDate();
            newReservation.setCreatedOn(newDueDate);
            
            em.persist(newReservation);
            em.flush();
            em.refresh(newReservation);
            return newReservation;
        } else {
            throw new ReservationException(ReservationException.NO_LOAN_RESERVATION);
        }
    }

    private boolean isBookOnReservation(BookEntity currBook) {
        List<ReservationEntity> reservationList = retrieveReservationsByBookId(currBook.getBookId());
        if (reservationList.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    private boolean isBookOnLoan(BookEntity currBook) {
        try {
            LoanEntity loan = loanEntityControllerLocal.retrieveLoanByBookId(currBook.getBookId());
            return true;
        } catch (LoanNotFoundException ex) {
            return false;
        }

    }

    private boolean checkMultipleReserved(MemberEntity currMember, BookEntity currBook) {
        List<ReservationEntity> reservationList = retrieveReservationsByBookId(currBook.getBookId());
        if (reservationList.isEmpty()) {
            return false;
        } else {
            for (int i = 0; i < reservationList.size(); i++) {
                ReservationEntity reservationEntity = reservationList.get(i);
                if (reservationEntity.getMember().getIdentityNumber().equals(currMember.getIdentityNumber())) {
                    return true;
                }
            }
            return false;
        }
    }

    private boolean checkLoaningSameBook(MemberEntity currMember, BookEntity currBook) {
        List<LoanEntity> loanList = loanEntityControllerLocal.retrieveLoansByMemberId(currMember.getMemberId());
        if (loanList.isEmpty()) {
            return false;
        } else {
            for (int i = 0; i < loanList.size(); i++) {
                Long loanedBookId = loanList.get(i).getBook().getBookId();
                if (loanedBookId.equals(currBook.getBookId())) {
                    return true;
                }
            }
            return false;
        }
    }

    private boolean checkUnpaidFines(MemberEntity currMember) {
        List<FineEntity> fineList = fineEntityControllerLocal.retrieveFinesByMemberIdentityNumber(currMember.getIdentityNumber());
        if (fineList.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ReservationEntity retrieveReservationById(Long reservationId) throws ReservationNotFoundException {
        Query query = em.createQuery("SELECT r FROM ReservationEntity r WHERE r.reservationId= :inReservationId");
        query.setParameter("inReservationId", reservationId);

        try {
            return (ReservationEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
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
            return (ReservationEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
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

        if (topReservation != null && !topReservation.getMember().getMemberId().equals(memberId)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Date retrieveLatestReservationDate(Long bookId) throws ReservationNotFoundException {

        Query query = em.createQuery("SELECT r FROM ReservationEntity r WHERE r.book.bookId= :inBookId ORDER BY r.createdOn DESC");
        query.setParameter("inBookId", bookId);
        query.setMaxResults(1);

        try {
            ReservationEntity latestRes = (ReservationEntity) query.getSingleResult();
            Date latestDate = latestRes.getCreatedOn();

            return latestDate;
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new ReservationNotFoundException("Reservation does not exist!");
        }
    }
}
