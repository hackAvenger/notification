package com.credable.notification.Utils;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GenericUtil {
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean isValidEmail(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.find();
    }
    
    public static Integer getRandomOTP() {
      return new Random().nextInt(999999 - 100000) + 100000;
    }
}
