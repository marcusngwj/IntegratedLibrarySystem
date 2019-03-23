package util.exception;

public class BookNotFoundException extends Exception {
    public BookNotFoundException() {}
    
    public BookNotFoundException(String message) {
        super(message);
    }
}