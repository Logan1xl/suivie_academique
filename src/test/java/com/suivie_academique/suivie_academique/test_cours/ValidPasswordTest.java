package com.suivie_academique.suivie_academique.test_cours;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.suivie_academique.suivie_academique.utils.PassWordValidator;

public class ValidPasswordTest {
    static String password ="12345";
    @BeforeEach
    void beforeTestvalid(){
        System.out.println("Travaille avant le test");
        password = password + "6";


    }
    @AfterEach
    void afterisValid(){
        System.out.println("fin du test "  +password);
    }
    @Test
    public  void testPassword(){
//        PassWordValidator passWordValidator = new PassWordValidator();
        Assertions.assertEquals(true,PassWordValidator.isValid("12345"),"Verifier longueur mot de passe");

    }
}
