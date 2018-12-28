package com.kevbotstudios.main;

import java.util.Scanner;

public class LogInAuthDriver {

    public static void main(String[] args) {

        LogInAuth log = new LogInAuth();
        Scanner s = new Scanner(System.in);
        String password;

        System.out.println("Enter Password On Registration");
        password = s.next();

        log.generateMD5Password(password, log.generateSalt());

        log.verifyLogIn(password);
    }
}
