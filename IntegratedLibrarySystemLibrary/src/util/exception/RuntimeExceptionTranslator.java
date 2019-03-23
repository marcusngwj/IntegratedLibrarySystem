package util.exception;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RuntimeExceptionTranslator {
    public static final String DATABASE_INTEGRITY_CONSTRAINT_VIOLATION = "MySQLIntegrityConstraintViolationException";
    
    public static boolean isDatabseIntegrityConstantViolation(RuntimeException ex) {
        String internalExceptionName = getInternalExceptionName(ex);
        return internalExceptionName.equals(DATABASE_INTEGRITY_CONSTRAINT_VIOLATION);
    }
    
    private static String getInternalExceptionName(RuntimeException ex) {
        String message = ex.getCause().getMessage();
        Pattern pattern = Pattern.compile("[a-zA-Z]+Exception");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            return matcher.group(0);
        }
        else {
            return "";
        }
    }
}