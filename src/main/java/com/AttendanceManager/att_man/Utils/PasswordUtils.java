package com.AttendanceManager.att_man.Utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtils {
    private static final BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
    public static String encodePassword(String password){
        return password;
    }
    public static boolean validatePassword(String encryptPass,String enteredPass){
        // String temp = encodePassword(enteredPass);
        // return encoder.matches(encryptPass,temp);

        return encryptPass.equals(enteredPass);
    }
}
