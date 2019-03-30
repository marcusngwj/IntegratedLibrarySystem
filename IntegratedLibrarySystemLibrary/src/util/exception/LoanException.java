package util.exception;

public class LoanException extends Exception {
    public static final String BOOK_ON_LOAN = "Book is still being lent.";
    public static final String BOOK_IS_OVERDUE = "Book is already overdue.";
    public static final String BOOK_IS_RESERVED = "Book is reserved by another member.";
    public static final String EXCEED_LOAN_LIMIT = "Member has already borrowed a maximum of 3 books.";
    public static final String UNPAID_FINE = "Member has unpaid fines.";
    public static final String BOOK_LOANED_NO_RESERVED = "Member is not allowed to reserve book that he/she is currently loaning";
    
    public LoanException() {}
    
    public LoanException(String message) {
        super(message);
    }
}
