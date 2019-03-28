package util.exception;

public class ReservationNotFoundException extends Exception {
    public ReservationNotFoundException() {}
    
    public ReservationNotFoundException(String message) {
        super(message);
    }
}