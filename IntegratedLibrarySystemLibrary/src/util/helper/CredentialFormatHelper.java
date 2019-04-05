package util.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CredentialFormatHelper {
    public static boolean isValidMemberIdentityNumberFormat(String identityNumber) {
        final String ID_PATTERN = "^[a-zA-Z]{1}?[0-9]+[a-zA-Z]{1}?";
        Pattern idPattern = Pattern.compile(ID_PATTERN);
        Matcher idMatcher = idPattern.matcher(identityNumber);
        return idMatcher.matches();
    }
    
    public static boolean isValidSecurityCodeFormat(String securityCode) {
        final String SECURITY_PATTERN = "[0-9]{6}";
        Pattern securityPattern = Pattern.compile(SECURITY_PATTERN);
        Matcher securityMatcher = securityPattern.matcher(securityCode);
        return securityMatcher.matches();
    }
    
    public static boolean isValidNameFormat(String name) {
        final String NAME_PATTERN = "[a-zA-Z]+";
        Pattern namePattern = Pattern.compile(NAME_PATTERN);
        Matcher nameMatcher = namePattern.matcher(name);
        return nameMatcher.matches();
    }
    
    public static boolean isValidGenderFormat(String gender) {
        final String GENDER_PATTERN = "([M|Male|male]|[F|Female|female]){1}";
        Pattern genderPattern = Pattern.compile(GENDER_PATTERN);
        Matcher genderMatcher = genderPattern.matcher(gender);
        return genderMatcher.matches();
    }
    
    public static boolean isValidAgeFormat(String age) {
        final String AGE_PATTERN = "[0-9]+";
        Pattern agePattern = Pattern.compile(AGE_PATTERN);
        Matcher ageMatcher = agePattern.matcher(age);
        return ageMatcher.matches();
    }
    
    public static boolean isValidPhoneFormat(String phone) {
        final String PHONE_PATTERN = "[0-9]+";
        Pattern phonePattern = Pattern.compile(PHONE_PATTERN);
        Matcher phoneMatcher = phonePattern.matcher(phone);
        return phoneMatcher.matches();
    }
    
    public static boolean isValidAddressFormat(String address) {
        final String ADDRESS_PATTERN = "(([a-zA-Z0-9]+)\\s*)+";
        Pattern addressPattern = Pattern.compile(ADDRESS_PATTERN);
        Matcher addressMatcher = addressPattern.matcher(address);
        return addressMatcher.matches();
    }
    
    public static String convertToStandardGenderFormat(String gender) {
        if(gender.equals("M") || gender.equals("male")) {
            return "Male";
        } 
        else if(gender.equals("F") || gender.equals("female")) {
            return "Female";
        }
        else {
            return gender;
        }
    }
}
