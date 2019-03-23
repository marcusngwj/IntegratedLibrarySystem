package util.exception;

public class StaffNotFoundException extends Exception {
    public StaffNotFoundException() {}
    
    public StaffNotFoundException(String message) {
        super(message);
    }
}
