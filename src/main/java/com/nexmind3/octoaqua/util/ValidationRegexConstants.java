package com.nexmind3.octoaqua.util;

public class ValidationRegexConstants {

    /**
     * Password must include at least one digit, a lower case alphabet, an upper case alphabet.
     * Also must have at least 8 character and at most 20 character
    **/
    public static final String  PASSWORD_VALIDATOR ="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[#?!@$%^&*-_]).{8,20}$";

    /**
     * Email validator regex
     **/
    public static final String  EMAIL_VALIDATOR ="^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";



    public static final char[] POSSIBLE_CHARS = (new String("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789")).toCharArray();

}
