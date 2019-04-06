package util.exception;

public class InvalidLoginException extends Exception {
    public static final String INVALID_CREDENTIALS = "Invalid username or password.";
    public static final String INVALID_MEMBER_CREDENTIALS = "Invalid Identity Number or Security Code.";
    
    public InvalidLoginException() {}
    
    public InvalidLoginException(String message) {
        super(message);
    }
}
