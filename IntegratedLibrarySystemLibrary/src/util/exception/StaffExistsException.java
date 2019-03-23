package util.exception;

public class StaffExistsException extends Exception {
    public StaffExistsException() {}
    
    public StaffExistsException(String message) {
        super(message);
    }
}
