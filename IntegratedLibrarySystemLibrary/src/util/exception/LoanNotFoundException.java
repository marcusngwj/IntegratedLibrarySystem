package util.exception;

public class LoanNotFoundException extends Exception {
    
    public LoanNotFoundException() {}
    
    public LoanNotFoundException(String message) {
        super(message);
    }
}