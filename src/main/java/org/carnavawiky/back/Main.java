package org.carnavawiky.back;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode("Deporte.10");

        System.out.println("HASH GENERADO: " + hash);
    }
}