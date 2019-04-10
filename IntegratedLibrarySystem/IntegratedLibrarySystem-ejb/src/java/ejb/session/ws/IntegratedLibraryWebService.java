package ejb.session.ws;

import ejb.session.stateless.BookEntityControllerLocal;
import ejb.session.stateless.FineEntityControllerLocal;
import ejb.session.stateless.LoanEntityControllerLocal;
import ejb.session.stateless.MemberEntityControllerLocal;
import ejb.session.stateless.ReservationEntityControllerLocal;
import entity.BookEntity;
import entity.FineEntity;
import entity.LoanEntity;
import entity.MemberEntity;
import entity.ReservationEntity;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.ejb.Stateless;
import javax.jws.WebParam;
import util.exception.BookNotFoundException;
import util.exception.FineNotFoundException;
import util.exception.InvalidLoginException;
import util.exception.LoanException;
import util.exception.LoanNotFoundException;
import util.exception.ReservationException;
import util.exception.ReservationNotFoundException;

@WebService(serviceName = "IntegratedLibraryWebService")
@Stateless

public class IntegratedLibraryWebService {

    @EJB
    private BookEntityControllerLocal bookEntityControllerLocal;
    @EJB
    private FineEntityControllerLocal fineEntityControllerLocal;
    @EJB
    private LoanEntityControllerLocal loanEntityControllerLocal;
    @EJB
    private MemberEntityControllerLocal memberEntityControllerLocal;
    @EJB
    private ReservationEntityControllerLocal reservationEntityControllerLocal;

    @WebMethod(operationName = "memberLogin")
    public MemberEntity memberLogin(@WebParam(name = "username") String username, @WebParam(name = "password") String password) throws InvalidLoginException {
        return memberEntityControllerLocal.memberLogin(username, password);
    }

    @WebMethod(operationName = "retrieveLoansByMemberId")
    public List<LoanEntity> retrieveLoansByMemberId(@WebParam(name = "memberId") Long memberId) {
        return loanEntityControllerLocal.retrieveLoansByMemberId(memberId);
    }

    @WebMethod(operationName = "removeLoan")
    public void removeLoan(@WebParam(name = "bookId") Long bookId, @WebParam(name = "memberId") Long memberId) throws LoanNotFoundException {
        loanEntityControllerLocal.removeLoan(bookId, memberId);
    }

    @WebMethod(operationName = "extendLoan")
    public LoanEntity extendLoan(@WebParam(name = "bookId") Long bookId, @WebParam(name = "memberId") Long memberId) throws LoanNotFoundException, LoanException {
        return loanEntityControllerLocal.extendLoan(bookId, memberId);
    }

    @WebMethod(operationName = "retrieveFinesByMemberIdentityNumber")
    public List<FineEntity> retrieveFinesByMemberIdentityNumber(@WebParam(name = "memberIdentityNumber") String memberIdentityNumber) {
        return fineEntityControllerLocal.retrieveFinesByMemberIdentityNumber(memberIdentityNumber);
    }

    @WebMethod(operationName = "removeFine")
    public void removeFine(@WebParam(name = "fineId") Long fineId, @WebParam(name = "memberId") Long memberId) throws FineNotFoundException {
        fineEntityControllerLocal.removeFine(fineId, memberId);
    }

    @WebMethod(operationName = "searchBookByTitle")
    public List<BookEntity> searchBookByTitle(@WebParam(name = "title") String title) throws BookNotFoundException {
        return bookEntityControllerLocal.searchBookByTitle(title);
    }

    @WebMethod(operationName = "retrieveLoanByBookId")
    public LoanEntity retrieveLoanByBookId(@WebParam(name = "bookId") Long bookId) throws LoanNotFoundException {
        return loanEntityControllerLocal.retrieveLoanByBookId(bookId);
    }

    @WebMethod(operationName = "retrieveReservationsByBookId")
    public List<ReservationEntity> retrieveReservationsByBookId(@WebParam(name = "bookId") Long bookId) {
        return reservationEntityControllerLocal.retrieveReservationsByBookId(bookId);
    }

    @WebMethod(operationName = "retrieveLatestReservationDate")
    public Date retrieveLatestReservationDate(@WebParam(name = "bookId") Long bookId) throws ReservationNotFoundException {
        return reservationEntityControllerLocal.retrieveLatestReservationDate(bookId);
    }

    @WebMethod(operationName = "retrieveBookById")
    public BookEntity retrieveBookById(@WebParam(name = "bookId") Long bookId) throws BookNotFoundException {
        return bookEntityControllerLocal.retrieveBookById(bookId);
    }

    @WebMethod(operationName = "createNewReservationEntity")
    public ReservationEntity createNewReservationEntity(@WebParam(name = "newReservation") ReservationEntity newReservation) throws LoanException, ReservationException, LoanNotFoundException {
        return reservationEntityControllerLocal.createNewReservationEntity(newReservation);
    }
}
