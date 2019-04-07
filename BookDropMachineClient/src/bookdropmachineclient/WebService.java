package bookdropmachineclient;

import java.util.Date;
import java.util.List;
import ws.client.BookEntity;
import ws.client.BookNotFoundException_Exception;
import ws.client.FineEntity;
import ws.client.FineNotFoundException_Exception;
import ws.client.IntegratedLibraryWebService;
import ws.client.IntegratedLibraryWebService_Service;
import ws.client.InvalidLoginException_Exception;
import ws.client.LoanEntity;
import ws.client.LoanException_Exception;
import ws.client.LoanNotFoundException_Exception;
import ws.client.MemberEntity;
import ws.client.MultipleReservationException_Exception;
import ws.client.ReservationEntity;
import ws.client.ReservationNotFoundException_Exception;

/**
 *
 * @author ang
 */
public class WebService {

    public static MemberEntity memberLogin(String username, String password) throws InvalidLoginException_Exception {
        IntegratedLibraryWebService_Service service = new IntegratedLibraryWebService_Service();
        IntegratedLibraryWebService port = service.getIntegratedLibraryWebServicePort();
        return port.memberLogin(username, password);
    }

    public static List<LoanEntity> retrieveLoansByMemberId(Long memberId) {
        IntegratedLibraryWebService_Service service = new IntegratedLibraryWebService_Service();
        IntegratedLibraryWebService port = service.getIntegratedLibraryWebServicePort();
        return port.retrieveLoansByMemberId(memberId);
    }

    public static void removeLoan(Long bookId, Long memberId) throws LoanNotFoundException_Exception {
        IntegratedLibraryWebService_Service service = new IntegratedLibraryWebService_Service();
        IntegratedLibraryWebService port = service.getIntegratedLibraryWebServicePort();
        port.removeLoan(bookId, memberId);
    }

    public static LoanEntity extendLoan(Long bookId, Long memberId) throws LoanNotFoundException_Exception, LoanException_Exception {
        IntegratedLibraryWebService_Service service = new IntegratedLibraryWebService_Service();
        IntegratedLibraryWebService port = service.getIntegratedLibraryWebServicePort();
        return port.extendLoan(bookId, memberId);
    }

    public static List<FineEntity> retrieveFinesByMemberIdentityNumber(String memberIdentityNumber) {
        IntegratedLibraryWebService_Service service = new IntegratedLibraryWebService_Service();
        IntegratedLibraryWebService port = service.getIntegratedLibraryWebServicePort();
        return port.retrieveFinesByMemberIdentityNumber(memberIdentityNumber);
    }

    public static void removeFine(Long fineId, Long memberId) throws FineNotFoundException_Exception {
        IntegratedLibraryWebService_Service service = new IntegratedLibraryWebService_Service();
        IntegratedLibraryWebService port = service.getIntegratedLibraryWebServicePort();
        port.removeFine(fineId, memberId);
    }

    public static List<BookEntity> searchBookByTitle(String title) throws BookNotFoundException_Exception {
        IntegratedLibraryWebService_Service service = new IntegratedLibraryWebService_Service();
        IntegratedLibraryWebService port = service.getIntegratedLibraryWebServicePort();
        return port.searchBookByTitle(title);
    }

    public static LoanEntity retrieveLoanByBookId(Long bookId) throws LoanNotFoundException_Exception {
        IntegratedLibraryWebService_Service service = new IntegratedLibraryWebService_Service();
        IntegratedLibraryWebService port = service.getIntegratedLibraryWebServicePort();
        return port.retrieveLoanByBookId(bookId);
    }

    public static List<ReservationEntity> retrieveReservationsByBookId(Long bookId) {
        IntegratedLibraryWebService_Service service = new IntegratedLibraryWebService_Service();
        IntegratedLibraryWebService port = service.getIntegratedLibraryWebServicePort();
        return port.retrieveReservationsByBookId(bookId);
    }

    public static Date retrieveLatestReservationDate(Long bookId) throws ReservationNotFoundException_Exception {
        IntegratedLibraryWebService_Service service = new IntegratedLibraryWebService_Service();
        IntegratedLibraryWebService port = service.getIntegratedLibraryWebServicePort();
        return port.retrieveLatestReservationDate(bookId).toGregorianCalendar().getTime();
    }

    public static BookEntity retrieveBookById(Long bookId) throws BookNotFoundException_Exception {
        IntegratedLibraryWebService_Service service = new IntegratedLibraryWebService_Service();
        IntegratedLibraryWebService port = service.getIntegratedLibraryWebServicePort();
        return port.retrieveBookById(bookId);
    }

    public static ReservationEntity createNewReservationEntity(ReservationEntity newReservation) throws LoanException_Exception, MultipleReservationException_Exception, LoanNotFoundException_Exception {
        IntegratedLibraryWebService_Service service = new IntegratedLibraryWebService_Service();
        IntegratedLibraryWebService port = service.getIntegratedLibraryWebServicePort();
        return port.createNewReservationEntity(newReservation);
    }
}
