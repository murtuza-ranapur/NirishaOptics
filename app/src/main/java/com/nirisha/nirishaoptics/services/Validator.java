package com.nirisha.nirishaoptics.services;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hp on 13-08-2017.
 */

public class Validator {
    public static final String EMAIL="email";
    public static final String EMPTY="empty";
    public static final String PASSWORD="password";
    public static final String LENGTH="length";

    private static String message=null;

    public static String getErrorMessage(){
        return message;
    }
    public static boolean forEmail(String input){
        if(!forEmpty(input))
            return false;
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches())
            return true;
        else {
            message="Invalid Email";
            return false;
        }
    }

    public static boolean forEmpty(String input){
        if(input.isEmpty()){
            message="This field cannot be empty";
            return false;
        }
        return true;
    }

    public static boolean forPassword(String input,int minimumLength){
        if(!forLength(input,minimumLength))
            return false;
        return true;
    }

    public static boolean forLength(String input,int minimumLength){
        if(!forEmpty(input))
            return false;
        if (input.length()<minimumLength){
            message="Atleast "+minimumLength+" character required";
            return false;
        }
        return true;
    }

}
