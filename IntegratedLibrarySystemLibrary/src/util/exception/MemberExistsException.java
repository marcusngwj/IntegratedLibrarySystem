package util.exception;

public class MemberExistsException extends Exception {
    public MemberExistsException() {}

    public MemberExistsException(String msg) {
        super(msg);
    }
}
