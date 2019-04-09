package util.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CredentialFormatHelper {
    public static boolean isValidMemberIdentityNumberFormat(String identityNumber) {
        final String ID_PATTERN = "^.{1,50}$";
        Pattern idPattern = Pattern.compile(ID_PATTERN);
        Matcher idMatcher = idPattern.matcher(identityNumber);
        return idMatcher.matches();
    }
    
    public static boolean isValidSecurityCodeFormat(String securityCode) {
        final String SECURITY_PATTERN = "^.{1,50}$";
        Pattern securityPattern = Pattern.compile(SECURITY_PATTERN);
        Matcher securityMatcher = securityPattern.matcher(securityCode);
        return securityMatcher.matches();
    }
    
    public static boolean isValidUsernameFormat(String username) {
        final String USERNAME_PATTERN = "^\\S{1,50}$";
        Pattern usernamePattern = Pattern.compile(USERNAME_PATTERN);
        Matcher usernameMatcher = usernamePattern.matcher(username);
        return usernameMatcher.matches();
    }
    
    public static boolean isValidPasswordFormat(String password) {
        final String PASSWORD_PATTERN = "^.{1,50}$";
        Pattern passwordPattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher passwordMatcher = passwordPattern.matcher(password);
        return passwordMatcher.matches();
    }
    
    public static boolean isValidNameFormat(String name) {
        final String NAME_PATTERN = "^[a-zA-Z\\s]{1,50}$";
        Pattern namePattern = Pattern.compile(NAME_PATTERN);
        Matcher nameMatcher = namePattern.matcher(name);
        return nameMatcher.matches();
    }
    
    public static boolean isValidGenderFormat(String gender) {
        final String GENDER_PATTERN = "^(M|Male|F|Female){1}$";
        Pattern genderPattern = Pattern.compile(GENDER_PATTERN, Pattern.CASE_INSENSITIVE);
        Matcher genderMatcher = genderPattern.matcher(gender);
        return genderMatcher.matches();
    }
    
    public static boolean isValidAgeFormat(String age) {
        final String AGE_PATTERN = "^[0-9]{1,3}$";
        Pattern agePattern = Pattern.compile(AGE_PATTERN);
        Matcher ageMatcher = agePattern.matcher(age);
        return ageMatcher.matches();
    }
    
    public static boolean isValidPhoneFormat(String phone) {
        final String PHONE_PATTERN = "^[0-9()\\-+\\s]{1,50}$";
        Pattern phonePattern = Pattern.compile(PHONE_PATTERN);
        Matcher phoneMatcher = phonePattern.matcher(phone);
        return phoneMatcher.matches();
    }
    
    public static boolean isValidAddressFormat(String address) {
        final String ADDRESS_PATTERN = "^[\\w\\-\\s,#]{1,255}$";
        Pattern addressPattern = Pattern.compile(ADDRESS_PATTERN);
        Matcher addressMatcher = addressPattern.matcher(address);
        return addressMatcher.matches();
    }
    
    public static boolean isValidBookTitleFormat(String title) {
        final String TITLE_PATTERN = "^.{1,255}$";
        Pattern titlePattern = Pattern.compile(TITLE_PATTERN);
        Matcher titleMatcher = titlePattern.matcher(title);
        return titleMatcher.matches();
    }
    
    public static boolean isValidYearFormat(String year) {
        final String YEAR_PATTERN = "^[0-9]{4}$";
        Pattern yearPattern = Pattern.compile(YEAR_PATTERN);
        Matcher yearMatcher = yearPattern.matcher(year);
        return yearMatcher.matches();
    }
    
    public static boolean isValidIsbnFormat(String isbn) {
        final String ISBN_PATTERN = "^[a-zA-Z0-9\\s\\-:]{1,50}$";
        Pattern isbnPattern = Pattern.compile(ISBN_PATTERN);
        Matcher isbnMatcher = isbnPattern.matcher(isbn);
        return isbnMatcher.matches();
    }
    
    public static String convertToStandardGenderFormat(String gender) {
        if(gender.equalsIgnoreCase("m") || gender.equalsIgnoreCase("male")) {
            return "Male";
        } 
        else if(gender.equalsIgnoreCase("f") || gender.equalsIgnoreCase("female")) {
            return "Female";
        }
        else {
            return gender;
        }
    }
}
