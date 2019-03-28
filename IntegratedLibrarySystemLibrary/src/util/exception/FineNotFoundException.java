package util.exception;

public class FineNotFoundException extends Exception {
    public FineNotFoundException() {}
    
    public FineNotFoundException(String message) {
        super(message);
    }
}