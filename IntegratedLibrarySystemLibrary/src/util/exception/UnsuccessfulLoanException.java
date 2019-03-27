package util.exception;

public class UnsuccessfulLoanException extends Exception {
    public static final String BOOK_ON_LOAN = "Book is still being lent.";
    public static final String EXCEED_LOAN_LIMIT = "Member has already borrowed a maximum of 3 books";
    public static final String UNPAID_FINE = "Member has unpaid fines.";
    
    public UnsuccessfulLoanException() {}
    
    public UnsuccessfulLoanException(String message) {
        super(message);
    }
}
