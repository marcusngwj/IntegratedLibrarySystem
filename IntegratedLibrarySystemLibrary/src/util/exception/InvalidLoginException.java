package util.exception;

public class InvalidLoginException extends Exception {
    public static final String INVALID_CREDENTIALS = "Invalid username or password.";
    
    public InvalidLoginException() {}
    
    public InvalidLoginException(String message) {
        super(message);
    }
}
