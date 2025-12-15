package com.suivie_academique.suivie_academique.utils;

public class PassWordValidator {
    public static boolean  isValid(String pw) {
        if (pw.length() >= 5 && pw.length() <= 10)
            return true;
        else
            return false;
    }
    }


